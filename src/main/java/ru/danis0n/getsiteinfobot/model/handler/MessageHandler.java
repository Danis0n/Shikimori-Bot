package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.service.MenuService;
import ru.danis0n.getsiteinfobot.service.Parser;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final MenuService menuService;
    private final EventHandler eventHandler;
    private final BotStateCash botStateCash;
    private final Parser parser;

    public MessageHandler(UserDAO userDAO, MenuService menuService, EventHandler eventHandler, BotStateCash botStateCash, Parser parser) {
        this.userDAO = userDAO;
        this.menuService = menuService;
        this.eventHandler = eventHandler;
        this.botStateCash = botStateCash;
        this.parser = parser;
    }

    public SendMessage handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(chatId));

        if(!userDAO.isExist(userId)){
            return eventHandler.saveNewUser(message,userId,replyMessage);
        }

        botStateCash.saveBotState(userId,botState);

        switch (botState.name()){
            case"START":
                return menuService.getMainMenuMessage(chatId,"Воспользуйтесь главным меню",userId);
            case"SHOWABOUTAUTHOR":
                return eventHandler.showAuthor(userId);
            case"HELP":
                return eventHandler.showHelp(userId);
            case"SHOWALLUSERS":
                return eventHandler.getAllUsers(userId);
            case"SHOWPOPULARONGOINGS":
                return eventHandler.showOngoings(userId);
            case"SHOWANIME":
                return eventHandler.showAnime(userId);
            case"ENTERNUMBERUSER":
                return eventHandler.removeUser(message,userId);
            case"ENTERNUMBERTITLEFORMORE":
                return eventHandler.getInfoAboutTitle(message,userId);
            default:    
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
