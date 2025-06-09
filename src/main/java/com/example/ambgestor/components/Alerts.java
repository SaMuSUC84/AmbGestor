package com.example.ambgestor.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.util.Optional;

/*
 * @author Samuel Alonso Viera
 */
public class Alerts {

    // Componente personalizado de los Alerts

    public Alerts(String message, Alert.AlertType alertType) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        alert.setX((screenSize.getWidth() - alert.getWidth()) / 2);
        alert.setY((screenSize.getHeight() - alert.getHeight()) / 2);
    }


}