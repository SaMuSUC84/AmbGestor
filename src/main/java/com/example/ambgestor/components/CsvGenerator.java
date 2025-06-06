package com.example.ambgestor.components;

import com.example.ambgestor.models.daos.AmbCrewDAO;
import com.example.ambgestor.models.entities.AmbCrewModel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvGenerator {

    public void saveFileCSV() throws IOException {

        List<AmbCrewModel> allCrews = new AmbCrewDAO().getAllCrews();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),
                    StandardCharsets.UTF_8)) {

                // Se agrega un BOM para que programas como EXCEL reconozcan los acentos y de un formate correcto a los datos.
                writer.write("\uFEFF");
                writer.write("Unidad;Conductor;Sanitario;Facultativo;Recurso\n");
                for (AmbCrewModel ambcrew : allCrews) {
                    String isDoctor = (!ambcrew.isDoctor().equalsIgnoreCase("Sin facultativo"))
                            ? ambcrew.getDoctorDota().getName() + " " + ambcrew.getDoctorDota().getSurName()
                            : "SIN MÉDICO";
                    writer.write(
                            ambcrew.getUnitDota().getUnitCode() + ";"
                                    + ambcrew.getConductDota().getName() + " " + ambcrew.getConductDota().getSurName() + ";"
                                    + ambcrew.getSanitDota().getName() + " " + ambcrew.getSanitDota().getSurName() + ";"
                                    + isDoctor + ";"
                                    + ambcrew.getUnitDota().getUnitName()+"\n");
                }
            }

        }

    }
}