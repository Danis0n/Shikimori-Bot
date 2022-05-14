package ru.danis0n.getsiteinfobot.service;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.danis0n.getsiteinfobot.model.entities.AnimeTitle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class Parser {

    private List<AnimeTitle> titles;

    private Document getPage(String url){
        try {
            return Jsoup.parse(new URL(url),3000);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться!");
        }
        return null;
    }

    public List<String> getOngoingsFromSite(String url){
        Document page = getPage(url);

        // path to ongoings
        assert page != null;
        Element sitePage = page.select("div[class=block2]").first();

        assert sitePage != null;
        // path to new anime
        Elements anime = sitePage.select("a[class]");

        List<String> fullPaths = new ArrayList<>();
        for(Element el : anime){
            fullPaths.add(el.toString());
        }
        return fullPaths;
    }

    public List<AnimeTitle> getOngoings(List<String> ongoings){
        List<AnimeTitle> titles = new ArrayList<>();

        for(String el : ongoings){
            titles.add(parseTitle(el));
        }
        return titles;
    }


    private AnimeTitle parseTitle(String page){
        if(page == null){
            return new AnimeTitle("Unknown","Unknown","Unknown");
        }

        String url = page.substring(page.indexOf("href=") + 6);
        url = url.substring(0,url.indexOf("\""));

        // Name
        String name = page.substring(page.indexOf("title=") + 7);
        name = name.substring(0,name.indexOf("\""));
        return new AnimeTitle(name,"InProcess",url);
    }

}
