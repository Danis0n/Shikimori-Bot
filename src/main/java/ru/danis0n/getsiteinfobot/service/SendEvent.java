package ru.danis0n.getsiteinfobot.service;

import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.danis0n.getsiteinfobot.config.ApplicationContextProvider;
import ru.danis0n.getsiteinfobot.model.TelegramBot;

@Setter
//thread with event
public class SendEvent extends Thread {

    private SendMessage sendMessage;

    public SendEvent() {
    }

    @SneakyThrows
    @Override
    public void run() {
        TelegramBot telegramBot = ApplicationContextProvider.getApplicationContext().getBean(TelegramBot.class);
        telegramBot.execute(sendMessage);
    }
}
