package com.epam.repository;

import com.epam.model.FarmJava;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmJavaRepository extends CrudRepository<FarmJava,Integer> {
}
