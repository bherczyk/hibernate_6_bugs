package com.example.hibernate_6_bugs.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Feature> features = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CarFactory carFactory;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Info info;

    public Car() {
    }

    public Car(String name, Info info) {
        this.name = name;
        this.info = info;
    }

    public CarFactory getCarFactory() {
        return carFactory;
    }

    public void setCarFactory(CarFactory carFactory) {
        this.carFactory = carFactory;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
        feature.setCar(this);
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public Info getInfo() {
        return info;
    }
}
