package com.example.ambgestor.controllers;
import com.example.ambgestor.components.Alerts;
import com.example.ambgestor.components.CsvGenerator;
import com.example.ambgestor.components.Windows;
import com.example.ambgestor.models.daos.AmbCrewDAO;
import com.example.ambgestor.models.daos.AmbProfDAO;
import com.example.ambgestor.models.daos.AmbUnitDAO;
import com.example.ambgestor.models.daos.AmbUserDAO;
import com.example.ambgestor.models.entities.AmbCrewModel;
import com.example.ambgestor.models.entities.AmbUnitModel;
import com.example.ambgestor.models.entities.AmbUserModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * @author Samuel Alonso Viera
 */
public class HomeController {

    private AmbUserDAO _objUserDAO;
    private AmbCrewDAO _objCrewDAO;
    private AmbProfDAO _objProfDAO;
    private AmbUnitDAO _objUnitDAO;

    @FXML
    private TableView<AmbCrewModel> tableDota;
    @FXML
    private TableColumn<AmbCrewModel,String> columnUnitCode;
    @FXML
    private TableColumn<AmbCrewModel,String> columnConduct;
    @FXML
    private TableColumn<AmbCrewModel,String> columnSanit;
    @FXML
    private TableColumn<AmbCrewModel,String> columnDoctor;
    @FXML
    private TableColumn<AmbCrewModel,String> columnUnitName;
    @FXML
    private Label labelUnitCode;
    @FXML
    private Label labelUnitName;
    @FXML
    private Label labelConduct;
    @FXML
    private Label labelFacult;
    @FXML
    private Label labelSanit;
    @FXML
    private TextField txtSearch;


    @FXML
    public void initialize() {

        this._objUserDAO = new AmbUserDAO();
        this._objCrewDAO = new AmbCrewDAO();
        this._objProfDAO = new AmbProfDAO();
        this._objUnitDAO = new AmbUnitDAO();

        showAllDotas();
        selectDota();
    }

    // Método para insertar dotaciones
    @FXML
    private void onInsertDota(ActionEvent e) {
        Windows insertView = new Windows(new Stage(),true,"new-crew-view", "Nueva Dotación");
        insertView.getStage().setOnHidden(event -> {
            showAllDotas();
        });


    }

    // Método para modificar dotaciones
    @FXML
    private void onModifyDota(ActionEvent e) {

        AmbCrewModel ambcrewSelected = tableDota.getSelectionModel().getSelectedItem();

        if(ambcrewSelected != null) {
            new Windows(new Stage(), "modify-crew-view", "Modificar Dotación",
                    ambcrewSelected);
        }else{
            new Alerts("No se ha seleccionado ninguna dotación para modificar",
                    Alert.AlertType.WARNING);
        }
        showAllDotas();
    }

    // Método para eliminar dotaciones
    @FXML
    private void onDeleteDota(ActionEvent e) {

        AmbCrewModel ambcrewSelected = tableDota.getSelectionModel().getSelectedItem();
        if (ambcrewSelected == null || ambcrewSelected.getId() == null) {
            new Alerts("No se ha seleccionado una dotación para borrar", Alert.AlertType.WARNING);
            return;
        }
        Alert dataConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        dataConfirm.setTitle("Confirmación");
        dataConfirm.setHeaderText( "¿Estás seguro de que deseas eliminar esta dotación?");
        dataConfirm.setContentText(ambcrewSelected.toCrewString());
        Optional<ButtonType> result = dataConfirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                _objCrewDAO.deleteCrew(ambcrewSelected.getId());
                new Alerts("Dotación borrada correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                new Alerts("Error al eliminar la dotación: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            new Alerts("Eliminación cancelada", Alert.AlertType.INFORMATION);
        }
        showAllDotas();
    }

    // Método para exportar a CSV las dotaciones
    @FXML
    private void onExportDotaCsv(ActionEvent e) {

        try{
            new CsvGenerator().saveFileCSV();

        } catch (IOException exc) {
            new Alerts("Error al exportar las dotaciones { "+ exc.getMessage()+" }", Alert.AlertType.ERROR);
        }
    }


    // Método de busqueda activa
    @FXML
    private void onActiveSearch(KeyEvent keyEvent) {

        String keyWord = txtSearch.getText().trim();

        // Obtener todos los datos si la búsqueda está vacía
        List<AmbCrewModel> searchCrews = new ArrayList<>();

        if(keyWord.isEmpty()) {
            searchCrews = _objCrewDAO.getAllCrews();
        }else if (keyWord.matches("\\d+")) {
            searchCrews = _objCrewDAO.searchByUnitCode(Integer.parseInt(keyWord));
        } else{
            // Agregar los resultados sin sobrescribir
            searchCrews.addAll(_objCrewDAO.searchCrews(keyWord));
            // Agregar resultado
            searchCrews.addAll(_objCrewDAO.searchByUnitName(keyWord));
        }

        // Evita errores si la consulta falla
        if (searchCrews == null) {
            searchCrews = new ArrayList<>();
        }

        // Verificar si hay resultados y actualizar la tabla
        ObservableList<AmbCrewModel> searchActiveCrews = FXCollections.observableArrayList(searchCrews);

        this.tableDota.setItems(searchActiveCrews);
        this.tableDota.refresh();
    }

    // Método que muestra los datos en la tabla
    @FXML
    private void showAllDotas(){
        // Obtener los datos desde el DAO y agregarlos a la tabla
        List<AmbCrewModel> getAllCrews = _objCrewDAO.getAllCrews();
        ObservableList<AmbCrewModel> allCrews = FXCollections.observableArrayList(getAllCrews);

        // Modificamos las tablas para aque presenten los datos como queremos.
        this.columnUnitCode.setCellValueFactory(data ->{
           AmbUnitModel unitCode = data.getValue().getUnitDota();
           String code = unitCode.getUnitCode().toString();
            return new SimpleStringProperty(code);
        });
        this.columnConduct.setCellValueFactory(data ->{
            AmbUserModel conductCrew = data.getValue().getConductDota();
            return new SimpleStringProperty(conductCrew.getName());
        });
        this.columnSanit.setCellValueFactory(data->{
            AmbUserModel sanitCrew = data.getValue().getSanitDota();
            return new SimpleStringProperty(sanitCrew.getName());
        });
        this.columnDoctor.setCellValueFactory(data->{
            String doctorCrew = data.getValue().isDoctor();
            return new SimpleStringProperty(doctorCrew);
        });
        this.columnUnitName.setCellValueFactory(data->{
            AmbUnitModel unitCrew = data.getValue().getUnitDota();
            return new SimpleStringProperty(unitCrew.getUnitName());
        });

        this.tableDota.setItems(allCrews);
        this.tableDota.refresh();
    }

    //  Método para que modifique los Labels de la vista
    @FXML
    private void selectDota(){

        this.tableDota.getSelectionModel().selectedItemProperty().addListener((obs,oldselect, newselected) ->{

            AmbCrewModel selectedCrew = this.tableDota.getSelectionModel().getSelectedItem();

            if(selectedCrew != null){

                boolean changeColor = !selectedCrew.getUnitDota().getUnitName().equalsIgnoreCase("Traslado No Urgente");

                this.labelUnitName.setText(selectedCrew.getUnitDota().getUnitName().toUpperCase());
                this.labelUnitName.setTextFill(changeColor ? Color.RED : Color.BLUE);
                this.labelConduct.setText(selectedCrew.getConductDota().getName());
                this.labelSanit.setText(selectedCrew.getSanitDota().getName());
                this.labelFacult.setText(selectedCrew.isDoctor());
                this.labelUnitCode.setText(selectedCrew.getUnitDota().getUnitCode().toString());
                this.labelUnitCode.setTextFill(changeColor ? Color.RED : Color.BLUE);
            }
        });

    }

    /*
     * Métodos de la TAB de Configuración
     */
    @FXML
    public void onUserClick(ActionEvent event) {
        new Windows(new Stage(),true, "user-view", "Gestión de Usuarios");
        showAllDotas();
    }

    @FXML
    public void onUnitClick(ActionEvent event) {
        new Windows(new Stage(),true,"unit-view", "Gestión de Unidades");
        showAllDotas();
    }
}