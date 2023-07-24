package com.sanskrit.pmo.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.callbacks.RequestTokenListener;
import com.sanskrit.pmo.network.server.models.RequestToken;
import com.sanskrit.pmo.utils.Utils;

import java.util.Arrays;

import io.fabric.sdk.android.services.network.HttpRequest;
import retrofit.client.Response;

public class LocationUtils {
    Context context;
    double latitude = 0.0d;
    LocationListener locationListenerGps = new C13431();
    LocationListener locationListenerNetwork = new C13442();
    LocationManager locationManager;
    int locationShifted = 0;
    double longitude = 0.0d;

    class C13431 implements LocationListener {
        C13431() {
        }

        public void onLocationChanged(Location location) {
            LocationUtils.this.latitude = location.getLatitude();
            LocationUtils.this.longitude = location.getLongitude();
            try {
                LocationUtils.this.getToken(String.valueOf(LocationUtils.this.latitude).toCharArray(), String.valueOf(LocationUtils.this.longitude).toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                LocationUtils.this.locationManager.removeUpdates(this);
                LocationUtils.this.locationManager.removeUpdates(LocationUtils.this.locationListenerNetwork);
            } catch (SecurityException e2) {
                e2.printStackTrace();
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class C13442 implements LocationListener {
        C13442() {
        }

        public void onLocationChanged(Location location) {
            LocationUtils.this.latitude = location.getLatitude();
            LocationUtils.this.longitude = location.getLongitude();
            try {
                LocationUtils.this.getToken(String.valueOf(LocationUtils.this.latitude).toCharArray(), String.valueOf(LocationUtils.this.longitude).toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                LocationUtils.this.locationManager.removeUpdates(this);
                LocationUtils.this.locationManager.removeUpdates(LocationUtils.this.locationListenerGps);
            } catch (SecurityException e2) {
                e2.printStackTrace();
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class C13464 implements ResponseListener {
        C13464() {
        }

        public void success(Response response) {
            Log.d(HttpRequest.HEADER_LOCATION, "Location has been updated");
            PreferencesUtility.setLastLocationTime(LocationUtils.this.context, System.currentTimeMillis());
        }

        public void failure() {
        }
    }

    public static LocationUtils getInstance(Context context) {
        LocationUtils locationUtils = new LocationUtils();
        locationUtils.context = context;
        return locationUtils;
    }

    public void updateUserLocation() {
        if (this.context == null) {
            return;
        }
        if (Utils.isMarshmallow()) {
            //checkPermissionAndThenGetLocation();
        } else {
            setCurrentLocation();
        }
    }

    public void clearUserLocation() {
        if (this.context != null) {
            getToken(String.valueOf(0).toCharArray(), String.valueOf(0).toCharArray());
        }
    }

    @SuppressLint("MissingPermission")
    private void setCurrentLocation() {
        this.locationManager = (LocationManager) this.context.getSystemService("location");
        if (this.locationManager.isProviderEnabled("gps")) {
            try {
                this.locationManager.requestLocationUpdates("gps", 1000, 0.0f, this.locationListenerGps);
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (SecurityException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1000, 0.0f, this.locationListenerNetwork);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndThenGetLocation() {
        if (Nammu.checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
            setCurrentLocation();
        }
    }*/

    private void getToken(final char[] lati, final char[] longi) {
        try {
            SanskritClient.getInstance(this.context).getRequestToken(Utils.getPermaToken(this.context), new RequestTokenListener() {
                public void success(RequestToken token) {
                    if (token.mErrorResponse == null) {
                        LocationUtils.this.updateLocation(token.mToken, lati, longi);
                    }
                }

                public void failure() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLocation(String token, char[] lati, char[] loni) {
        try {
            SanskritClient.getInstance(this.context).setUserLocation(token, String.valueOf(lati), String.valueOf(loni), new C13464());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Arrays.fill(lati, '0');
        Arrays.fill(loni, '0');
    }
}
