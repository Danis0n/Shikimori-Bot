package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.service.MenuService;

@Component
public class CallbackQueryHandler {

    private final BotStateCash botStateCash;
    private final MenuService menuService;
    private final EventHandler eventHandler;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, MenuService menuService, EventHandler eventHandler) {
        this.botStateCash = botStateCash;
        this.menuService = menuService;
        this.eventHandler = eventHandler;
    }

    public SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        SendMessage callbackAnswer = new SendMessage();
        callbackAnswer.setChatId(String.valueOf(chatId));
        String data = callbackQuery.getData();
        switch (data){
            case"buttonDeleteUser":
                callbackAnswer.setText("Введите номер пользователя");
                botStateCash.saveBotState(userId, BotState.ENTERNUMBERUSER);
                return callbackAnswer;

        }
        return callbackAnswer;
    }

}
