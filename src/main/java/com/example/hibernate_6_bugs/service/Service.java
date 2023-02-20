package com.example.hibernate_6_bugs.service;

import com.example.hibernate_6_bugs.model.*;
import com.example.hibernate_6_bugs.repository.Repository;
import org.hibernate.jpa.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

@org.springframework.stereotype.Service
public class Service {

    private final Repository repository;
    private final EntityManager entityManager;

    public Service(Repository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public long create() {
        var info = new Info("info");
        var car = new Car("car", info);
        var feature = new Feature();
        car.addFeature(feature);
        var carFactory = new CarFactory("car factory");
        carFactory.addCar(car);
        var factory = new Factory("factory", carFactory);
        feature.setFactory(factory);
        var company = new Company(factory);
        return repository.save(company).getId();
    }

    @Transactional(readOnly = true)
    public Company getCustomEntityGraph(long id) {
        return repository.findByIdCustomEntityGraph(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Company getFetchGraphHint(long id) {
        var companyGraph = entityManager.createEntityGraph(Company.class);
        var factoryGraph = companyGraph.addSubgraph("factory");
        var carFactoryGraph = factoryGraph.addSubgraph("carFactory");
        var carsGraph = carFactoryGraph.addSubgraph("cars");
        carsGraph.addAttributeNodes("features", "info");
        return entityManager.createQuery("select c from Company c where c.id = :id", Company.class)
                .setParameter("id", id)
                .setHint(QueryHints.HINT_FETCHGRAPH, companyGraph)
                .getSingleResult();
    }

    @Transactional(readOnly = true)
    public Company getTwoQueries(long id) {
        return repository.findByIdExplicitFetchFactoryAndCarFactory(id)
                .flatMap(repository::findByCompanyFetchCars)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Company getTwoQueriesWithoutExplicitJoinFetch(long id) {
        return repository.findById(id)
                .flatMap(repository::findByCompanyFetchCars)
                .orElseThrow(EntityNotFoundException::new);
    }
}
