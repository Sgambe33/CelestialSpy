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
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.data.ZipJarCrawler;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //===================ARCGIS INIT====================//

        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);
        ArcGISRuntimeEnvironment.setLicense(licenseKey);

        //===================OREKIT INIT====================//
        File orekitData = new File("E:\\GITHUB_REPOS\\JavaFXTest\\orekit-data-master.zip");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new ZipJarCrawler(orekitData));



        //===================JavaFX INIT====================//
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test2.fxml"));
        //Apply the CSS stylesheet to the scene
        Scene scene = new Scene(fxmlLoader.load(), 640, 375);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        stage.setTitle("CelestialSpy");
        stage.setScene(scene);
        stage.setMinHeight(500);
        stage.setMinWidth(700);
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        Utils utils = new Utils();
        utils.updateTLEs();
        launch();
    }
}