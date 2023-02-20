package com.example.hibernate_6_bugs.model;

import jakarta.persistence.*;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Factory factory;

    public Company() {
    }

    public Company(Factory factory) {
        this.factory = factory;
    }

    public Long getId() {
        return id;
    }

    public Factory getFactory() {
        return factory;
    }
}
