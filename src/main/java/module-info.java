module com.example.pixaid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires static com.dlsc.formsfx; // Use static if only required at runtime
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires com.google.gson;
    requires java.sql;

    opens com.example.pixaid to javafx.fxml, com.google.gson;
    exports com.example.pixaid;
}
