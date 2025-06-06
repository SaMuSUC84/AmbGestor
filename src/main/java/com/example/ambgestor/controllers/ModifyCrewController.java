package com.example.ambgestor.controllers;

import com.example.ambgestor.components.Alerts;
import com.example.ambgestor.models.daos.AmbCrewDAO;
import com.example.ambgestor.models.daos.AmbUnitDAO;
import com.example.ambgestor.models.daos.AmbUserDAO;
import com.example.ambgestor.models.entities.AmbCrewModel;
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

public class ModifyCrewController {

    private AmbCrewModel _modifyCrew;
    private AmbUserDAO _objUserDAO;
    private AmbCrewDAO _objCrewDAO;
    private AmbUnitDAO _objUnitDAO;

    @FXML
    private Label lblConductModifyName;
    @FXML
    private Label lblConductModifySurname;
    @FXML
    private Label lblConductModifyProf;
    @FXML
    private Label lblSanitModifyName;
    @FXML
    private Label lblSanitModifySurname;
    @FXML
    private Label lblSanitModifyProf;
    @FXML
    private Label lblFacultModifyName;
    @FXML
    private Label lblFacultModifySurname;
    @FXML
    private Label lblUnitModifyName;
    @FXML
    private ComboBox <AmbUserModel>cmbUserConductModify;
    @FXML
    private ComboBox <AmbUserModel>cmbUserSanitModify;
    @FXML
    private ComboBox <AmbUserModel>cmbFacultModify;
    @FXML
    private ComboBox <AmbUnitModel>cmbUnitCodeModify;

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
    public void modifyCrew(AmbCrewModel ambCrewSelected) {

        this._modifyCrew = ambCrewSelected;

        if (_modifyCrew.getConductDota() != null) {
            cmbUserConductModify.setValue(_modifyCrew.getConductDota());
            lblConductModifyName.setText(_modifyCrew.getConductDota().getName());
            lblConductModifySurname.setText(_modifyCrew.getConductDota().getSurName());
            lblConductModifyProf.setText(_modifyCrew.getConductDota().getProfession().getProfName());
        }

        if (_modifyCrew.getSanitDota() != null) {
            cmbUserSanitModify.setValue(_modifyCrew.getSanitDota());
            lblSanitModifyName.setText(_modifyCrew.getSanitDota().getName());
            lblSanitModifySurname.setText(_modifyCrew.getSanitDota().getSurName());
            lblSanitModifyProf.setText(_modifyCrew.getSanitDota().getProfession().getProfName());
        }

        if (_modifyCrew.getDoctorDota() != null) {
            cmbFacultModify.setValue(_modifyCrew.getDoctorDota());
            lblFacultModifyName.setText(_modifyCrew.getDoctorDota().getName());
            lblFacultModifySurname.setText(_modifyCrew.getDoctorDota().getSurName());
        }

        if (_modifyCrew.getUnitDota() != null) {
            cmbUnitCodeModify.setValue(_modifyCrew.getUnitDota());
            lblUnitModifyName.setText(_modifyCrew.getUnitDota().getUnitName());
        }
    }

    @FXML
    public void onConductModifySelected(ActionEvent event) {
        formatUserSelected(cmbUserConductModify, lblConductModifyName, lblConductModifySurname, lblConductModifyProf);
    }

    @FXML
    public void onSanitModifySelected(ActionEvent event) {
        formatUserSelected(cmbUserSanitModify, lblSanitModifyName, lblSanitModifySurname, lblSanitModifyProf);
    }

    @FXML
    public void onFacultModifySelected(ActionEvent event) {
        String facultName = cmbFacultModify.getSelectionModel().getSelectedItem().getName();
        String facultSurname = cmbFacultModify.getSelectionModel().getSelectedItem().getSurName();

        lblFacultModifyName.setText(facultName);
        lblFacultModifySurname.setText(facultSurname);
    }

    @FXML
    public void onUnitCodeModifySelected(ActionEvent event) {
        String unitName = cmbUnitCodeModify.getSelectionModel().getSelectedItem().getUnitName();
        lblUnitModifyName.setText(unitName);
    }

    @FXML
    public void onCancelModifyCrewClick(ActionEvent event) {closeWindow(event); }

    @FXML
    public void onModifyCrewClick(ActionEvent event) {

        AmbCrewModel ambcrewModify = new AmbCrewModel();
        AmbUserModel conductModify = cmbUserConductModify.getSelectionModel().getSelectedItem();
        AmbUserModel sanitModify = cmbUserSanitModify.getSelectionModel().getSelectedItem();
        AmbUserModel facultModify = cmbFacultModify.getSelectionModel().getSelectedItem();
        AmbUnitModel unitModify = cmbUnitCodeModify.getSelectionModel().getSelectedItem();

        ambcrewModify.setId(_modifyCrew.getId());
        ambcrewModify.setConductDota(conductModify);
        ambcrewModify.setSanitDota(sanitModify);
        ambcrewModify.setDoctorDota(facultModify);
        ambcrewModify.setUnitDota(unitModify);

        _objCrewDAO.updateCrew(ambcrewModify);

        new Alerts("La dotación se modificó correctamente", Alert.AlertType.INFORMATION);

        closeWindow(event);
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
        this.cmbUserConductModify.setItems(allUser);
        formatComboBoxUser(cmbUserConductModify);
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
        this.cmbUserSanitModify.setItems(allUser);
        formatComboBoxUser(cmbUserSanitModify);

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
        this.cmbFacultModify.setItems(allUser);

        formatComboBoxUser(cmbFacultModify);
    }

    @FXML
    private void initUnitComboBox() {

        ObservableList<AmbUnitModel> allUnit = FXCollections.observableArrayList();
        List<AmbUnitModel> units = this._objUnitDAO.getAllUnits();
        allUnit.addAll(units);
        this.cmbUnitCodeModify.setItems(allUnit);

        formatComboBoxUnit(cmbUnitCodeModify);
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
    @FXML
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
    @FXML
    private void formatComboBoxUnit(ComboBox <AmbUnitModel> cb) {
        cb.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AmbUnitModel> call(ListView<AmbUnitModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AmbUnitModel unit, boolean empty) {
                        super.updateItem(unit, empty);
                        if (empty || unit == null) {
                            setText(null);
                        } else {
                            setText(String.valueOf(unit.getUnitCode()));
                        }
                    }
                };
            }
        });

        cb.setConverter(new StringConverter<AmbUnitModel>() {
            @Override
            public String toString(AmbUnitModel unit) {
                return (unit != null) ? String.valueOf(unit.getUnitCode()) : "";
            }

            @Override
            public AmbUnitModel fromString(String string) {
                return null;
            }
        });
    }
}
