module com.example.javafxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    opens com.example.javafxtest to javafx.fxml;
    exports com.example.javafxtest;
    // require ArcGIS Runtime module
    requires com.esri.arcgisruntime;
    // requires JavaFX modules that the application uses
    requires javafx.graphics;
    // requires SLF4j module
    requires org.slf4j.nop;
    requires java.sql;
    requires org.orekit;
    requires hipparchus.geometry;
    requires hipparchus.core;
}