package com.example.javafxtest;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.Graphic;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Satellite {
    public String name;
    public String line1;
    public String line2;
    public int noradId;
    public char classification;
    public int launchYear;
    public int launchNumber;
    public double inclination;
    public double rightAscension;
    public double eccentricity;
    public double argumentOfPerigee;
    public double meanAnomaly;
    public double meanMotion;
    public double revAtEpoch;
    public TLE tle;
    public Point positionPoint;
    public Graphic pointGraphic;
    private final Frame earthFrame;
    private final TLEPropagator propagator;
    private final BodyShape earth;

    public Satellite(String name, String line1, String line2){
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.tle = new TLE(line1, line2);
        this.earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        this.earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, earthFrame);
        this.propagator = TLEPropagator.selectExtrapolator(tle);
        extractFieldsFromTLE(this);
    }

    private void extractFieldsFromTLE(Satellite satellite) {
        satellite.noradId = Integer.parseInt(satellite.line1.substring(2, 7));
        satellite.classification = satellite.line1.charAt(7);
        satellite.launchYear = Integer.parseInt(satellite.line1.substring(9, 11));
        satellite.launchNumber = Integer.parseInt(satellite.line1.substring(11, 14));
        satellite.inclination = Double.parseDouble(satellite.line2.substring(8, 16));
        satellite.rightAscension = Double.parseDouble(satellite.line2.substring(17, 25));
        satellite.eccentricity = Double.parseDouble("0." + satellite.line2.substring(26, 33));
        satellite.argumentOfPerigee = Double.parseDouble(satellite.line2.substring(34, 42));
        satellite.meanAnomaly = Double.parseDouble(satellite.line2.substring(43, 51));
        satellite.meanMotion = Double.parseDouble(satellite.line2.substring(52, 63));
        satellite.revAtEpoch = Double.parseDouble(satellite.line2.substring(63, 68));
    }

    /**
     * This function returns the current position of the satellite provided TLE
     * @return LatLongLocation object containing the latitude and longitude of the satellite
     */
    public LatLongLocation getCurrentPosition(){
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        AbsoluteDate absoluteDate = new AbsoluteDate(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                TimeScalesFactory.getUTC()
        );
        LatLongLocation position = getLatLongLocation(absoluteDate, propagator, earthFrame, earth);
        positionPoint = new Point(position.longitude, position.latitude);
        pointGraphic = new Graphic(positionPoint, Globals.simpleMarkerSymbol);
        return position;
    }

    static LatLongLocation getLatLongLocation(AbsoluteDate absoluteDate, TLEPropagator propagator, Frame earthFrame, BodyShape earth) {
        PVCoordinates pvCoordinates = propagator.propagate(absoluteDate).getPVCoordinates(earthFrame);
        GeodeticPoint geodeticPoint = earth.transform(pvCoordinates.getPosition(), earthFrame, absoluteDate);
        double actualLatitude = FastMath.toDegrees(geodeticPoint.getLatitude());
        double actualLongitude = FastMath.toDegrees(geodeticPoint.getLongitude());
        return new LatLongLocation(actualLatitude, actualLongitude);
    }

    public double getAverageOrbitTimeMS() {
        return 8.64e+7 / this.meanMotion;
    }
    public LatLongLocation[] getOrbitTrackSync(){
        double orbitTimeMS = getAverageOrbitTimeMS();
        int numberOfPoints = (int) (orbitTimeMS / 10000);
        LatLongLocation[] orbitTrack = new LatLongLocation[numberOfPoints];
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        AbsoluteDate absoluteDate = new AbsoluteDate(
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                TimeScalesFactory.getUTC()
        );
        for (int i = 0; i < numberOfPoints; i++) {
            PVCoordinates pvCoordinates = propagator.propagate(absoluteDate).getPVCoordinates(earthFrame);
            GeodeticPoint geodeticPoint = earth.transform(pvCoordinates.getPosition(), earthFrame, absoluteDate);
            double actualLatitude = FastMath.toDegrees(geodeticPoint.getLatitude());
            double actualLongitude = FastMath.toDegrees(geodeticPoint.getLongitude());
            orbitTrack[i] = new LatLongLocation(actualLatitude, actualLongitude);
            absoluteDate = absoluteDate.shiftedBy(10);
        }
        return orbitTrack;
    }
}
