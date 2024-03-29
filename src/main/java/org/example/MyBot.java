package org.example;

import java.util.Arrays;
import java.util.List;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.lang.Long.parseLong;

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
                List<String> textArray2 = Arrays.asList(s.split(" "));

                if (textArray2.size() == 2) {
                    var amount = (int) parseLong(textArray2.get(1));
                    getFullMessage(textArray2.get(0), chatId, amount);
                } else {
                    getFullMessage(s, chatId, 0);
                }
            }
        } else {
            List<String> textArray3 = Arrays.asList(text.split(" "));

            if (textArray3.size() == 2) {
                var amount = (int) parseLong(textArray3.get(1));
                getFullMessage(textArray3.get(0), chatId, amount);
            } else {
                getFullMessage(text, chatId, 0);
            }
        }
    }

    void sendPrice(long chatId, String name, long amount) throws Exception {
        var price = CryptoPrice.spotPrice(name);
        var availableAmount = amount > 0 ? "\n" + "can buy: " + (amount/price.getAmount().doubleValue()) : "";
        sendMessage(chatId, name + " price: " + price.getAmount().doubleValue() + availableAmount);
    }

    void sendPicture(long chatId, String name) throws Exception {
        var photo = getClass().getClassLoader().getResourceAsStream(name);

        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(photo, name));
        execute(message);
    }

    void sendMessage(long chatId, String text) throws Exception {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    void getFullMessage(String textMessage, Long chatId, long amount)  {
        try {
            var message = new SendMessage();
            message.setChatId(chatId);

            if (textMessage.equals("/start")) {
                sendMessage(chatId, "Hello!");
            } else if (textMessage.equals("btc")) {
                sendPicture(chatId, "bitcoin.png");
                sendPrice(chatId, "BTC", amount);
            } else if (textMessage.equals("eth")) {
                sendPicture(chatId, "ethereum.png");
                sendPrice(chatId, "ETH", amount);
            } else if (textMessage.equals("doge")) {
                sendPicture(chatId, "dogecoin.png");
                sendPrice(chatId, "DOGE", amount);
            }else if (textMessage.equals("/all")) {
                var priceBtc = CryptoPrice.spotPrice("BTC");
                var priceEth = CryptoPrice.spotPrice("ETH");
                var priceDoge = CryptoPrice.spotPrice("DOGE");
                sendMessage(chatId,
                        "BTC price: " + priceBtc.getAmount().doubleValue() + "\n"
                                + "ETH price: " + priceEth.getAmount().doubleValue() + "\n"
                                + "DOGE price: " + priceDoge.getAmount().doubleValue()
                );
            }else {
                sendMessage(chatId, "Unknown command!");
            }

        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    @Override
    public String getBotUsername() {
        return "LanaSvit5102_Bot";
    }
}
