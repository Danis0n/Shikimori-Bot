package ru.danis0n.getsiteinfobot.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.handler.CallbackQueryHandler;
import ru.danis0n.getsiteinfobot.model.handler.MessageHandler;

//TODO : IMPLEMENT BASIC LOGIC
//TODO : LEARN ABOUT JSON PROPERTIES
//TODO : LEARN ABOUT @AllArgsConstructor/@ToString/@JsonIgnoreProperties


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

    final MessageHandler messageHandler;
    final CallbackQueryHandler callbackQueryHandler;
    final BotStateCash botStateCash;

    @Value("${telegrambot.adminId}")
    int adminId;

    public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler, BotStateCash botStateCash) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botStateCash = botStateCash;
    }

    public SendMessage handleUpdate(Update update) {

        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else{
            Message message = update.getMessage();
            if(message != null && message.hasText()){
                return handleInputMessage(message);
            }
        }
        return null;
    }

    private SendMessage handleInputMessage(Message message) {

        BotState botState;
        String inputMsg = message.getText();

        switch (inputMsg){
            case"/start":
                botState = BotState.START;
                break;
            case"Онгоинги":
                botState = BotState.SHOWONGOINGS;
                break;
            case"Топ Аниме":
                botState = BotState.SHOWTOPANIME;
                break;
            case"Топ Манга":
                botState = BotState.SHOWTOPMANGA;
                break;
            case"Помощь":
                botState = BotState.HELP;
                break;
            case"Об авторе":
                botState = BotState.SHOWABOUTAUTHOR;
                break;
            case"Все пользователи":
                if(message.getFrom().getId() == adminId) botState = BotState.SHOWALLUSERS;
                else botState = BotState.START;
                break;
            default:
                botState = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null?
                        BotState.START: botStateCash.getBotStateMap().get(message.getFrom().getId());
        }
        return messageHandler.handle(message,botState);
    }

}