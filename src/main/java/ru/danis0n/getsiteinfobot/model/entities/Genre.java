package ru.danis0n.getsiteinfobot.model.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {

    private Long id;

    private String genre;

    private String link;

    public Genre(){
    }

    public Genre(String genre, String link){
        this.genre = genre;
        this.link = link;
    }

}
