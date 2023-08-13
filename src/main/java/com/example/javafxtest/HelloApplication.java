package com.example.javafxtest;

import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.ViewLabelProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.view.MapView;
import java.io.IOException;

public class HelloApplication extends Application {
    private MapView mapView;

    @Override
    public void start(Stage stage) throws IOException {

        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);
        ArcGISRuntimeEnvironment.setLicense(licenseKey);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Apply the CSS stylesheet to the scene
        Scene scene = new Scene(fxmlLoader.load(), 640, 375);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        stage.setTitle("ARCGIS MAP");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}