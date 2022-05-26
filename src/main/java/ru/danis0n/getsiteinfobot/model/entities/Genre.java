package ru.danis0n.getsiteinfobot.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {

    Long id;
    String genre;
    String link;

    public Genre(){
    }

    public Genre(String genre, String link){
        this.genre = genre;
        this.link = link;
    }
}
