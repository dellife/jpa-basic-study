package com.dellife.jpa.jpql;


import com.dellife.jpa.entity.Member;
import com.dellife.jpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class jpqlMain {

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

            // select
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            System.out.println("name : " + result.get(0).getUserName());


            String joinQuery = "select m from Member m inner join m.team t";
            List<Member> result2 = em.createQuery(joinQuery, Member.class)
                    .getResultList();


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
