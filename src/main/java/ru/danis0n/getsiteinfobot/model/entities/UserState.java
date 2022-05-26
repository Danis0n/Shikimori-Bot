package ru.danis0n.getsiteinfobot.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users_state")
@AllArgsConstructor
@NoArgsConstructor
public class UserState {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "state")
    private String state;

}
