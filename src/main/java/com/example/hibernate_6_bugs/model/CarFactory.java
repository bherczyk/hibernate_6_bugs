package com.example.hibernate_6_bugs.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class CarFactory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "carFactory", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Car> cars = new HashSet<>();

    public CarFactory() {
    }

    public CarFactory(String name) {
        this.name = name;
    }

    public void addCar(Car car) {
        cars.add(car);
        car.setCarFactory(this);
    }

    public Set<Car> getCars() {
        return cars;
    }
}
