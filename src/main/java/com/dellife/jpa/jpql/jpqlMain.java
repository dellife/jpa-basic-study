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


            String caseQuery = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                    "      when m.age > 60 then '경로요금' " +
                    "      else '일반요금' end " +
                    " from Member m ";
            List<String> result3 = em.createQuery(caseQuery, String.class)
                    .getResultList();
            System.out.println(result3.get(0));

            String coalesceQuery = "select coalesce(m.userName, '이름없는회원') from Member m"; //사용자 이름이 없으면 이름없는 회원 반환
            em.createQuery(coalesceQuery);

            //사용자 이름이 관리자면 null을 반환/나머지는 본인의 이름을 반환 -> 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
            String nullIfQuery = "select NULLIF(m.userName, '관리자') from Member m";
            em.createQuery(nullIfQuery);

            String impliedQuery = "select t.members from Team t";
            em.createQuery(impliedQuery);

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
