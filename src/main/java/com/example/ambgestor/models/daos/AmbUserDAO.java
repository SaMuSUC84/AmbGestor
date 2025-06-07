package com.example.ambgestor.models.daos;


import com.example.ambgestor.models.entities.AmbUserModel;
import com.example.ambgestor.models.repositories.AmbUserRepository;
import com.example.ambgestor.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public class AmbUserDAO implements AmbUserRepository {

    private static final Logger logger = Logger.getLogger(AmbUserDAO.class);
    private  Session _objSession;
    private  Transaction _objTx;

    // Método SELECT *
    @Override
    public List<AmbUserModel> getAllUsers() {

        List<AmbUserModel> allUsers = new ArrayList<>();
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            logger.info("Obteniendo todos los usuarios de la base de datos...");
            allUsers = _objSession.createQuery("from AmbUserModel", AmbUserModel.class).getResultList();
            logger.debug("Total de usuarios de la base de datos : " + allUsers.size());

        } catch (HibernateException e) {
            logger.error("Error al obtener todos los usuarios { "+ e.getMessage()+" }");
        }finally {
            _objSession.close();
        }
        return allUsers;
    }


    // Método buscar por email
    @Override
    public AmbUserModel findUserByEmail(String email) {
        logger.info("Obteniendo usuario...");
        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();

            Query<AmbUserModel> query = _objSession.createQuery(
                    "FROM AmbUserModel WHERE useremailUa = :email", AmbUserModel.class);
            query.setParameter("email", email);

            AmbUserModel objUser = query.uniqueResult();

            if (objUser != null) {
                logger.info("Usuario con email: { " + email + " } obtenido con éxito.");
                return objUser;
            } else {
                logger.warn("⚠️ No se encontró usuario con email: { " + email+" }");
                return null;
            }

        } catch (HibernateException e) {
            logger.error("Error al buscar el usuario con email: { "+ email +" } " + e.getMessage());
            return null;
        } finally {
            _objSession.close();
        }

    }

    // Método INSERT
    @Override
    public AmbUserModel saveUser(AmbUserModel userapp) {

        logger.info("Creando un usuario....");
        try {
            getSessionFactory(userapp);
            _objSession.save(userapp);
            logger.info(userapp.toUserString() + " guardado con éxito");
            _objTx.commit();
            return userapp;

        }catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al guardar "+ userapp.toUserString() +" ," + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método obtener por id
    @Override
    public AmbUserModel findUserById(int id) {

        logger.info("Obteniendo usuario...");
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            logger.info("Usuario con ID: { " + id + " } obtenido con éxito.");
            return _objSession.get(AmbUserModel.class, id);

        } catch (HibernateException e) {
            logger.error("Error al buscar el usuario con ID: { "+ id +" } " + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método UPDATE
    @Override
    public AmbUserModel updateUser(AmbUserModel updateUser) {

        logger.info("Actualizando usuario...");
        try{
            getSessionFactory(updateUser);
            _objSession.update(updateUser);
            logger.info(updateUser.toUserString()+ ", actualizado con éxito.");
            _objTx.commit();
            return updateUser;

        }catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al actualizar el "+ updateUser.toUserString() +" ," + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }    }

    // Método DELETE
    @Override
    public void deleteUser(int id) {
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            _objTx = _objSession.beginTransaction();
            _objSession.delete(findUserById(id));
            logger.info(findUserById(id).toUserString() + ", eliminado con éxito");
            _objTx.commit();

        } catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al eliminar el usuario con ID: { "+ id +" } " + e.getMessage());

        }finally {
            _objSession.close();
        }    }

    // Método privado para obtener el Session Factory y manejar el usuario
    private void getSessionFactory(AmbUserModel userapp) {

        this._objSession = HibernateUtil.getSessionFactory().openSession();
        this._objTx = _objSession.beginTransaction();

        AmbUserModel objUser = new AmbUserModel();

        objUser.setId(userapp.getId());
        objUser.setName(userapp.getName());
        objUser.setSurName(userapp.getSurName());
        objUser.setEmail(userapp.getEmail());
        objUser.setPassword(userapp.getPassword());
        objUser.setPhone(userapp.getPhone());
        objUser.setProfession(userapp.getProfession());
    }

}
