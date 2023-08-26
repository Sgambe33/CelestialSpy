package com.example.javafxtest;

import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.labeling.SimpleLabelExpression;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

import com.esri.arcgisruntime.mapping.view.ViewLabelProperties;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.javafxtest.Globals.blueOutlineSymbol;
import static com.example.javafxtest.Globals.simpleMarkerSymbol;

public class HelloController {
    @FXML
    private MapView mappa;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ListView<CheckBox> listView;
    @FXML
    private ComboBox<String> satCategory;
    private Utils utils = new Utils();
    private HashMap<String, LatLongLocation> currentlyTrackedSats = new HashMap<String, LatLongLocation>();
    private HashMap<String, Satellite> satellites = new HashMap<String, Satellite>();
    private final GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
    private Satellite[] sats;



    public void initialize() throws IOException {
        //====================SATELLITE CATEGORIES LOADING====================//
        List<String> categories = new ArrayList<String>(Globals.SATELLITES_URL.keySet());
        satCategory.getItems().addAll(categories);

        //====================ARCGIS INIT====================//
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_IMAGERY);
        mappa.setMap(map);
        mappa.setViewpoint(new Viewpoint(34.02700, -118.80543, 1000000000));
        mappa.getGraphicsOverlays().add(graphicsOverlay);
        simpleMarkerSymbol.setOutline(blueOutlineSymbol);

        mappa.setOnMouseClicked(e -> {
            Point point = mappa.screenToLocation(new Point2D(e.getX(), e.getY()));
            point = (Point) GeometryEngine.project(point, SpatialReferences.getWgs84());
            System.out.println(point);
            Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
            graphicsOverlay.getGraphics().add(pointGraphic);

            //Find nearest satellite
            double minDistance = Double.MAX_VALUE;
            String nearestSat = "";
            for (Satellite sat : sats) {
                if(sat == null){
                    break;
                }
                double distance = sat.getCurrentPosition().distanceTo(point);
                if(distance < minDistance){
                    minDistance = distance;
                    nearestSat = sat.name;
                }
            }
            System.out.println("Nearest satellite: " + nearestSat + " at " + minDistance + " km");

        });
    }

    public void handleComboBoxAction(){
        System.out.println(satCategory.getValue());
        sats = utils.readTLEs(satCategory.getValue());
        List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        for (Satellite sat : sats) {
            if(sat == null){
                break;
            }
            System.out.println(sat.name);
            //TODO: add better checkbox listener
            CheckBox test = new CheckBox(sat.name);
            test.setOnAction((event) -> {
                if(test.isSelected()){
                    System.out.println(test.getText() + " got selected");
                    //Find satellite in sats array
                    for (Satellite sat1 : sats) {
                        if (sat1.name.equals(test.getText())) {
                            Thread one = new Thread() {
                                public void run() {
                                    Point point = new Point(0,0, SpatialReferences.getWgs84());
                                    Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
                                    graphicsOverlay.getGraphics().add(pointGraphic);

                                    Graphic label = new Graphic(point, new TextSymbol(15, sat1.name, Color.BLACK, TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.TOP));
                                    label.setSelected(true);
                                    graphicsOverlay.getGraphics().add(label);

                                    while(true){
                                        try {
                                            Thread.sleep(1000);

                                            System.out.println(sat1.getCurrentPosition());
                                            point = new Point(sat1.getCurrentPosition().getLongitude(),sat1.getCurrentPosition().getLatitude(), SpatialReferences.getWgs84());
                                            pointGraphic.setGeometry(point);
                                            label.setGeometry(point);
                                        } catch(InterruptedException v) {
                                            System.out.println(v);
                                        }
                                    }

                                }
                            };

                            one.start();
                            System.out.println(sat1.getCurrentPosition());
                        }
                    }

                }
            });
            checkBoxes.add(test);
        }
        //Add ALL checkboxes
        listView.getItems().clear();
        listView.getItems().addAll(checkBoxes);
    }

    public void stop() {
        if (mappa != null) {
            mappa.dispose();
        }
    }
}

