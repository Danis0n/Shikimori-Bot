package ru.danis0n.getsiteinfobot.model.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.danis0n.getsiteinfobot.DAO.UserDAO;
import ru.danis0n.getsiteinfobot.cash.BotStateCash;
import ru.danis0n.getsiteinfobot.model.BotState;
import ru.danis0n.getsiteinfobot.model.entities.AnimeTitle;
import ru.danis0n.getsiteinfobot.model.entities.Genre;
import ru.danis0n.getsiteinfobot.model.entities.User;
import ru.danis0n.getsiteinfobot.service.MenuService;
import ru.danis0n.getsiteinfobot.service.Parser;
import ru.danis0n.getsiteinfobot.service.ParserGenre;

import java.util.List;

@Component
public class EventHandler {

    private final UserDAO userDAO;
    private final BotStateCash botStateCash;
    private final MenuService menuService;
    private final Parser parser;
    private final ParserGenre parserGenre;

    @Value("${telegrambot.adminId}")
    private int adminId;

    public EventHandler(UserDAO userDAO, BotStateCash botStateCash, MenuService menuService, Parser parser, ParserGenre parserGenre) {
        this.userDAO = userDAO;
        this.botStateCash = botStateCash;
        this.menuService = menuService;
        this.parser = parser;
        this.parserGenre = parserGenre;
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

        int i = 0;
        for(User user : users){
            builder.append(++i).append(" ").append(buildUser(user));
        }
        replyMessage.setText(String.valueOf(builder));
        return replyMessage;
    }

    public SendMessage showAuthor(long userId) {
        SendMessage replyMessage = new SendMessage();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(userId));

        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Телеграм-Шикимори-Бот. Версия 0.2. Автор: Danis0n\n Гитхаб: https://github.com/Danis0n");
        return replyMessage;
    }

    public SendMessage showHelp(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Данная функция находится в разработке! Ожидайте обновления");
        return replyMessage;
    }

    public SendMessage showOngoings(long userId) {
        parser.setTitles(parser.getTopAnimeFromString(parser.getOngoingsFromSite("https://shikimori.one")));

        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        StringBuilder builder = new StringBuilder();
        List<AnimeTitle> titles = parser.getTitles();

        int i = 0;
        for(AnimeTitle title : titles){
            builder.append(++i).append(" ").append(buildTitle(title)).append("\n");
        }
        replyMessage.setText(String.valueOf(builder));
        replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsAnime());
        return replyMessage;
    }

    public SendMessage showAnime(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Данная функция находится в разработке! Ожидайте обновления");
        return replyMessage;
    }

    public StringBuilder buildTitle(AnimeTitle animeTitle){
        String name = animeTitle.getName();
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        return builder;
    }

    private StringBuilder buildUser(User user) {
        StringBuilder builder = new StringBuilder();
        long userId = user.getId();
        String name = user.getName();
        builder.append(userId).append(". ").append(name).append("\n");
        return builder;
    }

    public SendMessage removeUser(Message message, long userId) {
        SendMessage sendMessage = new SendMessage();

        User user;
        try{
            long i = Long.parseLong(message.getText());
            System.out.println(i);
            user = userDAO.findByUserId(i);
        }catch (NumberFormatException e){
            sendMessage.setText("Введенная строка не является числом, попробуйте снова!");
            return sendMessage;
        }

        if(user == null){
            sendMessage.setText("Введенное число отсутсвует в списке, попробуйте снова!");
            return sendMessage;
        }

        userDAO.removeUser(user);
        botStateCash.saveBotState(userId,BotState.START);

        sendMessage.setText("Удаление пользователя произошло успешно");
        return sendMessage;
    }

    public StringBuilder buildFullInfoTitle(AnimeTitle title, String type,
                                            String currentEpisodes, String nextEpisode,
                                            String duration, String imgSrc
    )
    {
        StringBuilder builder = new StringBuilder();

        // КОСТЫЛЬ
        if(!currentEpisodes.contains("/")){
            builder.append(buildTitle(title)).append("\n").
                    append("Тип: ").append(type).append("\n").
                    append("Эпизоды: ").append(currentEpisodes).append("\n").
                    append("Длительность эпизода: ").append(nextEpisode).
                    append("\n").append(imgSrc);
        }
        else{
        builder.append(buildTitle(title)).append("\n").
                append("Тип: ").append(type).append("\n").
                append("Эпизоды: ").append(currentEpisodes).append("\n").
                append("Следующий эпизод: ").append(nextEpisode).append("\n").
                append("Длительность эпизода: ").append(duration).
                append("\n").append(imgSrc);
        }
        return builder;
    }

    public SendMessage getInfoAboutTitle(Message message, long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        AnimeTitle title;

        try {
            title = parser.getTitles().get(Integer.parseInt(message.getText()) - 1);
        } catch (IndexOutOfBoundsException | NumberFormatException e){
            replyMessage.setText("Неверный id");
            return replyMessage;
        }

        List<String> titleProperties = parser.parseTitle(title.getUrl());

        StringBuilder builder = buildFullInfoTitle(
                title,
                titleProperties.get(0), // type
                titleProperties.get(1), // episodes
                titleProperties.get(2), // next episode date
                titleProperties.get(3), // duration
                titleProperties.get(4)  // img

        );
        replyMessage.setText(String.valueOf(builder));
        return replyMessage;
    }

    public SendMessage showTopAnime(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));

        parser.setTitles(parser.getTopAnimeFromString(parser.getTopAnimeStringsFromSite("https://shikimori.one/animes")));

        StringBuilder builder = new StringBuilder();
        List<AnimeTitle> titles = parser.getTitles();

        int i = 0;
        for(AnimeTitle title : titles){
            builder.append(++i).append(" ").append(buildTitle(title)).append("\n");
        }
        replyMessage.setText(String.valueOf(builder));
        replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsAnime());
        return replyMessage;
    }

    public SendMessage showGenres(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));

        parserGenre.setGenres(parserGenre.parseStringsToGenres(parserGenre.getGenresStringsFromSite("https://shikimori.one/animes/menu")));

        StringBuilder builder = new StringBuilder();
        List<Genre> genres = parserGenre.getGenres();

        int i = 0;
        for (Genre genre : genres){
            builder.append(++i).append(" ").append(buildGenre(genre)).append("\n");
        }

        replyMessage.setText(String.valueOf(builder));
        replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsGenres());
        return replyMessage;
    }

    private StringBuilder buildGenre(Genre genre){
        StringBuilder builder = new StringBuilder();
        return builder.append(genre.getGenre());
    }

    public SendMessage getTitlesByGenre(Message message, long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));

        String url = "https://shikimori.one/animes/genre/";

        Genre genre;
        try{
            genre = parserGenre.getGenres().get(Integer.parseInt(message.getText()) - 1);
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            replyMessage.setText("Неверный id");
            return replyMessage;
        }
        url += genre.getLink();

        parser.setTitles(parser.getTopAnimeFromString(parser.getTopAnimeStringsFromSite(url)));

        StringBuilder builder = new StringBuilder();
        List<AnimeTitle> titles = parser.getTitles();

        int i = 0;
        builder.append(genre.getGenre()).append("\n");
        for(AnimeTitle title : titles){
            builder.append(++i).append(" ").append(buildTitle(title)).append("\n");
        }

        replyMessage.setText(String.valueOf(builder));
        replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsAnime());
        return replyMessage;
    }
}
