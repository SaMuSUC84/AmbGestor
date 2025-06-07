package com.example.ambgestor.models.repositories;

import com.example.ambgestor.models.entities.AmbProfModel;

import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public interface AmbProfRepository {
    AmbProfModel saveProf(AmbProfModel ambProf);
    AmbProfModel findProfById(int id);
    AmbProfModel updateProf(AmbProfModel ambProfession);
    void deleteProf(int id);
    List <AmbProfModel> getAllProfs();
    AmbProfModel findProfByCode(String code);

}