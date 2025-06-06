import com.example.ambgestor.models.daos.AmbProfDAO;
import com.example.ambgestor.models.daos.AmbUserDAO;
import com.example.ambgestor.models.entities.AmbProfModel;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAmbProfDAO {


    private AmbProfDAO _objAmbProfDAO;

    @BeforeAll
    public void setUp() throws Exception {
        _objAmbProfDAO = new AmbProfDAO();

        System.out.printf("Session abierta con exito\n");
    }

    @Test
    @Order(1)
    @DisplayName("TEST Guardar profesion Test")
    public void testGuardarProfesion() {

        AmbProfModel objProfTest = new AmbProfModel();

        objProfTest.setProfName("Test Junit5");
        objProfTest.setProfCode("TEST");

        AmbProfModel existingProf = _objAmbProfDAO.findProfByCode(objProfTest.getProfCode());
        if (existingProf != null) {
            _objAmbProfDAO.deleteProf(existingProf.getId());
            System.out.printf("⚠️ Profesión Test eliminado antes de la prueba.\n");
        }

        _objAmbProfDAO.saveProf(objProfTest);

        AmbProfModel objSaveProf = _objAmbProfDAO.findProfById(objProfTest.getId());
        assertEquals("TEST", objSaveProf.getProfCode());

        System.out.printf("Profesón guardada con exito en Junit5\n", objSaveProf.toProfString());
    }

    @Test
    @Order(2)
    @DisplayName("TEST Mostrar profesion Test")
    public void testMostrarProfesion() {

        AmbProfModel objProfTest = _objAmbProfDAO.findProfByCode("TEST");

        System.out.printf("Profesión Test obtenida desde Junit5: %s\n", objProfTest.toProfString());
        assertEquals("TEST", objProfTest.getProfCode());
    }

    @Test
    @Order(3)
    @DisplayName("TEST Eliminar profesion Test")
    public void testEliminarProfesion() {

        AmbProfModel objProfTest = _objAmbProfDAO.findProfByCode("TEST");

        assertNotNull(objProfTest.toProfString(), "La profesion no puede ser null antes de eliminarla");

        _objAmbProfDAO.deleteProf(objProfTest.getId());

        // Esperar un momento para que la eliminación se refleje en la base de datos
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        AmbProfModel deleteProf = _objAmbProfDAO.findProfByCode("TEST");

        assertNull(deleteProf, "La profesion debería haber sido eliminada correctamente");

        System.out.printf("🗑️ Profesión eliminado con éxito desde Junit5\n.");
    }
}