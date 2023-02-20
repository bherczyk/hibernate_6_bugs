package com.example.hibernate_6_bugs.repository;

import com.example.hibernate_6_bugs.model.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface Repository extends CrudRepository<Company, Long> {

    @Query("select c from Company c where c.id = :id")
    @EntityGraph(attributePaths = {"factory", "factory.carFactory", "factory.carFactory.cars",
            "factory.carFactory.cars.features","factory.carFactory.cars.info"
    })
    Optional<Company> findByIdCustomEntityGraph(long id);

    @Query("select c from Company c join fetch c.factory f join fetch f.carFactory cf where c.id = :id")
    Optional<Company> findByIdExplicitFetchFactoryAndCarFactory(long id);

    @Query("select c from Company c  where c in :company")
    @EntityGraph(attributePaths = {"factory.carFactory.cars"})
    Optional<Company> findByCompanyFetchCars(Company company);
}
