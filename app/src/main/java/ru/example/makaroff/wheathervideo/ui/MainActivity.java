package ru.example.makaroff.wheathervideo.ui;

import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Locale;

import ru.example.makaroff.wheathervideo.MyApplication;
import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.Dropbox;
import ru.example.makaroff.wheathervideo.io.GeoProvider;
import ru.example.makaroff.wheathervideo.io.rest.Weather;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFile;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFileList;

@EActivity(R.layout.activity_main)
public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnButtonsClick {

    public static int selectedButton = 0;
    public static final int NOTHING_SELECTED = 0;
    public static final int WEATHER_SELECTED = 1;
    public static final int VIDEO_SELECTED = 2;

    private WeatherFragment weatherFragment;
    private MainFragment mainFragment;

    @ViewById
    protected FrameLayout mainContainer;

    @ViewById
    protected FrameLayout rightContainer;

    @ViewById
    protected Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.BUS.register(this);
        selectedButton = NOTHING_SELECTED;
        mainFragment = MainFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, mainFragment).commitAllowingStateLoss();
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
            mainFragment.changeViews(selectedButton);
            setVisibilities();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        init();
    }

    @AfterViews
    protected void init() {
        setVisibilities();
        if (!isPortraitConfiguration()) {
            if (selectedButton == VIDEO_SELECTED) {
                onVideoClick();
            } else {
                onWeatherClick();
            }
        }
    }

    private void setVisibilities() {
        space.setVisibility(isPortraitConfiguration() ? View.GONE : View.VISIBLE);

        if (!isPortraitConfiguration()) {
            mainContainer.setVisibility(View.VISIBLE);
            rightContainer.setVisibility(View.VISIBLE);
        } else {
            if (selectedButton == NOTHING_SELECTED) {
                mainContainer.setVisibility(View.VISIBLE);
                rightContainer.setVisibility(View.GONE);
            } else {
                mainContainer.setVisibility(View.GONE);
                rightContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isPortraitConfiguration() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.rightContainer, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onWeatherClick() {
        selectedButton = WEATHER_SELECTED;
        mainFragment.changeViews(selectedButton);
        if (weatherFragment == null) {
            setFragment(ProgressFragment.newInstance());
            new GeoProvider((LocationManager) getSystemService(LOCATION_SERVICE), onLocationGet);
        } else {
            setFragment(weatherFragment);
        }
        setVisibilities();
    }

    @Subscribe
    protected void onWeatherGet(Weather weather) {
        weatherFragment = WeatherFragment.newInstance(weather);
        if (selectedButton == WEATHER_SELECTED) {
            if (weather.isSuccess()) {
                setFragment(weatherFragment);
            } else {
                setVisibilities();
                setFragment(ErrorFragment.newInstance(String.format(Locale.getDefault(), "%s:\n%s", getString(R.string.errorFromServer), weather.getMessage())));
            }
        }
    }

    @Override
    public void onVideoClick() {
        selectedButton = VIDEO_SELECTED;
        setVisibilities();
        setFragment(ProgressFragment.newInstance());
        mainFragment.changeViews(selectedButton);
        List<DropboxFile> files = Dropbox.getFileList();
        if (files == null) {
            setFragment(ErrorFragment.newInstance("Возникла ошибка поключения к облаку"));
        } else {
            setFragment(VideoFragment.newInstance(new DropboxFileList(files)));
        }
    }

    private GeoProvider.OnLocationGet onLocationGet = new GeoProvider.OnLocationGet() {
        @Override
        public void onLocationGet(Location location) {
            if (selectedButton == WEATHER_SELECTED) {
                setFragment(ProgressFragment.newInstance());
                MyApplication.networkService.getWeather(location.getLatitude(), location.getLongitude());
                Toast.makeText(MainActivity.this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(String message) {
            setFragment(ErrorFragment.newInstance(message));
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected MainActivity(Parcel in) {
    }

    public static final Parcelable.Creator<MainActivity> CREATOR = new Parcelable.Creator<MainActivity>() {
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
