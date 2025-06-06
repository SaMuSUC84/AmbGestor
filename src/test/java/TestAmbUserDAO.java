import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ambgestor.models.entities.AmbUserModel;
import com.example.ambgestor.models.entities.AmbProfModel;
import com.example.ambgestor.models.daos.AmbUserDAO;
import com.example.ambgestor.models.daos.AmbProfDAO;


/*
 * @author Samuel Alonso Viera
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAmbUserDAO {

    private AmbUserDAO _objAmbUserDAO;
    private AmbProfDAO _objAmbProfDAO;



    @BeforeAll
    public void setUp() throws Exception {
        _objAmbUserDAO = new AmbUserDAO();
        _objAmbProfDAO = new AmbProfDAO();

        System.out.printf("Session abierta con exito\n");
    }

    @Test
    @Order(1)
    @DisplayName("TEST Guardar usuario Test")
    public void testGuardarUsuario() {
        AmbUserModel objUserTest = new AmbUserModel();
        AmbProfModel objProfTest = _objAmbProfDAO.findProfById(1) ;

        objUserTest.setName("Test");
        objUserTest.setSurName("TestJUnit");
        objUserTest.setPassword("1234Test");
        objUserTest.setEmail("test@test.com");
        objUserTest.setPhone(123456789);
        objUserTest.setProfession(objProfTest);

        AmbUserModel existingUser = _objAmbUserDAO.findUserByEmail(objUserTest.getEmail());
        if (existingUser != null) {
            _objAmbUserDAO.deleteUser(existingUser.getId());
            System.out.println("⚠️ Usuario Test eliminado antes de la prueba.");
        }

        _objAmbUserDAO.saveUser(objUserTest);

        AmbUserModel objSaveUserTest = _objAmbUserDAO.findUserById(objUserTest.getId());
        assertEquals("Test", objSaveUserTest.getName());

        System.out.printf("Usuario guardado con exito en JUnit5\n" , objSaveUserTest.toUserString());
    }

    @Test
    @Order(2)
    @DisplayName("TEST Mostrar Usuario Test")
    public void testMostrarUsuario() {
        AmbUserModel objUserTest = _objAmbUserDAO.findUserByEmail("test@test.com");
        System.out.printf("Usuario Test obtenido de Junit5: %s\n", objUserTest.toUserString());
        assertEquals("Test", objUserTest.getName());
    }

    @Test
    @Order(3)
    @DisplayName("TEST Eliminar Usuario Test")
    public void testEliminarUsuario() {

        AmbUserModel objDeleteUserTest = _objAmbUserDAO.findUserByEmail("test@test.com");

        assertNotNull(objDeleteUserTest.toUserString(), "El usuario no debería ser null antes de eliminarlo");

        _objAmbUserDAO.deleteUser(objDeleteUserTest.getId());


        // Esperar un momento para que la eliminación se refleje en la base de datos
        try {
            Thread.sleep(500); // Espera 500ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AmbUserModel deletedUSer = _objAmbUserDAO.findUserByEmail("test@test.com");
        assertNull(deletedUSer, "El usuario debería haber sido eliminado correctamente");

        System.out.println("🗑️ Usuario eliminado con éxito desde Junit5\n.");

    }


}