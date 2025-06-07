package com.example.ambgestor.models.daos;
import com.example.ambgestor.models.entities.AmbUnitModel;

import com.example.ambgestor.models.repositories.AmbUnitRepository;
import com.example.ambgestor.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;
import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public class AmbUnitDAO implements AmbUnitRepository {

    private static final Logger logger = Logger.getLogger(AmbUserDAO.class);
    private Session _objSession;
    private Transaction _objTx;

    // Método obtener por ID
    @Override
    public AmbUnitModel findUnitByCode(int id) {

        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            logger.info("Unidad con indicativo: { " + id + "} obtenido con éxito.");
            return _objSession.get(AmbUnitModel.class, id);

        } catch (HibernateException e) {
            logger.error("Error al buscar la unidad con indicativo: { " + id + " } " + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método SELECT *
    @Override
    public List<AmbUnitModel> getAllUnits() {

        List<AmbUnitModel> allUnits;
        try{
            _objSession = HibernateUtil.getSessionFactory().openSession();
            allUnits = _objSession.createQuery("from AmbUnitModel ", AmbUnitModel.class).getResultList();
            return allUnits;

        } catch (HibernateException e) {
            logger.error("Error al obtener todas las unidades { "+ e.getMessage() + " }");
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método INSERT
    @Override
    public AmbUnitModel saveUnit(AmbUnitModel saveAmbUnit) {
        try{
            getSessionFactory(saveAmbUnit);
            _objSession.save(saveAmbUnit);
            logger.info(saveAmbUnit.toUnitString() + " guardada con éxito");
            _objTx.commit();
            return saveAmbUnit;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al guardar la "+ saveAmbUnit.toUnitString() +" ," + e.getMessage());
            return null;
        }finally {
            _objSession.close();
        }
    }

    // Método UPDATE
    @Override
    public AmbUnitModel updateUnit(AmbUnitModel updateAmbUnit) {
        try{
            getSessionFactory(updateAmbUnit);
            // Usamos merge en vez de update porque fusiona los cambios, y en este caso
            // podemos actualizar una unidad que aún no existe en la BBDD.
            _objSession.merge(updateAmbUnit);
            logger.info(updateAmbUnit.toUnitString() + ", actualizada con éxito");
            _objTx.commit();
            return updateAmbUnit;

        }catch (HibernateException e){
            _objTx.rollback();
            logger.error("Error al actualizar la "+ updateAmbUnit.toUnitString() +" ," + e.getMessage());
            return null;

        }finally {
            _objSession.close();
        }
    }

    // Método DELETE
    @Override
    public void deleteUnit(int id) {
        try {
            _objSession = HibernateUtil.getSessionFactory().openSession();
            _objTx = _objSession.beginTransaction();
            _objSession.delete(findUnitByCode(id));
            logger.info(findUnitByCode(id).toUnitString() + ", eliminada con éxito");
            _objTx.commit();
        } catch (HibernateException e) {
            _objTx.rollback();
            logger.error("Error al eliminar la unidad con indicativo: { "+ id +" } " + e.getMessage());
        }finally {
            _objSession.close();
        }

    }

    // Método privado para obtener el Session Factory y manejar las profesiones
    private void getSessionFactory(AmbUnitModel unit) {

        _objSession = HibernateUtil.getSessionFactory().openSession();
        _objTx = _objSession.beginTransaction();

        AmbUnitModel objUnit = new AmbUnitModel();
        objUnit.setUnitName(unit.getUnitName());
        objUnit.setUnitCode(unit.getUnitCode());

    }
}
