package ru.danis0n.getsiteinfobot.service;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.danis0n.getsiteinfobot.model.entities.Genre;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Setter
@Getter
public class ParserGenre {

    private List<Genre> genres;
    private Parser parser;

    protected Document getPage(String url) {
        try {
            return Jsoup.parse(new URL(url), 3000);
        } catch (IOException e) {
            System.out.println("�� ������� ������������!");
        }
        return null;
    }

    public List<Genre> parseStringsToGenres(List<String> strings){
        List<Genre> genres = new ArrayList<>();

        for(String el : strings){
            genres.add(parseStringToGenre(el));
        }
        return genres;
    }

    public Genre parseStringToGenre(String string){
        Genre genre = new Genre();

        String linkTmp = string.substring(string.indexOf("data-value=") + 12);
        String link = linkTmp.substring(0,linkTmp.indexOf("\""));

        String nameTmp = string.substring(string.indexOf("genre-ru") + 10);
        String name = nameTmp.substring(0,nameTmp.indexOf("<"));

        String id = link.substring(0,link.indexOf("-"));

        genre.setId(Long.valueOf(id));
        genre.setLink(link);
        genre.setGenre(name);


        return genre;
    }

    public List<String> getGenresStringsFromSite(String url){
        if(url == null){
            return null;
        }

        Document page = getPage(url);
        List<String> strings = new ArrayList<>();

        Elements genres = page.select("li[data-field=genre]");


        for(Element el : genres){
            strings.add(el.toString());
        }

        return strings;
    }

}
