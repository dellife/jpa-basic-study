package com.dellife.jpa.jpql;


import com.dellife.jpa.entity.Member;
import com.dellife.jpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class joinMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team team = new Team();
            team.setName("team1");
            em.persist(team);


            Member member = new Member();
            member.setUserName("name1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 묵시적 조인 -> 이렇게 사용하면 안됨. (INNER JOIN만 가능)
            String impliedQuery = "select m.team from Member m";
            List<Team> impliedResult = em.createQuery(impliedQuery, Team.class).getResultList();

            // 명시적 조인 -> 이렇게 사용
            String explicitQuery = "select m from Member m join m.team t";
            List<Team> explicitResult = em.createQuery(impliedQuery, Team.class).getResultList();

            tx.commit();
        } catch (Exception e) {

            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
