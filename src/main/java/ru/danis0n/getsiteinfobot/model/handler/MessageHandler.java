package ru.danis0n.getsiteinfobot.model.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.danis0n.getsiteinfobot.DAO.StateDAO;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;
import ru.danis0n.getsiteinfobot.cash.BotStateCache;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.model.entities.UserState;
import ru.danis0n.getsiteinfobot.service.MenuService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {

    final UserDAO userDAO;
    final MenuService menuService;
    final EventHandler eventHandler;
    final BotStateCache botStateCache;
    final StateDAO stateDAO;

    public MessageHandler(UserDAO userDAO, MenuService menuService, EventHandler eventHandler, BotStateCache botStateCache, StateDAO stateDAO) {
        this.userDAO = userDAO;
        this.menuService = menuService;
        this.eventHandler = eventHandler;
        this.botStateCache = botStateCache;
        this.stateDAO = stateDAO;
    }

    public SendMessage handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(chatId));

        if(!userDAO.isExist(userId)){
            return eventHandler.saveNewUser(message,userId,replyMessage);
        }

        botStateCache.saveBotState(userId,botState);
        stateDAO.save(new UserState(userId,botState.name()));
        switch (botState.name()){
            case"START":
                return menuService.getMainMenuMessage(chatId,"Воспользуйтесь главным меню",userId);
            case"SHOWABOUTAUTHOR":
                return eventHandler.showAuthor(userId);
            case"SHOWTOPANIME":
                return eventHandler.showTopAnime(userId);
            case"SHOWGENRES":
                return eventHandler.showGenres(userId);
            case"HELP":
                return eventHandler.showHelp(userId);
            case"SHOWALLUSERS":
                return eventHandler.getAllUsers(userId);
            case"SHOWONGOINGS":
                return eventHandler.showOngoings(userId);
            case"ENTERNUMBERUSER":
                return eventHandler.removeUser(message,userId);
            case"ENTERNUMBERTITLEFORMORE":
                return eventHandler.getInfoAboutTitle(message,userId);
            case"ENTERNUMBERGENRE":
                return eventHandler.getTitlesByGenre(message,userId);
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}