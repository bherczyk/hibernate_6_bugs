package com.example.hibernate_6_bugs.model;


import javax.persistence.*;

@Entity
public class Factory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CarFactory carFactory;

    public Factory() {
    }

    public Factory(String name, CarFactory carFactory) {
        this.name = name;
        this.carFactory = carFactory;
    }

    public CarFactory getCarFactory() {
        return carFactory;
    }
}
