package com.icecream.repository;

import com.icecream.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {

    Optional<Factory> findByName(String name);

    @Override
    List<Factory> findAll();

    boolean existsByName(String name);
}