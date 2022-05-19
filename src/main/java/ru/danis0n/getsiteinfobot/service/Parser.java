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

    private Document getPage(String url) {
        try {
            return Jsoup.parse(new URL(url), 3000);
        } catch (IOException e) {
            System.out.println("Не удалось подключиться!");
        }
        return null;
    }

    private String getImgSrc(String url){
        Document page = getPage(url);

        assert page != null;
        Element img = page.select("img[src]").first();

        assert img != null;
        String imgStr = img.toString();

        String srcTmp = imgStr.substring(imgStr.indexOf("src=") + 5);
        return srcTmp.substring(0,srcTmp.indexOf("\""));
    }


    public List<String> parseTitle(String url) {
        Document page = getPage(url);
        if (page == null) {
            return null;
        }

        Elements elements = page.select("div[class=value]");
        Element src = page.select("img[src]").first();

        List<String> values = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            values.add(elements.get(i).text());
        }
        assert src != null;
        values.add(getImgSrc(url));

        return values;
    }

    // returns all popular ongoings from main page of shikimori
    public List<String> getOngoingsFromSite(String url) {
        Document page = getPage(url);

        // path to ongoings
        assert page != null;
        Element sitePage = page.select("div[class=block2]").first();

        assert sitePage != null;
        // path to new anime
        Elements anime = sitePage.select("a[class]");

        List<String> fullPaths = new ArrayList<>();
        for (Element el : anime) {
            fullPaths.add(el.toString());
        }
        return fullPaths;
    }

    public List<String> getTopAnimeStringsFromSite(String url) {
        Document page = getPage(url);

        // path to anime
        assert page != null;
        Elements titles = page.select("a[data-delay=150]");

        List<String> animePath = new ArrayList<>();

        for (Element el : titles) {
            animePath.add(el.toString());
        }

        return animePath;
    }

    // converts titleStrings to object AnimeTitles
    public List<AnimeTitle> getTopAnimeFromString(List<String> strings) {
        List<AnimeTitle> titles = new ArrayList<>();

        for (String el : strings) {
            titles.add(parseTitleTopFromString(el));
        }
        return titles;
    }

    // converts titleString to object AnimeTitle
    public AnimeTitle parseTitleTopFromString(String titleString) {
        if (titleString == null) {
            return new AnimeTitle("Unknown", "Unknown", "Unknown","Unknown");
        }
            String tmUrl = titleString.substring(titleString.indexOf("href=") + 6);
            String url = tmUrl.substring(0, tmUrl.indexOf("\""));

            String tmpName = titleString.substring(titleString.indexOf("title=") + 7);
            String name = tmpName.substring(0, tmpName.indexOf("\""));

            return new AnimeTitle(name, "inProcess", url,null);
    }

}
