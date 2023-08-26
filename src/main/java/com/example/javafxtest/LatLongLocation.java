package com.example.javafxtest;

import com.esri.arcgisruntime.geometry.Point;

public class LatLongLocation {
    public double latitude;
    public double longitude;

    public LatLongLocation() {
        latitude = 0;
        longitude = 0;
    }

    public LatLongLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{latitude=" + latitude +", longitude=" + longitude + "}";
    }

    public double distanceTo(Point point) {
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(point.getY());
        double lon1 = Math.toRadians(longitude);
        double lon2 = Math.toRadians(point.getX());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;

        return(c * r);
    }
}