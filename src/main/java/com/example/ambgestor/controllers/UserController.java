package com.example.ambgestor.controllers;

import com.example.ambgestor.components.Alerts;
import com.example.ambgestor.models.daos.AmbProfDAO;

import com.example.ambgestor.models.daos.AmbUserDAO;
import com.example.ambgestor.models.entities.AmbProfModel;
import com.example.ambgestor.models.entities.AmbUserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @author Samuel Alonso Viera
 */
public class UserController {


    private AmbUserDAO _objUserDAO;
    private AmbProfDAO _objProfDAO;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSurName;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPass;
    @FXML
    private PasswordField txtRepeatPass;
    @FXML
    private TextField txtPhone;
    @FXML
    private Label txtError;
    @FXML
    private ComboBox <AmbProfModel>cmbProfs;
    @FXML
    private ComboBox <AmbUserModel>cmbUsers;

    @FXML
    public void initialize() {
        this._objUserDAO = new AmbUserDAO();
        this._objProfDAO = new AmbProfDAO();

        initUserComboBox();
        initProfComboBox();
    }

    // Método botón Guardar
    @FXML
    public void onSaveUserClick(ActionEvent event) {
        if (validForm()){
            AmbUserModel newUser = new AmbUserModel();
            setSaveOrModifyUser(newUser);

            _objUserDAO.saveUser(newUser);

            new Alerts("El usuario se creó con éxito", Alert.AlertType.INFORMATION);
            closeWindow(event);
        }
    }

    // Método botón Modificar
    @FXML
    public void onModifyUserClick(ActionEvent event) {
        AmbUserModel modifyUser = cmbUsers.getSelectionModel().getSelectedItem();

        if(modifyUser == null){
            new Alerts("No se ha seleccionado un usuario para modificar", Alert.AlertType.WARNING);
            return;
        }

        if (validForm()){
            setSaveOrModifyUser(modifyUser);

            this._objUserDAO.updateUser(modifyUser);

            new Alerts("El usuario se modificó correctamente", Alert.AlertType.INFORMATION);
            closeWindow(event);
        }
    }

    // Método botón Eliminar
    @FXML
    public void onDeletedUserClick(ActionEvent event) {

        AmbUserModel deletedUser = cmbUsers.getSelectionModel().getSelectedItem();

        if(deletedUser == null){
            new Alerts("No se ha seleccionado un usuario para borrar", Alert.AlertType.WARNING);
            return;
        }

        Alert dataConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        dataConfirm.setTitle("Confirmación");
        dataConfirm.setHeaderText( "¿Estás seguro de que deseas eliminar esta dotación?");
        dataConfirm.setContentText(deletedUser.toUserString());
        Optional<ButtonType> result = dataConfirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try{
                if (deletedUser.getId() != null) {
                    _objUserDAO.deleteUser(deletedUser.getId());
                    new Alerts("Usuario borrado correctamente", Alert.AlertType.INFORMATION);
                    closeWindow(event);
                }
            } catch (Exception e) {
                new Alerts("Error al eliminar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            new Alerts("Eliminación cancelada", Alert.AlertType.INFORMATION);
        }
    }

    // Método botón Cancelar
    @FXML
    public void onCancelClick(ActionEvent event) {closeWindow(event);}

    // Método para selecionar los elementos del ComboBox de Usuarios
    @FXML
    public void onCmbUserSelect(ActionEvent event) {
        formatUserSelected(cmbUsers,txtName,txtSurName,cmbProfs,txtEmail,txtPass,txtRepeatPass,txtPhone);
    }

    /*
     * Método para cerrar ventana
     */
    @FXML private void closeWindow(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /*
     * Método para setear el usuario
     */
    @FXML
    private void setSaveOrModifyUser(AmbUserModel user) {
        user.setName(txtName.getText());
        user.setSurName(txtSurName.getText());
        user.setPassword(txtPass.getText());
        user.setEmail(txtEmail.getText());
        user.setPhone( Integer.parseInt(txtPhone.getText()));
        user.setProfession(this.cmbProfs.getSelectionModel().getSelectedItem());

    }

    /*
     * Método para iniciar los ComboBox
     */
    @FXML
    private void initUserComboBox() {
        ObservableList<AmbUserModel> allUser = FXCollections.observableArrayList();

        List<AmbUserModel> users = this._objUserDAO.getAllUsers();
        allUser.addAll(users);

        this.cmbUsers.setItems(allUser);
        formatComboBoxUser(cmbUsers);
    }

    @FXML
    private void initProfComboBox() {
        ObservableList<AmbProfModel> allProf = FXCollections.observableArrayList();

        List<AmbProfModel> profs = this._objProfDAO.getAllProfs();

        allProf.addAll(profs);

        this.cmbProfs.setItems(allProf);
        formatComboBoxProf(cmbProfs);

    }

    /*
     * Método para dar formato a los Combobox
     */
    private void formatComboBoxUser(ComboBox <AmbUserModel> cb) {
        cb.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AmbUserModel> call(ListView<AmbUserModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AmbUserModel user, boolean empty) {
                        super.updateItem(user, empty);
                        if (empty || user == null) {
                            setText(null);
                        } else {
                            setText(user.getName() + " " + user.getSurName());
                        }
                    }
                };
            }
        });
        cb.setConverter(new StringConverter<AmbUserModel>() {
            @Override
            public String toString(AmbUserModel user) {
                return (user != null) ? user.getName() + " " + user.getSurName() : "";
            }

            @Override
            public AmbUserModel fromString(String string) {
                return null; // No necesitas conversión de String a AmbUserModel
            }
        });
    }

    private void formatComboBoxProf(ComboBox <AmbProfModel> cb) {
        cb.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AmbProfModel> call(ListView<AmbProfModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AmbProfModel prof, boolean empty) {
                        super.updateItem(prof, empty);
                        if (empty || prof == null) {
                            setText(null);
                        } else {
                            setText(prof.getProfName() + " -- " + "[ "+ prof.getProfCode()+ " ]");
                        }
                    }
                };
            }
        });
        cb.setConverter(new StringConverter<AmbProfModel>() {
            @Override
            public String toString(AmbProfModel prof) {
                return (prof != null) ?  prof.getProfCode() : "";
            }

            @Override
            public AmbProfModel fromString(String string) {
                return null; // No necesitas conversión de String a AmbProfModel
            }
        });
    }

    /*
     * Método para formatear las selecciones de los usuarios
     */
    @FXML
    private void formatUserSelected(ComboBox<AmbUserModel> cmbUser, TextField lblUserName, TextField lblUserSurname, ComboBox<AmbProfModel> cmbUserProf, TextField lblUserEmail, PasswordField lblUserPass,PasswordField lblUserRepeatPass, TextField lblUserPhone) {
        String userName = cmbUser.getSelectionModel().getSelectedItem().getName();
        String userSurname = cmbUser.getSelectionModel().getSelectedItem().getSurName();
        AmbProfModel userProf = cmbUser.getSelectionModel().getSelectedItem().getProfession();
        String userEmail = cmbUser.getSelectionModel().getSelectedItem().getEmail();
        String userPass = cmbUser.getSelectionModel().getSelectedItem().getPassword();
        String userRepeatPass = cmbUser.getSelectionModel().getSelectedItem().getPassword();
        Integer phone = cmbUser.getSelectionModel().getSelectedItem().getPhone();




        lblUserName.setText(userName);
        lblUserSurname.setText(userSurname);
        cmbUserProf.setValue(userProf);
        lblUserEmail.setText(userEmail);
        lblUserPass.setText(userPass);
        lblUserRepeatPass.setText(userRepeatPass);
        lblUserPhone.setText(phone + "");
    }

    /*
     * Métodos de validación para el formulario
     */
    private boolean validForm() {

        boolean valid = validPassword(txtPass, txtRepeatPass);

        if (isFieldEmpty(txtName)) {
            errorMessage("El campo usuario no puede estar vacío");
            valid = false;
        }

        if (!validMail(txtEmail)) {
            valid = false;
        }

        return valid;
    }

    private boolean validPassword(PasswordField password, PasswordField repeatPassword) {

        if (isFieldEmpty(password)) {
            errorMessage("El campo contraseña no puede estar vacío");
            return false;

        } else if (isFieldEmpty(repeatPassword)) {
            errorMessage("El campo repetir contraseña no puede estar vacío");
            return false;

        } else if (!password.getText().equals(repeatPassword.getText())) {
            errorMessage("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    private boolean validMail(TextField email) {

        if (isFieldEmpty(email)) {
            errorMessage("El campo de email no puede estar vacío");
            return false;
        } else {
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email.getText());

            if (!matcher.matches()) {
                errorMessage(
                        "Formato de email incorrecto.\nFormato correcto: ejemplo@ejemplo.com ");
                return false;
            }
        }
        return true;
    }
    private void errorMessage(String message) {txtError.setText(message);}
    private boolean isFieldEmpty(TextField txt) {return txt.getText().isEmpty();}

}
