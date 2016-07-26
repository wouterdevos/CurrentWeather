package com.wouterdevos.currentweather.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    public static final String TAG_PERMISSION_DIALOG_FRAGMENT = "TAG_PERMISSION_DIALOG_FRAGMENT";

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 0;

    private boolean mRequestPermissionOnResume = true;

    private WeatherPresenter mWeatherPresenter;
    private ActivityWeatherBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        setSupportActionBar(mBinding.toolbar);

        mWeatherPresenter = new WeatherPresenter(this, getSupportLoaderManager(), getApplicationContext());
        mWeatherPresenter.startWeatherLoader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWeatherPresenter.registerEventBus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestPermissionOnResume) {
            onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestPermissionOnResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWeatherPresenter.unregisterEventBus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_weather, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                onRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION_PERMISSION);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        mBinding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeather(Weather weather) {
        if (weather != null) {
            mBinding.setWeather(weather);
            Glide.with(this)
                    .load(weather.getIconUrl())
                    .centerCrop()
                    .crossFade()
                    .into(mBinding.icon);
        }
    }

    @Override
    public void showWeatherError(Error error) {
        Toast.makeText(this, R.string.toast_error_connecting_to_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLocationError() {
        Toast.makeText(this, R.string.toast_error_retrieving_location, Toast.LENGTH_SHORT).show();
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
            mWeatherPresenter.startLocationLoader();
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
}
