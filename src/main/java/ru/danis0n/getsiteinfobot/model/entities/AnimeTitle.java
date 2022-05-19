package ru.danis0n.getsiteinfobot.model.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimeTitle {

    private String name;

    private String url;

    private String studio;

    private String imgUrl;

    public AnimeTitle(){
    }

    public AnimeTitle(String name, String studio, String url, String imgUrl){
        this.name = name;
        this.studio = studio;
        this.url = url;
        this.imgUrl = imgUrl;
    }

}