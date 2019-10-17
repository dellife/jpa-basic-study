package com.dellife.jpa.jpql;


import com.dellife.jpa.entity.Member;
import com.dellife.jpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class fetchJoinMain {

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
            member3.setUserName("name2");
            member3.setAge(12);
            member3.setTeam(team2);

            em.persist(member3);

            em.flush();
            em.clear();

            String nQuery = "select m from Member m";
            List<Member> result = em.createQuery(nQuery, Member.class).getResultList();

            // 위 쿼리 수행 후 아래 team의 이름을 roop 돌면서 찍으면 lazy loading 이기 때문에 loop 도는 만큼 team 조회 쿼리가 나감.
            // 단, 같은 team의 경우 프록시로 가져왔기 때문에 아래 수행하면 3번 돌지만, team 조회쿼리 2번 나갈 것임. -> 100번이라면 100번 db 조회하는 문제 생김.
            result.forEach( m -> System.out.println(m.getTeam().getName()) );


            // jpql 에서의 fetch join 사용 방법
            // 한번의 member 와 team 모두 한방 쿼리로 조회한다! 위의 n+1 문제 해결.
            String fetchQuery = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(fetchQuery, Member.class).getResultList();
            result2.forEach( m -> System.out.println(m.getTeam().getName()) );


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
