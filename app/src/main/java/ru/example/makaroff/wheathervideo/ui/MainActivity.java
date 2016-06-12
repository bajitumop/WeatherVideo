package ru.example.makaroff.wheathervideo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.Locale;

import ru.example.makaroff.wheathervideo.MyApplication;
import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.Utilits.EventForMainFragment;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EActivity(R.layout.activity_main)
public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnButtonsClick {

    public static int selectedButton = 0;
    public static final int NOTHING_SELECTED = 0;
    public static final int WEATHER_SELECTED = 1;
    public static final int VIDEO_SELECTED = 2;

    private static final long TIME_REPEAT_WEATHER_REQUEST = 1000 * 60 * 10;
    private static final long MIN_TIME = 1000 * 60; //1 мин
    private static final long MIN_DISTANCE = 1000 * 10; // 10 км

    @ViewById
    protected FrameLayout mainContainer;

    @ViewById
    protected FrameLayout rightContainer;

    @ViewById
    protected Space space;

    private Weather weather;

    private CountDownTimer countDownTimer;

    private Date timeToUpdate;

    private LocationManager locationManager;

    private Location lastKnownLocation;

    private boolean mayGetLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.BUS.register(this);
        changeFragment(R.id.mainContainer, MainFragment.newInstance(this));
        selectedButton = NOTHING_SELECTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedButton == WEATHER_SELECTED) {
            if (countDownTimer == null) {
                initLocation();
            } else {
                setWeatherFragment();
            }
        }
    }

    @Override
    protected void onPause() {
        locationManager = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MyApplication.BUS.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isPortraitConfiguration() && selectedButton != NOTHING_SELECTED) {
            selectedButton = NOTHING_SELECTED;
            space.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
            rightContainer.setVisibility(View.GONE);
            MyApplication.BUS.post(new EventForMainFragment());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeFragments(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @AfterViews
    protected void init() {
        initializeFragments(isPortraitConfiguration());
    }

    private void initializeFragments(boolean isPortrait) {
        if (isPortrait) {
            if (selectedButton == NOTHING_SELECTED) {
                space.setVisibility(View.GONE);
                rightContainer.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
            } else {
                space.setVisibility(View.GONE);
                rightContainer.setVisibility(View.VISIBLE);
                mainContainer.setVisibility(View.GONE);
            }
        } else {
            if (selectedButton == NOTHING_SELECTED) {
                selectedButton = WEATHER_SELECTED;
            }
            space.setVisibility(View.VISIBLE);
            rightContainer.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.VISIBLE);
            switch (selectedButton) {
                case WEATHER_SELECTED:
                    getWeather();
                    break;
                case VIDEO_SELECTED:
                    getVideo();
                    break;
                default:
                    throw new RuntimeException("wrong value of selectedButton in MainActivity");
            }
        }
    }

    private void getWeather() {
        selectedButton = WEATHER_SELECTED;
        MyApplication.BUS.post(new EventForMainFragment());
        setProgressFragment();
        if (isPortraitConfiguration()) {
            space.setVisibility(View.GONE);
            mainContainer.setVisibility(View.GONE);
            rightContainer.setVisibility(View.VISIBLE);
        }
        if (countDownTimer != null) {
            setWeatherFragment();
        } else {
            initLocation();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (selectedButton == WEATHER_SELECTED) {
                if (countDownTimer != null) {
                    setWeatherFragment();
                } else {
                    requestWeather(location);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {
            mayGetLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onProviderDisabled(String provider) {
            mayGetLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    };

    private void initLocation() {
        if (locationManager == null) {
            try {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (lastKnownLocation == null)
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation == null)
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation == null)
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            } catch (SecurityException e) {
                if (lastKnownLocation == null) {
                    locationManager = null;
                    showAlertDialog();
                } else {
                    Snackbar.make(mainContainer,
                            getString(R.string.locationPermissionsDenied),
                            Snackbar.LENGTH_SHORT);
                }
            }
        }
        if (!mayGetLocation) {
            if (lastKnownLocation == null) {
                setErrorFragment(getString(R.string.locationIsOff));
            } else {
                Snackbar.make(mainContainer,
                        getString(R.string.locationAsLastKnown),
                        Snackbar.LENGTH_SHORT);
                requestWeather(lastKnownLocation);
            }
        }
    }

    private void requestWeather(Location location){
        MyApplication.networkService.getWeather(location.getLatitude(), location.getLongitude());
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.errorLocationTitle));
        builder.setMessage(getString(R.string.errorLocationMessage));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setErrorFragment(getString(R.string.errorLocation));
            }
        });
    }

    private void getVideo() {
        selectedButton = VIDEO_SELECTED;
        MyApplication.BUS.post(new EventForMainFragment());
        setProgressFragment();
        if (isPortraitConfiguration()) {
            space.setVisibility(View.GONE);
            mainContainer.setVisibility(View.GONE);
            rightContainer.setVisibility(View.VISIBLE);
        } else {
            MyApplication.BUS.post(new EventForMainFragment());
        }
        setErrorFragment(getString(R.string.errorConnection));
    }

    @Subscribe
    protected void onWeatherGet(Weather weather) {
        if (weather.isSuccess()) {
            this.weather = weather;
            timeToUpdate = new Date();
            setWeatherFragment();
            countDownTimer = new CountDownTimer(TIME_REPEAT_WEATHER_REQUEST, TIME_REPEAT_WEATHER_REQUEST) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    countDownTimer = null;
                    if (selectedButton == WEATHER_SELECTED) {
                        getWeather();
                    }
                }
            }.start();
        } else {
            setErrorFragment(String.format(Locale.getDefault(), "%s:\n%s", getString(R.string.errorFromServer), weather.getMessage()));
        }
    }

    private void setProgressFragment() {
        changeFragment(R.id.rightContainer, ProgressFragment.newInstance());
    }

    private void setErrorFragment(String message) {
        changeFragment(R.id.rightContainer, ErrorFragment.newInstance(message));
    }

    private void setWeatherFragment() {
        changeFragment(R.id.rightContainer, WeatherFragment.newInstance(weather, timeToUpdate));
    }

    private boolean isPortraitConfiguration() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void changeFragment(int resId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(resId, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onWeatherClick() {
        getWeather();
    }

    @Override
    public void onVideoClick() {
        getVideo();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected MainActivity(Parcel in) {
    }

    public static final Creator<MainActivity> CREATOR = new Creator<MainActivity>() {
        @Override
        public MainActivity createFromParcel(Parcel in) {
            return new MainActivity(in);
        }

        @Override
        public MainActivity[] newArray(int size) {
            return new MainActivity[size];
        }
    };

    protected MainActivity() {
    }
}
