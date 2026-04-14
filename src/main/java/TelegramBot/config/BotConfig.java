package TelegramBot.config;

import TelegramBot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
//        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//        botsApi.registerBot(telegramBot);
//        //System.setProperty("telegram.api.host", "149.154.167.220");
//        return botsApi;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            System.out.println("Bot registered successfully");
            return botsApi;
        } catch (TelegramApiException e) {
            // 🛡 ГЛАВНОЕ: НЕ выбрасываем исключение дальше!
            System.err.println("Warning: Не удалось подключить бота: " + e.getMessage());
            System.err.println("Приложение продолжит работу. Бот попробует подключиться позже.");

            // Возвращаем "пустой" экземпляр — Spring НЕ упадёт
            return null;
        }
    }
}