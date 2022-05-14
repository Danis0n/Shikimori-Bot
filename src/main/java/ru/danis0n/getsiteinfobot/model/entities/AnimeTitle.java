package ru.danis0n.getsiteinfobot.model.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimeTitle {

    private String name;
    private String url;
    private String studio;

    public AnimeTitle(){
    }

    public AnimeTitle(String name, String studio, String url){
        this.name = name;
        this.studio = studio;
        this.url = url;
    }

}