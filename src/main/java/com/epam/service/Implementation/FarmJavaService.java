package com.epam.service.Implementation;

import com.epam.model.FarmJava;
import com.epam.repository.FarmJavaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FarmJavaService {

    private FarmJavaRepository farmRepo;

    @Autowired
    public FarmJavaService(FarmJavaRepository farmRepo)
    {
        this.farmRepo = farmRepo;
    }

    public FarmJava saveFarm(FarmJava reservation){

        return farmRepo.save(reservation);
    }

    public Iterable<FarmJava> getAllFarms(){
        return farmRepo.findAll();
    }

    public void deleteAllFarms(){
        farmRepo.deleteAll();
    }

    public void deleteFarmById(Integer id){
        farmRepo.deleteById(id);
    }

    public Optional<FarmJava> findFarmById(Integer id){
        return farmRepo.findById(id);
    }
}
