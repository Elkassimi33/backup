package com.youssef.etude;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {

        Student s =new Student();
        s.setId(101);
        s.setName("youssef");
        s.setTech("java");

        Configuration config= new Configuration();

        config.addAnnotatedClass(com.youssef.etude.Student.class);

        SessionFactory sf=config.buildSessionFactory();

        Session session =sf.openSession();

        session.persist(s);

    }
}