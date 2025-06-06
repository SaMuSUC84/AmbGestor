package com.example.ambgestor.models.repositories;

import com.example.ambgestor.models.entities.AmbUnitModel;

import java.util.List;

public interface AmbUnitRepository {

    AmbUnitModel saveUnit(AmbUnitModel ambUnitModel);
    AmbUnitModel findUnitByCode(int id);
    List<AmbUnitModel> getAllUnits();
    void deleteUnit(int id);
    AmbUnitModel updateUnit(AmbUnitModel ambUnitModel);

}
