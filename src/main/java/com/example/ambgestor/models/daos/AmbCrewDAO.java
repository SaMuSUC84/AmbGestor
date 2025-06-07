package com.example.ambgestor.models.daos;

import com.example.ambgestor.models.entities.AmbCrewModel;
import com.example.ambgestor.models.repositories.AmbCrewRepository;
import com.example.ambgestor.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public class AmbCrewDAO  implements AmbCrewRepository {

    private static final Logger logger = Logger.getLogger(AmbUserDAO.class);
    private Session _objSession;
    private Transaction _objTx;

    // Método obtner por ID
    @Override
    public AmbCrewModel findCrewById(int id) {

        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            logger.info("Dotación con ID: { " + id + " } obtenido con éxito.");
            return _objSession.get(AmbCrewModel.class, id);

        } catch (HibernateException e) {
            logger.error("Error al obtener la profesion con id: { "+ id +" } " + e.getMessage());
            return null;

        }finally {
            _objSession.close();
        }
    }

    // Método SELECT *
    @Override
    public List<AmbCrewModel> getAllCrews() {
        List<AmbCrewModel> allCrews;
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            allCrews = _objSession.createQuery("from AmbCrewModel", AmbCrewModel.class).getResultList();
            return allCrews;

        } catch (HibernateException e) {
            logger.info("Error al cargar todas las dotaciones { "+e.getMessage()+" }");
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método INSERT
    @Override
    public AmbCrewModel saveCrew(AmbCrewModel saveCrew) {
        try{
            getSessionFactory(saveCrew);
            _objSession.save(saveCrew);
            logger.info(saveCrew.toCrewString() + " guardada con éxito");
            _objTx.commit();
            return saveCrew;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al guardar la "+ saveCrew.toCrewString() +" ," + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método UPDATE
    @Override
    public AmbCrewModel updateCrew(AmbCrewModel updateCrew) {
        try{
            getSessionFactory(updateCrew);
            _objSession.update(updateCrew);
            logger.info(updateCrew.toCrewString() + " actualizada con éxito");
            _objTx.commit();
            return updateCrew;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al actualizar la "+ updateCrew.toCrewString()  + " ," + e.getMessage());
            return null;

        }finally {
            _objSession.close();
        }
    }

    // Método DELETE
    @Override
    public void deleteCrew(int id) {
        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();
           _objTx =  _objSession.beginTransaction();
            _objSession.delete(findCrewById(id));
            logger.info(findCrewById(id).toCrewString() + "eliminada con éxito");
            _objTx.commit();
        } catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al eliminar la dotación con ID: { "+ id +" } " + e.getMessage());
        }finally {
            _objSession.close();
        }
    }

    // Método para la busqueda activa de Dotaciones
    public List<AmbCrewModel> searchCrews(String keyWord){
        List<AmbCrewModel> allCrews;

        try{
            String pattern = "%" + keyWord + "%";

            _objSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM AmbCrewModel a WHERE "
                    + "a.conductdotaDa.usernameUa LIKE :pattern OR "
                    + "a.sanitdotaDa.usernameUa LIKE :pattern OR "
                    + "a.facultdotaDa.usernameUa LIKE :pattern OR "
                    + "a.unitdotaDa.unitnameUta LIKE :pattern";

            Query<AmbCrewModel> query = _objSession.createQuery(hql, AmbCrewModel.class);

            query.setParameter("pattern",pattern);
            allCrews = query.getResultList();
            return allCrews;

        } catch (HibernateException e) {
            logger.info("Error al realizar la busqueda en las dotaciones { "+e.getMessage()+" }");
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método para buscar solo por unidad y evitar el problema de conversión entre int y String.
    public List<AmbCrewModel> searchByUnitCode(int unitCode) {
        List<AmbCrewModel> result;

        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "FROM AmbCrewModel a WHERE CONCAT(a.unitdotaDa.unitcodeUta, '')LIKE :unitCode";
            Query<AmbCrewModel> query = _objSession.createQuery(hql, AmbCrewModel.class);
            query.setParameter("unitCode", "%"+unitCode+"%");

            result = query.getResultList();

            return result;

        } catch (HibernateException e) {
            logger.error("Error en la búsqueda en la dotación por unidad: " + e.getMessage());
            return null;
        } finally {
            if (_objSession != null) {
                _objSession.close();
            }
        }
    }

    // Método para buscar por nombre de recurso
    public List<AmbCrewModel> searchByUnitName(String keyWord) {
        List<AmbCrewModel> allCrews;

        try{
            String pattern = "%" + keyWord + "%";

            _objSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM AmbCrewModel a WHERE a.unitdotaDa.unitnameUta LIKE :pattern";

            Query<AmbCrewModel> query = _objSession.createQuery(hql, AmbCrewModel.class);

            query.setParameter("pattern",pattern);
            allCrews = query.getResultList();
            return allCrews;

        } catch (HibernateException e) {
            logger.info("Error al realizar la busqueda en las dotaciones por el nombre del recurso { "+e.getMessage()+" }");
            return null;
        }finally {
            _objSession.close();
        }

    }


    // Método privado para obtener el Session Factory y manejar las dotaciones
    private void getSessionFactory(AmbCrewModel ambCrew) {

        _objSession = HibernateUtil.getSessionFactory().openSession();
        _objTx = _objSession.beginTransaction();

        AmbCrewModel objCrew = new AmbCrewModel();

        objCrew.setId(ambCrew.getId());
        objCrew.setConductDota(ambCrew.getConductDota());
        objCrew.setSanitDota(ambCrew.getSanitDota());
        objCrew.setDoctorDota(ambCrew.getDoctorDota());
        objCrew.setUnitDota(ambCrew.getUnitDota());
    }
}
