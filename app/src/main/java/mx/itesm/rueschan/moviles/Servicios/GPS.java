package mx.itesm.rueschan.moviles.Servicios;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Rubén Escalante on 24/04/2018.
 */

public class GPS implements LocationListener {

    private static GPS INSTANCE;
    private static final long MIN_TIME = 3600000; // 1hr
    private static final int MIN_DISTANCE = 2000; // 2000m
    private LocationManager gps;
    private Location posicion;

    public static GPS getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GPS();
        }
        return INSTANCE;
    }

    public GPS() {
        posicion = new Location(LocationManager.GPS_PROVIDER);
    }

    public double getLon() {
        return posicion.getLongitude();
    }

    public double getLat() {
        return posicion.getLatitude();
    }

    @Override
    public void onLocationChanged(Location location) {
        posicion = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
