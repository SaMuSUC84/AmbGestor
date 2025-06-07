package com.example.ambgestor.controllers;

import com.example.ambgestor.components.Alerts;
import com.example.ambgestor.models.daos.AmbCrewDAO;
import com.example.ambgestor.models.daos.AmbProfDAO;
import com.example.ambgestor.models.daos.AmbUnitDAO;
import com.example.ambgestor.models.daos.AmbUserDAO;

import com.example.ambgestor.models.entities.AmbCrewModel;
import com.example.ambgestor.models.entities.AmbProfModel;
import com.example.ambgestor.models.entities.AmbUnitModel;
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

/*
 * @author Samuel Alonso Viera
 */
public class NewCrewController {

    private AmbUserDAO _objUserDAO;
    private AmbCrewDAO _objCrewDAO;
    private AmbUnitDAO _objUnitDAO;

    @FXML
    private ComboBox <AmbUserModel>cmbUserConduct;
    @FXML
    private Label lblConductName;
    @FXML
    private Label lblConductSurname;
    @FXML
    private Label lblConductProf;
    @FXML
    private ComboBox <AmbUserModel>cmbUserSanit;
    @FXML
    private Label lblSanitName;
    @FXML
    private Label lblSanitSurname;
    @FXML
    private Label lblSanitProf;
    @FXML
    private ComboBox <AmbUserModel>cmbUserFacult;
    @FXML
    private Label lblFacultName;
    @FXML
    private Label lblFacultSurname;
    @FXML
    private ComboBox<AmbUnitModel> cmbUnitCode;
    @FXML
    private Label labelUnitName;


    @FXML
    public void initialize() {
        this._objUserDAO = new AmbUserDAO();
        this._objCrewDAO = new AmbCrewDAO();
        this._objUnitDAO = new AmbUnitDAO();

        initConductComboBox();
        initSanitComboBox();
        initDoctorComboBox();
        initUnitComboBox();
    }

    @FXML
    public void onConductSelected(ActionEvent e) {
        formatUserSelected(cmbUserConduct, lblConductName, lblConductSurname, lblConductProf);
    }

    @FXML
    public void onSanitSelected(ActionEvent e) {
        formatUserSelected(cmbUserSanit, lblSanitName, lblSanitSurname, lblSanitProf);
    }

    @FXML
    public void onFacultSelected(ActionEvent e) {
        String facultName = cmbUserFacult.getSelectionModel().getSelectedItem().getName();
        String facultSurname = cmbUserFacult.getSelectionModel().getSelectedItem().getSurName();

        lblFacultName.setText(facultName);
        lblFacultSurname.setText(facultSurname);
    }

    @FXML
    public void onUnitCodeSelected(ActionEvent e) {
        String unitName = cmbUnitCode.getSelectionModel().getSelectedItem().getUnitName();
        labelUnitName.setText(unitName);
    }

    @FXML
    public void onSaveNewCrewClick(ActionEvent e) {
        AmbCrewModel newCrew = new AmbCrewModel();
        AmbUserModel newConduct = cmbUserConduct.getSelectionModel().getSelectedItem();
        AmbUserModel newSanit = cmbUserSanit.getSelectionModel().getSelectedItem();
        AmbUserModel newFacult = cmbUserFacult.getSelectionModel().getSelectedItem();
        AmbUnitModel newUnit = cmbUnitCode.getSelectionModel().getSelectedItem();

        // Validaciones para las unidades según el tipo de recurso
        if(newUnit == null){
            new Alerts("Debe seleccionar una unidad antes de crear una nueva dotación", Alert.AlertType.WARNING);
            return;
        }

        if(newSanit == null){
            new Alerts("Debe seleccionar una sanitario", Alert.AlertType.WARNING);
            return;
        }

        if( newUnit.getUnitName().equalsIgnoreCase("Medicalizada")){
            if(newFacult == null || !newSanit.getProfession().getProfCode().equalsIgnoreCase("DUE")){
                new Alerts("Las Medicalizadas requieren un facultativo y un enfermero", Alert.AlertType.WARNING);
                return;
            }
        }

        if( newUnit.getUnitName().equalsIgnoreCase("Sanitarizada")){
            if(!newSanit.getProfession().getProfCode().equalsIgnoreCase("DUE")){
                new Alerts("Las Sanitarizadas el sanitario tiene que ser un enfermero", Alert.AlertType.WARNING);
                return;
            }
        }

        // Asignamos valores si las validaciones son correctas
        newCrew.setConductDota(newConduct);
        newCrew.setSanitDota(newSanit);
        newCrew.setDoctorDota(newFacult);
        newCrew.setUnitDota(newUnit);

        _objCrewDAO.saveCrew(newCrew);

        new Alerts("La dotación se ha creado correctamente", Alert.AlertType.INFORMATION);
        closeWindow(e);
    }


    /*
     * Método para iniciar los ComboBox
     */

    @FXML
    private void initConductComboBox() {
        ObservableList<AmbUserModel> allUser = FXCollections.observableArrayList();

        List<AmbUserModel> users = this._objUserDAO.getAllUsers();

        for (AmbUserModel user : users) {
            if(user.getProfession().getProfCode().equals("TES")){
                allUser.add(user);
            }
        }
        this.cmbUserConduct.setItems(allUser);
        formatComboBoxUser(cmbUserConduct);
    }

    @FXML
    private void initSanitComboBox() {
        ObservableList<AmbUserModel> allUser = FXCollections.observableArrayList();
        List<AmbUserModel> users = this._objUserDAO.getAllUsers();

        for (AmbUserModel user : users) {
            if(!user.getProfession().getProfCode().equals("MED")){
                allUser.add(user);
            }
        }
        this.cmbUserSanit.setItems(allUser);
        formatComboBoxUser(cmbUserSanit);

    }
    @FXML
    private void initDoctorComboBox() {
        ObservableList<AmbUserModel> allUser = FXCollections.observableArrayList();
        List<AmbUserModel> users = this._objUserDAO.getAllUsers();

        for (AmbUserModel user : users) {
            if(user.getProfession().getProfCode().equals("MED")){
                allUser.add(user);
            }
        }
        this.cmbUserFacult.setItems(allUser);
        formatComboBoxUser(cmbUserFacult);
    }

    @FXML
    private void initUnitComboBox() {
        ObservableList<AmbUnitModel> allUnit = FXCollections.observableArrayList();
        List<AmbUnitModel> units = this._objUnitDAO.getAllUnits();
        allUnit.addAll(units);
        this.cmbUnitCode.setItems(allUnit);
        formatComboBoxUnit(cmbUnitCode);
    }

    /*
     * Método para formatear las selecciones de los usuarios
     */
    @FXML
    private void formatUserSelected(ComboBox<AmbUserModel> cmbUser, Label lblUserName, Label lblUserSurname, Label lblUserProf) {
        String userName = cmbUser.getSelectionModel().getSelectedItem().getName();
        String userSurname = cmbUser.getSelectionModel().getSelectedItem().getSurName();
        String userProf = cmbUser.getSelectionModel().getSelectedItem().getProfession().getProfName();

        lblUserName.setText(userName);
        lblUserSurname.setText(userSurname);
        lblUserProf.setText(userProf);
    }


    /*
     * Método para botón cancelar
     */
    @FXML
    public void onCancelNewCrewClick(ActionEvent e) {closeWindow(e);  }


    /*
     * Método para cerrar ventana
     */
    @FXML private void closeWindow(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /*
     * Método para formatear los ComboBox de los Usuarios
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

    /*
     * Método para formatear el ComboBox de las unidades
     */
    private void formatComboBoxUnit(ComboBox <AmbUnitModel> cb) {
        cb.setCellFactory(new Callback<ListView<AmbUnitModel>, ListCell<AmbUnitModel>>() {
            @Override
            public ListCell<AmbUnitModel> call(ListView<AmbUnitModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AmbUnitModel unit, boolean empty) {
                        super.updateItem(unit, empty);
                        if (empty || unit == null) {
                            setText(null);
                        } else {
                            setText(unit.getUnitCode()+"");
                        }
                    }
                };
            }
        });

        cb.setConverter(new StringConverter<AmbUnitModel>() {
            @Override
            public String toString(AmbUnitModel unit) {
                return (unit != null) ? unit.getUnitCode()+"" : "";
            }

            @Override
            public AmbUnitModel fromString(String string) {
                return null;
            }
        });
    }



}
