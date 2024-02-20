package org.example;

import java.util.Arrays;
import java.util.List;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyBot extends TelegramLongPollingBot {
    public MyBot() {
        super("7002573080:AAG3UrF-MxRhGUrdJqw93iTOO1yUyP87lUY");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();
        List<String> textArray = Arrays.asList(text.split(","));

        if (!textArray.isEmpty()) {
            for (String s : textArray) {
                getMessage(s, chatId);
            }
        }
    }

    public void getMessage(String textMessage, Long chatId)  {
        try {
            var message = new SendMessage();
            message.setChatId(chatId);

            if (textMessage.equals("/start")) {
                message.setText("Hello!");
            } else if (textMessage.equals("btc")) {
                var price = CryptoPrice.spotPrice("BTC");
                message.setText("BTC price: " + price.getAmount().doubleValue());
            } else if (textMessage.equals("eth")) {
                var price = CryptoPrice.spotPrice("ETH");
                message.setText("ETH price: " + price.getAmount().doubleValue());
            } else if (textMessage.equals("doge")) {
                var price = CryptoPrice.spotPrice("DOGE");
                message.setText("DOGE price: " + price.getAmount().doubleValue());
            }else if (textMessage.equals("/all")) {
                var priceBtc = CryptoPrice.spotPrice("BTC");
                var priceEth = CryptoPrice.spotPrice("ETH");
                var priceDoge = CryptoPrice.spotPrice("DOGE");
                message.setText(
                        "BTC price: " + priceBtc.getAmount().doubleValue() + "\n"
                                + "ETH price: " + priceEth.getAmount().doubleValue() + "\n"
                                + "DOGE price: " + priceDoge.getAmount().doubleValue()
                );
            }else {
                message.setText("Unknown command!");
            }

            execute(message);
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    @Override
    public String getBotUsername() {
        return "LanaSvit5102_Bot";
    }
}
