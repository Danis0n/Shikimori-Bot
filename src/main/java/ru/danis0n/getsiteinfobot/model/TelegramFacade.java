package ru.danis0n.getsiteinfobot.model;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

//TODO : IMPLEMENT BASIC LOGIC
@Component
public class TelegramFacade {

    public SendMessage handleUpdate(Update update) {
        return null;
    }

}