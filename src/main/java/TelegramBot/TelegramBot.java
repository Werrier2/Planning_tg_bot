package TelegramBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Console;
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
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Just another one button");
        button.setCallbackData("knopka");
        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        keyboardButtonList.add(button);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonList);
        keyboard.setKeyboard(rowList);

        SendMessage msg = SendMessage.builder()
                .chatId(ChatID)
                .parseMode("Markdown")
                .text("this is knopka")
                .replyMarkup(keyboard)
                .build();
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        //System.out.println("me");
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String name = message.getFrom().getFirstName();
            if (message.getFrom().getLastName() != null) {
                name = name + " " + message.getFrom().getLastName();
            }
            switch (message.getText()) {
                case "/start":
                    Answer("Hi " + name + "!", message.getChatId().toString());
                    Answer("Nice to meet you", message.getChatId().toString());
                    //SendWithoutURL(message.getChatId().toString());
                    break;
                case "spec":
                    Answer("Secret text tss", message.getChatId().toString());
                    break;
                default:
                    //Answer("I can do nothing))", message.getChatId().toString());
                    SendWithoutURL(message.getChatId().toString());
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("knopka")) {
                Answer("Это кнопка ничего не делает. Пока...", update.getCallbackQuery().getMessage().getChatId().toString());
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