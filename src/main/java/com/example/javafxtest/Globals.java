package com.example.javafxtest;

import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class Globals {

    public static final HashMap<String, String> SATELLITES_URL = new HashMap<>();
    static {
        SATELLITES_URL.put("STATIONS", "https://celestrak.org/NORAD/elements/gp.php?GROUP=stations&FORMAT=tle");
        SATELLITES_URL.put("WEATHER", "https://celestrak.org/NORAD/elements/gp.php?GROUP=weather&FORMAT=tle");
        SATELLITES_URL.put("GEO", "https://celestrak.org/NORAD/elements/gp.php?GROUP=geo&FORMAT=tle");
        SATELLITES_URL.put("GNSS", "https://celestrak.org/NORAD/elements/gp.php?GROUP=gnss&FORMAT=tle");
    }
    public static final SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.ORANGE, 10);
    public static final SimpleLineSymbol blueOutlineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2);

}
