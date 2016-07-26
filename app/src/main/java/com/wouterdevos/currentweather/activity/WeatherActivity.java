package com.wouterdevos.currentweather.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wouterdevos.currentweather.R;
import com.wouterdevos.currentweather.databinding.ActivityWeatherBinding;
import com.wouterdevos.currentweather.fragment.PermissionDialogFragment;
import com.wouterdevos.currentweather.presenter.WeatherContract;
import com.wouterdevos.currentweather.presenter.WeatherPresenter;
import com.wouterdevos.currentweather.valueobject.Error;
import com.wouterdevos.currentweather.valueobject.Weather;
import com.wouterdevos.currentweather.service.LocationService;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    public static final String TAG_PERMISSION_DIALOG_FRAGMENT = "TAG_PERMISSION_DIALOG_FRAGMENT";

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 0;

    public static final int ID_LOCATION_LOADER = 0;

    private boolean mRequestPermissionOnResume = true;

    private WeatherPresenter mWeatherPresenter;
    private ActivityWeatherBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
//        setContentView(R.layout.activity_weather);
        mWeatherPresenter = new WeatherPresenter(this, getSupportLoaderManager(), getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWeatherPresenter.registerEventBus();
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

    @Override
    protected void onStop() {
        super.onStop();
        mWeatherPresenter.unregisterEventBus();
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        mBinding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeather(Weather weather) {
        Log.i(TAG, "showWeather: weatherResponse " + weather);
        boolean isWeather = weather != null;
        mBinding.description.setVisibility(isWeather ? View.VISIBLE : View.GONE);
        mBinding.temperature.setVisibility(isWeather ? View.VISIBLE : View.GONE);
        mBinding.highAndLow.setVisibility(isWeather ? View.VISIBLE : View.GONE);
        mBinding.icon.setVisibility(isWeather ? View.VISIBLE : View.GONE);

        mBinding.description.setText(weather.getMain());
        mBinding.temperature.setText(weather.getFormattedTemp());
        mBinding.highAndLow.setText(weather.getFormattedHighAndLow());
        Glide.with(this)
                .load(weather.getIconUrl())
                .centerCrop()
                .crossFade()
                .into(mBinding.icon);
    }

    @Override
    public void showError(Error error) {
        Toast.makeText(this, R.string.toast_error_retrieving_weather, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectivityStatus(boolean online) {

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
            mWeatherPresenter.loadCurrentWeather();
//            getSupportLoaderManager().initLoader(ID_LOCATION_LOADER, null, mLocationLoaderCallback);
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
