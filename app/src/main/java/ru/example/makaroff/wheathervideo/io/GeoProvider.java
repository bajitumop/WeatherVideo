package ru.example.makaroff.wheathervideo.io;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Date;

public class GeoProvider {

    private static final long NEXT_UPDATE_TIME = 1000*60;
    private static final long MIN_TIME = 0;
    private static final float MIN_DISTANCE = 0;
    private static long lastUpdateTime = -NEXT_UPDATE_TIME;

    private LocationManager locationManager;
    private OnLocationGet onLocationGet;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (new Date().getTime() > NEXT_UPDATE_TIME + lastUpdateTime) {
                lastUpdateTime = new Date().getTime();
                onLocationGet.onLocationGet(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (allProvidersDisabled()) {
                onLocationGet.onError("Вы отключили все провайдеры. Местоположение не может быть определено.");
            }
        }
    };

    public GeoProvider(LocationManager locationManager, OnLocationGet onLocationGet) {
        this.onLocationGet = onLocationGet;
        this.locationManager = locationManager;

        if (!checkPermissions()) {
            onLocationGet.onError("Установлен запрет на использование GPS и мобильных сетей");
            return;
        }

        if (allProvidersDisabled()) {
            onLocationGet.onError("GPS и мобильные данные отключены, невозможно получить координаты");
        }
    }

    private boolean checkPermissions() {
        boolean permissionsRejected = false;
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } catch (SecurityException | IllegalArgumentException e) {
            permissionsRejected = true;
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } catch (SecurityException | IllegalArgumentException e) {
            if (permissionsRejected) {
                return false;
            }
        }
        return true;
    }

    private boolean allProvidersDisabled() {
        return !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public static boolean isGetLocation(){
        return lastUpdateTime > 0;
    }

    public static long getUpdateTime(){
        return lastUpdateTime + NEXT_UPDATE_TIME;
    }

    public interface OnLocationGet {
        void onLocationGet(Location location);

        void onError(String string);
    }
}
