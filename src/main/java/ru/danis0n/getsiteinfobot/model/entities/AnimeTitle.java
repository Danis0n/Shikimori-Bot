package ru.danis0n.getsiteinfobot.model.entities;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimeTitle {

    String name;
    String url;
    String studio;
    String imgUrl;

    public AnimeTitle(){
    }

    public AnimeTitle(String name, String studio, String url, String imgUrl){
        this.name = name;
        this.studio = studio;
        this.url = url;
        this.imgUrl = imgUrl;
    }
}