package com.dellife.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private int age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
