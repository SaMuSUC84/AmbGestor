package com.example.ambgestor.components;

import com.example.ambgestor.controllers.ModifyCrewController;
import javafx.scene.Cursor;
import com.example.ambgestor.models.entities.AmbCrewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.awt.*;
import java.io.IOException;
import static javafx.stage.StageStyle.UNDECORATED;


/*
 * @author Samuel Alonso Viera
 */
public class Windows {

    private double mouseX = 0;
    private double mouseY = 0;

    public Windows(Stage stage, String viewName, String title) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ambgestor/views/"+viewName+".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.showAndWait();

            /*
             * Modo de centrar las ventanas que se generen en la pantalla
             */
            stage.setX((screenSize.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenSize.getHeight() - stage.getHeight()) / 2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Windows(Stage stage, String viewName, String title, AmbCrewModel ambCrewModel) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (ambCrewModel != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ambgestor/views/"+viewName+".fxml"));;
                Parent root = loader.load();
                Scene scene = new Scene(root);

                ModifyCrewController controller = loader.getController();
                controller.modifyCrew(ambCrewModel);

                stage = new Stage();
                modalShow(stage, scene);

                stage.setScene(scene);
                stage.setTitle(title);
                stage.showAndWait();

                /*
                 * Modo de centrar las ventanas que se generen en la pantalla
                 */
                stage.setX((screenSize.getWidth() - stage.getWidth()) / 2);
                stage.setY((screenSize.getHeight() - stage.getHeight()) / 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Windows(Stage stage, boolean isModal,String viewName, String title) {

        try {
            if(isModal){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ambgestor/views/"+viewName+".fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                modalShow(stage, scene);

                stage.setScene(scene);
                stage.show();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para hacer los modales sin decoracion del sistema

    private void modalShow(Stage stage, Scene scene) {
        stage.initStyle(UNDECORATED);
        scene.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            scene.setCursor(Cursor.CLOSED_HAND);
        });

        scene.setOnMouseReleased(event -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - mouseX);
            stage.setY(event.getScreenY() - mouseY);
        });
    }




}
