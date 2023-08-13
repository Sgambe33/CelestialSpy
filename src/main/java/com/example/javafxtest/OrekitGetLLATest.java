package com.example.javafxtest;

import org.hipparchus.util.FastMath;

import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class OrekitGetLLATest {

    private TLE tle;
    private Frame earthFrame;
    private TLEPropagator propagator;
    private BodyShape earth;
    private AbsoluteDate absoluteDate;

    public void setUp() throws IOException {
        //File orekitData = new File("orekit_data");
        //DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        //manager.addProvider(new DirectoryCrawler(orekitData));
        //=======================================================//
        tle = getIssTleFromCelesTrackAPI();
        //Get an unspecified International Terrestrial Reference Frame.
        earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        //Modeling of a one-axis ellipsoid.
        //One-axis ellipsoids is a good approximate model for most planet-size and larger natural bodies.
        earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                earthFrame);
        propagator = TLEPropagator.selectExtrapolator(tle);
    }
    public void setUpBeforeEach(){
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        absoluteDate = new AbsoluteDate(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                TimeScalesFactory.getUTC());
    }

    public double testGetLatitudeFromTLEUsingOrekit() throws IOException {
        PVCoordinates pvCoordinates = propagator.propagate(absoluteDate).getPVCoordinates(earthFrame);
        GeodeticPoint geodeticPoint = earth.transform(
                pvCoordinates.getPosition(),
                earthFrame,
                absoluteDate);
        double actualLatitude = FastMath.toDegrees(geodeticPoint.getLatitude());
        System.out.println(actualLatitude);
        return actualLatitude;
    }

    public double testGetLongitudeFromTLEUsingOrekit() throws IOException {
        PVCoordinates pvCoordinates = propagator.propagate(absoluteDate).getPVCoordinates(earthFrame);
        GeodeticPoint geodeticPoint = earth.transform(
                pvCoordinates.getPosition(),
                earthFrame,
                absoluteDate);
        double actualLongitude = FastMath.toDegrees(geodeticPoint.getLongitude());
        System.out.println(actualLongitude);
        return actualLongitude;
    }


    private TLE getIssTleFromCelesTrackAPI() throws IOException {
        String baseURL = "https://celestrak.org/NORAD/elements/gp.php?CATNR=25544&FORMAT=TLE";
        URL url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer tleContent = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            tleContent.append(inputLine + System.lineSeparator());
        }
        //TODO:remove when done testing
        System.out.println(tleContent);
        in.close();
        connection.disconnect();
        String[] tleLines = tleContent.toString().split("\\n");
        TLE tle = new TLE(tleLines[1], tleLines[2]);
        return tle;
    }
    //endregion

    private class LatLongLocation{
        private Double latitude;
        private Double longitude;
        public LatLongLocation() {
            latitude = null;
            longitude = null;
        }
        public LatLongLocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public Double getLatitude() {
            return latitude;
        }
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        public Double getLongitude() {
            return longitude;
        }
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
        @Override
        public String toString() {
            return "Location{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}