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
            result.forEach(m -> System.out.println(m.getTeam().getName()));


            // jpql 에서의 fetch join 사용 방법
            // 한번의 member 와 team 모두 한방 쿼리로 조회한다! 위의 n+1 문제 해결.
            String fetchQuery = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(fetchQuery, Member.class).getResultList();
            result2.forEach(m -> System.out.println(m.getTeam().getName()));


            // collection 을 fetch join 하는 방법.
            // 이렇게 할 경우 one to many 이기 때문에 중복으로 데이터를 가져오게 된다.
            String collectionFetchQuery = "select t from Team t join fetch t.members";
            List<Team> result3 = em.createQuery(collectionFetchQuery, Team.class).getResultList();

            for (Team team1 : result3) {
                System.out.println("team : " + team1.getName() + ", member size: " + team1.getMembers().size());
            }

            // jpql 의 DISTINCT
            // 1. SQL의 DISTINCT 추가, 2. 애플리케이션의 엔티티 중복 제거
            String distinctQuery = "select distinct t from Team t join fetch t.members";
            List<Team> result4 = em.createQuery(distinctQuery, Team.class).getResultList();

            for (Team team1 : result4) {
                System.out.println("team : " + team1.getName() + ", member size: " + team1.getMembers().size());
            }

            /*
            fetch join 의 특징과 한계
            1. fetch join 대상에는 별칭을 줄 수 없다. -> 별칭으로 where 문에 조건을 줄 수 없다.
            2. 둘 이상컬렉션은 페치조인할 수 없다.
            3. 컬렉션을 페치조인하면 페이징API를 사용할 수 없다. (일대일, 다대일은 가능, 하이버네이트는 메모리에서 구현되어 위험)

             */


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
