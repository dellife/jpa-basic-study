package com.dellife.jpa.dialect;


import com.dellife.jpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class jpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member();
            member.setUserName("name1");

            Member member2 = new Member();
            member2.setUserName("name2");

            em.persist(member);
            em.persist(member2);
            em.flush();
            em.clear();

            String query = "select function('group_concat', m.userName) from Member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            System.out.println(result.get(0));


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
