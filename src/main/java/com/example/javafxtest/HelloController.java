package com.example.javafxtest;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.hipparchus.geometry.euclidean.threed.Vector3D;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public class HelloController {
    @FXML
    private MapView mappa;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ScrollPane scrollPane;


    public void initialize() throws IOException {
        //Add checkbox with label to scrollpane

        Utils utils = new Utils();
        utils.updateTLEs();
        ArcGISMap map = new ArcGISMap(BasemapStyle.OSM_STANDARD);
        mappa.setMap(map);
        mappa.setViewpoint(new Viewpoint(34.02700, -118.80543, 1000000000));

        // create a graphics overlay and add it to the map view
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mappa.getGraphicsOverlays().add(graphicsOverlay);

        File orekitData = new File("C:\\Users\\Cosimo\\Desktop\\orekit-data-master");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));
        OrekitGetLLATest orekitGetLLATest = new OrekitGetLLATest();
        orekitGetLLATest.setUp();
        orekitGetLLATest.setUpBeforeEach();

        //===================================//
        Point point = new Point(orekitGetLLATest.testGetLongitudeFromTLEUsingOrekit(), orekitGetLLATest.testGetLatitudeFromTLEUsingOrekit(), SpatialReferences.getWgs84());
        // create an opaque orange point symbol with a opaque blue outline symbol
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.ORANGE, 10);
        SimpleLineSymbol blueOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2);
        simpleMarkerSymbol.setOutline(blueOutlineSymbol);

        // create a graphic with the point geometry and symbol
        Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);

        // add the point graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(pointGraphic);

        //update the point graphic's geometry
        point = new Point(22,22, SpatialReferences.getWgs84());
        pointGraphic.setGeometry(point);

        utils.updateTLEs();
    }
}