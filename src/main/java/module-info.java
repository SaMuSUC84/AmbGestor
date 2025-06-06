module com.example.ambgestor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires java.persistence;
    requires java.sql;
    requires org.jboss.logging;
    requires java.transaction;



    exports com.example.ambgestor.app;
    opens com.example.ambgestor.app to javafx.fxml;
    exports com.example.ambgestor.controllers;
    opens com.example.ambgestor.controllers to javafx.fxml;
    exports com.example.ambgestor.models.entities;
    opens com.example.ambgestor.models.entities to org.hibernate.orm.core;
    exports com.example.ambgestor.models.daos;
    opens com.example.ambgestor.models.daos to org.junit.platform.engine;


}