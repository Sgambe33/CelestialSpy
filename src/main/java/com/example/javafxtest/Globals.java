package com.example.javafxtest;

import java.util.HashMap;

public class Globals {

    public static final HashMap<String, String> SATELLITES_URL = new HashMap<>();
    static {
        SATELLITES_URL.put("STATIONS", "https://celestrak.org/NORAD/elements/gp.php?GROUP=stations&FORMAT=tle");
        SATELLITES_URL.put("WEATHER", "https://celestrak.org/NORAD/elements/gp.php?GROUP=weather&FORMAT=tle");
        SATELLITES_URL.put("GEO", "https://celestrak.org/NORAD/elements/gp.php?GROUP=geo&FORMAT=tle");
        SATELLITES_URL.put("GNSS", "https://celestrak.org/NORAD/elements/gp.php?GROUP=gnss&FORMAT=tle");
    }
}
