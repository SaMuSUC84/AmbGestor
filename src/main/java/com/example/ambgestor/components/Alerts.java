package com.example.ambgestor.components;

import javafx.scene.control.Alert;

import java.awt.*;

/*
 * @author Samuel Alonso Viera
 */
public class Alerts {

    // Componente personalizado de los Alerts

    public Alerts(String message, Alert.AlertType alertType) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
        alert.setX((screenSize.getWidth() - alert.getWidth()) / 2);
        alert.setY((screenSize.getHeight() - alert.getHeight()) / 2);
    }
}