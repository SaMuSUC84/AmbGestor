package com.example.ambgestor.app;

import com.example.ambgestor.components.Windows;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


/*
 * @author Samuel Alonso Viera
 */
public class AmbGestor extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        new Windows(new Stage(), "home-view","Ambulance Gestor");

    }
    public static void main(String[] args) {launch();}
}