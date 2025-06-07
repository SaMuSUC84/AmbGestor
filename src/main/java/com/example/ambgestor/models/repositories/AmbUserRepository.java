package com.example.ambgestor.models.repositories;


import com.example.ambgestor.models.entities.AmbUserModel;

import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public interface AmbUserRepository {

    AmbUserModel saveUser (AmbUserModel userapp);
    AmbUserModel findUserById (int id);
    AmbUserModel findUserByEmail (String email);
    AmbUserModel updateUser (AmbUserModel userapp);
    void deleteUser (int id);
    List <AmbUserModel> getAllUsers ();

}
