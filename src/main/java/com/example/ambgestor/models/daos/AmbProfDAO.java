package com.example.ambgestor.models.daos;

import com.example.ambgestor.models.entities.AmbProfModel;
import com.example.ambgestor.models.entities.AmbUserModel;
import com.example.ambgestor.models.repositories.AmbProfRepository;
import com.example.ambgestor.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

public class AmbProfDAO implements AmbProfRepository {

    private static final Logger logger = Logger.getLogger(AmbUserDAO.class);
    private Session _objSession;
    private Transaction _objTx;

    // Método SELECT *
    @Override
    public List<AmbProfModel> getAllProfs() {

        List<AmbProfModel> allProfesion;
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            allProfesion = _objSession.createQuery("from AmbProfModel", AmbProfModel.class).getResultList();
            return allProfesion;

        } catch (HibernateException e) {
            logger.info("Error al cargar todas las profesiones { "+e.getMessage()+" }");
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método para obtener por el código
    @Override
    public AmbProfModel findProfByCode(String code) {
        logger.info("Obteniendo profesión...");
        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();

            Query<AmbProfModel> query = _objSession.createQuery(
                    "FROM AmbProfModel WHERE profcodePa = :code", AmbProfModel.class);
            query.setParameter("code", code);

            AmbProfModel objProf = query.uniqueResult();

            if (objProf != null) {
                logger.info("Profesión con código: { " + code + " } obtenido con éxito.");
                return objProf;
            } else {
                logger.warn("⚠️ No se encontró profesión con código: { " + code+" }");
                return null;
            }

        } catch (HibernateException e) {
            logger.error("Error al buscar la profesión con código: { "+ code +" } " + e.getMessage());
            return null;
        } finally {
            _objSession.close();
        }
    }

    // Método INSERT
    @Override
    public AmbProfModel saveProf(AmbProfModel saveProf) {
        try{
            getSessionFactory(saveProf);
            _objSession.save(saveProf);
            logger.info(saveProf.toProfString() + " guardada con éxito");
            _objTx.commit();
            return saveProf;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al guardar la "+ saveProf.toProfString() +" ," + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método obtener por ID
    @Override
    public AmbProfModel findProfById(int id) {

        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            logger.info("Profesión con ID: { " + id + " } obtenido con éxito.");
            return _objSession.get(AmbProfModel.class, id);

        } catch (HibernateException e) {
            logger.error("Error al obtener la profesion con id: { "+ id +" } " + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método UPDATE
    @Override
    public AmbProfModel updateProf(AmbProfModel updateProfession) {

        try{
            getSessionFactory(updateProfession);
            _objSession.update(updateProfession);
            logger.info( updateProfession.toProfString() + " actualizada con éxito");
            _objTx.commit();
            return updateProfession;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al actualizar la profesion con ID: { "+ updateProfession.getId()  +" } " + e.getMessage());
            return null;

        }finally {
            _objSession.close();
        }
    }

    // Método DELETE
    @Override
    public void deleteProf(int id) {

        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();
            _objTx = _objSession.beginTransaction();
            _objSession.delete(findProfById(id));
            logger.info(findProfById(id).toProfString() + " eliminada con éxito");
           _objTx.commit();
        } catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al eliminar la profesion con ID: { "+ id +" } " + e.getMessage());
        }finally {
            _objSession.close();
        }
    }

    // Método privado para obtener el Session Factory y manejar las profesiones
    private void getSessionFactory(AmbProfModel ambProfession) {

        _objSession = HibernateUtil.getSessionFactory().openSession();
        _objTx = _objSession.beginTransaction();

        AmbProfModel objProfession = new AmbProfModel();

        objProfession.setId(ambProfession.getId());
        objProfession.setProfCode(ambProfession.getProfCode());
        objProfession.setProfName(ambProfession.getProfName());
    }
}
