package TelegramBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {


    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void SendWithoutURL(String ChatID) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Erase target user");
        button.setCallbackData("erase");
        keyboardButtonList.add(button);

        for (int i = 0; i < usersList.size(); i++) {
            InlineKeyboardButton tempbutton = new InlineKeyboardButton();
            tempbutton.setText(usersList.get(i)[0]);
            tempbutton.setCallbackData(usersList.get(i)[1]);
            keyboardButtonList.add(tempbutton);
        }
//        насоздавать кнопок с айди юзерови каллбеками
//        добавить кнопок с айди чатов юзеров
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonList);
        keyboard.setKeyboard(rowList);

        SendMessage msg = SendMessage.builder()
                .chatId(ChatID)
                .parseMode("Markdown")
                .text("Users list")
                .replyMarkup(keyboard)
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isFatherExist = false;
    private ArrayList<String[]> usersList = new ArrayList<>();
    private String fatherID = "";
    private String nextUserId = "";
    private boolean waitingForFatherResponse = false;

    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println("me");
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String name = message.getFrom().getFirstName();
            if (message.getFrom().getLastName() != null) {
                name = name + " " + message.getFrom().getLastName();
            }
            if (isFatherExist) {
                boolean userListContainsUser = false;
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i)[0].equals(name) && usersList.get(i)[1].equals(message.getChatId().toString())) {
                        userListContainsUser = true;
                    }
                }
                if (!userListContainsUser) {
                    String[] temp = {name, message.getChatId().toString()};
                    usersList.add(temp);
                }
                if (waitingForFatherResponse && message.getChatId().toString().equals(fatherID)) {
                    Answer(message.getText(), nextUserId);
                    //Answer("услышал", fatherID);
                    return;
                }
            }
            switch (message.getText()) {
                case "/start":
                    Answer("Hi " + name + "!", message.getChatId().toString());
                    Answer("Nice to meet you", message.getChatId().toString());
                    //SendWithoutURL(message.getChatId().toString());
                    break;
                case "/deleteFather":
                    if (message.getChatId().toString().equals(fatherID)) {
                        Answer("Father deleted", fatherID);
                        isFatherExist = false;
                        fatherID = "";
                    }
                case "239932":
                    if (isFatherExist) {
                        Answer("Не не не", message.getChatId().toString());
                        break;
                    }
                    isFatherExist = true;
                    Answer("Father mode enabled", message.getChatId().toString());
                    fatherID = message.getChatId().toString();
                    break;
                default:
                    if (isFatherExist && !message.getChatId().toString().equas(fatherID)) {
                        Answer("From " + name + ": \n" + message.getText(), fatherID);
                        //Answer("I can do nothing))", message.getChatId().toString());
                    } else if (message.getChatId().toString().equals(fatherID)) {
                        SendWithoutURL(fatherID);
                    }
                    Answer("угу (он все видит)", message.getChatId().toString());
                    break;
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage().getChatId().toString().equals(fatherID)) {
            if (update.getCallbackQuery().getData().equals("erase")) {
                Answer("Erased", update.getCallbackQuery().getMessage().getChatId().toString());
                waitingForFatherResponse = false;
                nextUserId = "";
            } else if (isFatherExist) {
                nextUserId = update.getCallbackQuery().getData();
                String nextUserName = " <error> ";
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i)[1].equals(nextUserId)) {
                        nextUserName = usersList.get(i)[0];
                    }
                }
                Answer("Начинайте диалог с " + nextUserName + ":", fatherID);
                waitingForFatherResponse = true;
            }
        }
    }

    private void Answer(String text, String chatID) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatID)
                .parseMode("Markdown")
                .text(text)
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}