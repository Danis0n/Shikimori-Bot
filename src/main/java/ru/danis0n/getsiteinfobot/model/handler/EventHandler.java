package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.model.entities.User;
import ru.danis0n.getsiteinfobot.service.MenuService;

import java.util.List;

@Component
public class EventHandler {

    private final UserDAO userDAO;
    private final BotStateCash botStateCash;
    private final MenuService menuService;

    @Value("${telegrambot.adminId}")
    private int adminId;

    public EventHandler(UserDAO userDAO, BotStateCash botStateCash, MenuService menuService) {
        this.userDAO = userDAO;
        this.botStateCash = botStateCash;
        this.menuService = menuService;
    }

    public SendMessage saveNewUser(Message message, long userId, SendMessage replyMessage) {
        String userName = message.getFrom().getUserName();
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        userDAO.save(user);
        replyMessage.setText("Добро пожаловать на огонёк к моему боту! =)");
        botStateCash.saveBotState(userId, BotState.START);
        return replyMessage;
    }

    public SendMessage getAllUsers(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        StringBuilder builder = new StringBuilder();
        List<User> users = userDAO.findAllUsers();

        for(User user : users){
            builder.append(buildUser(user));
        }

        replyMessage.setText(String.valueOf(builder));
        replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsAllUsers());
        return replyMessage;
    }

    public SendMessage showAuthor(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Телеграм-Шикимори-Бот. Версия 0.1. Автор: Danis0n");
        return replyMessage;
    }

    public SendMessage showHelp(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Данная функция находится в разработке! Ожидайте обновления");
        return replyMessage;
    }

    public SendMessage showOngoings(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Данная функция находится в разработке! Ожидайте обновления");
        return replyMessage;
    }

    public SendMessage showAnime(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Данная функция находится в разработке! Ожидайте обновления");
        return replyMessage;
    }

    private StringBuilder buildUser(User user) {
        StringBuilder builder = new StringBuilder();
        long userId = user.getId();
        String name = user.getName();
        builder.append(userId).append(". ").append(name).append("\n");
        return builder;
    }
}
