package ru.danis0n.getsiteinfobot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuService {

    private final UserDAO userDAO;
    private final Parser parser;

    @Value("${telegrambot.adminId}")
    private int adminId;

    public MenuService(UserDAO userDAO, Parser parser){
        this.userDAO = userDAO;
        this.parser = parser;
    }

    private boolean isAdmin(long userId){
        return adminId == userId;
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage, final long userId){
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(userId);
        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

    public SendMessage getAnimeOngoingsMessage(final long chadId, final String textMessage, final long userId){
        return null;
    }

    // sends the text message
    private SendMessage createMessageWithKeyboard(final long charId, String textMessage, final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(charId));
        sendMessage.setText(textMessage);
        if(replyKeyboardMarkup != null){
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            return sendMessage;
        }
        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard(long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow ongoingsPanel = new KeyboardRow();
        ongoingsPanel.add(new KeyboardButton("Онгоинги"));
        keyboard.add(ongoingsPanel);

        if(isAdmin(userId)){
            KeyboardRow adminPanel = new KeyboardRow();
            adminPanel.add(new KeyboardButton("Все пользователи"));
            keyboard.add(adminPanel);
        }

        KeyboardRow aboutPanel = new KeyboardRow();
        aboutPanel.add(new KeyboardButton("Об авторе"));
        aboutPanel.add(new KeyboardButton("Помощь"));
        keyboard.add(aboutPanel);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getAnimeTitles(long userId){
        return null;
    }

    public ReplyKeyboard getInlineMessageButtonsAllUsers() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonDeleteUser = new InlineKeyboardButton();
        buttonDeleteUser.setText("Delete user");
        buttonDeleteUser.setCallbackData("buttonDeleteUser");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonDeleteUser);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getInlineMessageButtonsAnime() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonMore = new InlineKeyboardButton();
        buttonMore.setText("Подробнее");
        buttonMore.setCallbackData("buttonMore");

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(buttonMore);

        List<List<InlineKeyboardButton>> row = new ArrayList<>();
        row.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(row);
        return inlineKeyboardMarkup;
    }
}
