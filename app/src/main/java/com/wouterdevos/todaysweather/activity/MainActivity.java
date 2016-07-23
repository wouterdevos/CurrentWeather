package com.wouterdevos.todaysweather.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wouterdevos.todaysweather.R;
import com.wouterdevos.todaysweather.fragment.PermissionDialogFragment;
import com.wouterdevos.todaysweather.loader.LocationLoader;
import com.wouterdevos.todaysweather.model.WeatherModel;
import com.wouterdevos.todaysweather.service.LocationService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String TAG_PERMISSION_DIALOG_FRAGMENT = "TAG_PERMISSION_DIALOG_FRAGMENT";

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 0;

    public static final int ID_LOCATION_LOADER = 0;

    private boolean mRequestPermissionOnResume = true;

    private WeatherModel mWeatherModel;

    private LoaderManager.LoaderCallbacks<Location> mLocationLoaderCallback =
            new LoaderManager.LoaderCallbacks<Location>() {
        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return new LocationLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {
            // Request weather from WeatherModel.
            Log.i(TAG, "onLoadFinished: data " + data);
            mWeatherModel.getWeatherByCoordinates(data);
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherModel = WeatherModel.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mRequestPermissionOnResume) {
//            onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION_PERMISSION);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mRequestPermissionOnResume = true;
    }

    //region Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleRequestPermission(requestCode, permissions, grantResults);
    }

    private void onRequestPermission(String permission, int requestCode) {
        if (!hasPermission(permission, requestCode)) {
            return;
        }

        String[] permissions = {permission};
        int[] grantResults = {PackageManager.PERMISSION_GRANTED};
        handleRequestPermission(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handleRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_LOCATION_PERMISSION != requestCode) {
            return;
        }

        mRequestPermissionOnResume = false;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSupportLoaderManager().initLoader(ID_LOCATION_LOADER, null, mLocationLoaderCallback);
//            startLocationService();
        } else {
//            String permission = permissions[0];
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                showRequestPermissionRationaleDialog(false);
//            } else {
//                showRequestPermissionRationaleDialog(true);
//            }
        }
    }

    private boolean hasPermission(String permission, int requestCode) {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }

        return true;
    }

    private void showRequestPermissionRationaleDialog(boolean neverShowAgain) {
        PermissionDialogFragment permissionDialogFragment = PermissionDialogFragment.newInstance(neverShowAgain);
        permissionDialogFragment.show(getSupportFragmentManager(), TAG_PERMISSION_DIALOG_FRAGMENT);
    }
    //endregion

    public void refresh(View view) {
        onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION_PERMISSION);
    }

    private void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }
}
