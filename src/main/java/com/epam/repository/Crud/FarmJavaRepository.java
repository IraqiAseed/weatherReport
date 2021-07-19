package com.epam.repository.Crud;

import com.epam.model.Crud.FarmJava;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmJavaRepository extends CrudRepository<FarmJava,Integer> {
}
