package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler {
    public SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        return null;
    }
}
