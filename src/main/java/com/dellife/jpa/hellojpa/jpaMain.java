package com.dellife.jpa.hellojpa;


import com.dellife.jpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class jpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();

        try {
            et.begin();

            Member member = new Member();
//            member.setUserName("name1");

            em.persist(member);

            em.flush();
            et.commit();
        } catch (Exception e) {

            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
