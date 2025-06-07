package com.example.ambgestor.controllers;

import com.example.ambgestor.components.Alerts;

import com.example.ambgestor.models.daos.AmbUnitDAO;

import com.example.ambgestor.models.entities.AmbUnitModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;

/*
 * @author Samuel Alonso Viera
 */
public class UnitController {

    private AmbUnitDAO _objUnitDAO;

    @FXML
    private ComboBox <String>cmbunitName;
    @FXML
    private ComboBox <AmbUnitModel>cmbUnit;
    @FXML
    private TextField txtUnitCode;
    @FXML
    private Label txtError;
    @FXML
    private Label txtImgUnitName;
    @FXML
    private Label txtImgUnitCode;

    @FXML
    public void initialize() {
        this._objUnitDAO = new AmbUnitDAO();
        initUnitComboBox();
        initUnitNameComboBox();
    }

    // Método para seleccionar las unidades del ComboBox
    @FXML
    public void onCmbUnitSelect(ActionEvent event) {
        AmbUnitModel unit = cmbUnit.getSelectionModel().getSelectedItem();
        txtUnitCode.setText(unit.getUnitCode()+"");
        cmbunitName.setValue(unit.getUnitName());
        formatTextImageColor(unit);
    }

    // Método botón Guardar
    @FXML
    public void onSaveUnitClick(ActionEvent event) {
        if(validForm()){
            AmbUnitModel newUnit = new AmbUnitModel();
            setSaveOrModifyUnit(newUnit);
            System.out.println("Nueva unidad: " + newUnit.getUnitCode() +" "+ newUnit.getUnitName());
            formatTextImageColor(newUnit);
            _objUnitDAO.saveUnit(newUnit);
            new Alerts("La unidad se creó con éxito", Alert.AlertType.INFORMATION);
            initUnitComboBox();
            cmbUnit.setValue(newUnit);
        }
    }

    // Métodoo botón Modificar
    @FXML
    public void onModifyUnitClick(ActionEvent event) {
        if(validForm()) {
            AmbUnitModel originalUnit = cmbUnit.getSelectionModel().getSelectedItem();

            if (originalUnit == null) {
                new Alerts("Debe seleccionar una unidad para modificar.", Alert.AlertType.WARNING);
                return;
            }
            AmbUnitModel modifyUnit = new AmbUnitModel();
            setSaveOrModifyUnit(modifyUnit);
            formatTextImageColor(modifyUnit);

            // Persistir la modificación sin duplicar datos
            _objUnitDAO.updateUnit(modifyUnit);
            _objUnitDAO.deleteUnit(originalUnit.getUnitCode());

            new Alerts("La unidad se modificó con éxito", Alert.AlertType.INFORMATION);
            initUnitComboBox();
            cmbUnit.setValue(modifyUnit);
        }

    }

    // Método botón Eliminar
    @FXML
    public void onDeletedUnitClick(ActionEvent event) {

        AmbUnitModel deletedUnit = cmbUnit.getSelectionModel().getSelectedItem();
        int deleteCode = (deletedUnit != null) ?
                deletedUnit.getUnitCode() :
                Integer.parseInt(txtUnitCode.getText());

        if (deletedUnit != null) {
            _objUnitDAO.deleteUnit(deleteCode);

            new Alerts("Unidad borrada correctamente", Alert.AlertType.INFORMATION);

            resetFormatImageColor();
            initUnitComboBox();
            initUnitNameComboBox();

        } else {
            new Alerts("No se ha seleccionado una unidad para borrar", Alert.AlertType.WARNING);
        }

    }

    // Método para el botón Cancelar
    @FXML
    public void onCancelClick(ActionEvent event) {closeWindow(event);}

    /*
     * Método para cerrar ventana
     */
    @FXML private void closeWindow(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /*
     * Método para iniciar el ComboBox de Unidades
     */
    @FXML
    private void initUnitComboBox() {

        List<AmbUnitModel> units = this._objUnitDAO.getAllUnits();
        ObservableList<AmbUnitModel> allUnit = FXCollections.observableArrayList(units);

        this.cmbUnit.getItems().clear();
        this.cmbUnit.setItems(allUnit);

        this.cmbUnit.getSelectionModel().clearSelection();
        formatComboBoxUnit(cmbUnit);
    }

    @FXML
    private void initUnitNameComboBox() {
        ObservableList<String> allUnit = FXCollections.observableArrayList(
                "Medicalizada",
                "Sanitarizada",
                "Soporte Vital Básico",
                "Traslado No Urgente"
        );
        this.cmbunitName.setItems(allUnit);
    }

    /*
     * Método para dar formato a los Combobox de la BBDDD
     */
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
                return null; // No necesitas conversión de String a AmbUnitModel
            }
        });
    }
    /*
     * Método para seleccionar el color de la imagen
     */
    @FXML
    private void formatTextImageColor(AmbUnitModel unit) {
        boolean changeColor = !unit.getUnitName().equals("Traslado No Urgente");
        txtImgUnitCode.setText(unit.getUnitCode()+"");
        txtImgUnitCode.setTextFill(changeColor ? Color.RED : Color.BLUE);

        txtImgUnitName.setText(unit.getUnitName().toUpperCase());
        txtImgUnitName.setTextFill(changeColor ? Color.RED : Color.BLUE);
    }

    /*
     * Método para resetar el formulario
     */
    @FXML
    private void resetFormatImageColor(){

        txtImgUnitCode.setText("");
        txtImgUnitName.setText("");
        txtUnitCode.clear();

        resetComboBox(cmbUnit,"Modificar o Eliminar");
        resetComboBox(cmbunitName,"Recurso...");
    }

    /*
     * Método para resetear los combobox
     */
    @FXML
    private void resetComboBox(ComboBox cmb, String msg) {
        cmb.getItems().clear();
        cmb.getSelectionModel().clearSelection();
        cmb.setValue(null);
        cmb.setPromptText(msg);
        cmb.hide();

    }

    /*
     * Método para setear el unidad
     */
    @FXML
    private void setSaveOrModifyUnit(AmbUnitModel unit) {
        unit.setUnitCode(Integer.parseInt(txtUnitCode.getText()));
        unit.setUnitName(cmbunitName.getValue());
    }


    /*
     * Métodos para validar el Formulario
     */
    private boolean validForm() {

        if (cmbunitName.getValue() == null || cmbunitName.getValue().isEmpty()) {
            return showMessage("El campo de recurso no puede estar vacío");
        }
        if (isFieldEmpty(txtUnitCode)){
            return showMessage("El campo unidad esta vacio");
        }
        if(!isUnitValid(txtUnitCode,cmbunitName)){
            return showMessage("La unidad no es válida");
        }

        txtError.setVisible(false);
        return true;
    }


    private boolean showMessage(String message) {
        errorMessage(message);
        return false;
    }

    private void errorMessage(String message) {
        txtError.setVisible(true);
        txtError.setText(message);
    }

    private boolean isFieldEmpty(TextField txt) {return txt.getText().isEmpty();}

    private boolean isUnitValid (TextField unitcode, ComboBox<String> unitname) {

        if (unitcode.getText().isEmpty() || unitname.getValue() == null || unitname.getValue().trim().isEmpty()) {
            return false;
        }

        try {
            int code = Integer.parseInt(unitcode.getText().trim());
            String name = unitname.getValue().trim();

            // Validación estricta por rango
            if (code >= 100 && code <= 199 && name.equalsIgnoreCase("Medicalizada")) {
                System.out.println("Medicalizada");
                return true;
            } else if (code >= 200 && code <= 299 && name.equalsIgnoreCase("Sanitarizada")) {
                System.out.println("Sanitarizada");
                return true;
            } else if (code >= 300 && code <= 399 && name.equalsIgnoreCase("Soporte Vital Básico")) {
                System.out.println("Soporte Vital Básico");
                return true;
            } else if (code >= 400 && code <= 499 && name.equalsIgnoreCase("Traslado No Urgente")) {
                System.out.println("Traslado No Urgente");
                return true;
            } else {
                errorMessage("El código " + code + " no es válido para la categoría " + name);
                return false;
            }

        } catch (NumberFormatException e) {
            new Alerts("Unidad no válida", Alert.AlertType.ERROR);
            return false;
        }
    }
}
