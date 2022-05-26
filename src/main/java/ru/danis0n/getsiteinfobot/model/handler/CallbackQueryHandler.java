package ru.danis0n.getsiteinfobot.model.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.danis0n.getsiteinfobot.cash.BotStateCache;
import ru.danis0n.getsiteinfobot.model.BotState;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallbackQueryHandler {

    final BotStateCache botStateCache;

    @Autowired
    public CallbackQueryHandler(BotStateCache botStateCache) {
        this.botStateCache = botStateCache;
    }

    public SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        SendMessage callbackAnswer = new SendMessage();
        callbackAnswer.setChatId(String.valueOf(chatId));
        String data = callbackQuery.getData();
        switch (data){
            case"buttonMore":
                callbackAnswer.setText("Введите номер тайтла");
                botStateCache.saveBotState(userId, BotState.ENTERNUMBERTITLEFORMORE);
                return callbackAnswer;
            case"buttonSelect":
                callbackAnswer.setText("Введите номера/номер жанров");
                botStateCache.saveBotState(userId,BotState.ENTERNUMBERGENRE);
                return callbackAnswer;
        }
        return callbackAnswer;
    }
}
