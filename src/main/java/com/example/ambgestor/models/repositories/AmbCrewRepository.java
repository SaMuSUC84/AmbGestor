package com.example.ambgestor.models.repositories;

import com.example.ambgestor.models.entities.AmbCrewModel;

import java.util.List;

public interface AmbCrewRepository {
    AmbCrewModel findCrewById(int id);
    List<AmbCrewModel> getAllCrews();
    AmbCrewModel saveCrew(AmbCrewModel ambCrewModel);
    AmbCrewModel updateCrew(AmbCrewModel ambCrewModel);
    void deleteCrew(int id);
}
