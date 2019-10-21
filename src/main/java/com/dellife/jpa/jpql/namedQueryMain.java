package com.dellife.jpa.jpql;


import com.dellife.jpa.entity.Member;
import com.dellife.jpa.entity.Team;

import javax.persistence.*;
import java.util.List;

public class namedQueryMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("team2");
            em.persist(team2);


            Member member = new Member();
            member.setUserName("name1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUserName("name2");
            member2.setAge(11);
            member2.setTeam(team);

            em.persist(member2);

            Member member3 = new Member();
            member3.setUserName("name3");
            member3.setAge(12);
            member3.setTeam(team2);

            em.persist(member3);

            em.flush();
            em.clear();

            // Named 쿼리 - 정적쿼리
            // 미리 정의해서 이름을 부여해두고 사용하는 쿼리
            // 어노테이션, XML에 정의
            // 애플리케이 로딩 시점에 쿼리 검증
            // Spring Data Jpa -> @Query -> 스프링이 쿼리를 namedQuery로 등록해서 사용함.
            List<Member> namedQueryResult = em.createNamedQuery("Member.findByUserName", Member.class)
                    .setParameter("userName", "name2")
                    .getResultList();

            namedQueryResult.forEach(m -> System.out.println("name : " + m.getUserName()));


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
