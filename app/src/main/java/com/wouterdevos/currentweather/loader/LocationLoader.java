package com.wouterdevos.currentweather.loader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationLoader extends Loader<Location> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long LOCATION_REQUEST_INTERVAL = 1000;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ConnectionResult mConnectionResult;

    public LocationLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mLastLocation != null) {
            deliverResult(mLastLocation);
        }

        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (mGoogleApiClient.isConnected()) {
            removeLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (mLastLocation != null) {
            deliverResult(mLastLocation);
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        removeLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (hasPermission()) {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        }

//        if (mLastLocation != null) {
//            deliverResult(mLastLocation);
//        }

        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mConnectionResult = connectionResult;
        // Notify that an error has occurred.
        deliverResult(null);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        deliverResult(location);
        removeLocationUpdates();
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        return locationRequest;
    }

    private void requestLocationUpdates() {
        if (hasPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, getLocationRequest(), this);
        }
    }

    private void removeLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private boolean hasPermission() {
        boolean hasPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        return hasPermission;
    }
}
