package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.service.MenuService;
import ru.danis0n.getsiteinfobot.service.Parser;
import ru.danis0n.getsiteinfobot.service.ParserGenre;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final MenuService menuService;
    private final EventHandler eventHandler;
    private final BotStateCash botStateCash;
    private final Parser parser;
    private final ParserGenre parserGenre;

    public MessageHandler(UserDAO userDAO, MenuService menuService, EventHandler eventHandler, BotStateCash botStateCash, Parser parser, ParserGenre parserGenre) {
        this.userDAO = userDAO;
        this.menuService = menuService;
        this.eventHandler = eventHandler;
        this.botStateCash = botStateCash;
        this.parserGenre = parserGenre;
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
                System.out.println(parserGenre.parseStringsToGenres(parserGenre.getGenresStringsFromSite("https://shikimori.one/animes/menu")));
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
