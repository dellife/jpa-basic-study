package com.dellife.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Team {

    @Id @GeneratedValue
    private Long id;
    private String name;

//    @BatchSize(size = 100)  //collection fetch join 안되는 문제 해결 방법 중 하나.
    @OneToMany(mappedBy = "team")
    private List<Member> members;
}
