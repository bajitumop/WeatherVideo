package ru.example.makaroff.wheathervideo.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import ru.example.makaroff.wheathervideo.MyApplication;
import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.Utilits.EventForMainFragment;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EActivity(R.layout.activity_main)
public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnButtonsClick {

    @ViewById
    protected FrameLayout mainContainer;

    @ViewById
    protected FrameLayout rightContainer;

    @ViewById
    protected Space space;

    protected Weather weather;

    private CountDownTimer countDownTimer;

    protected Date timeToUpdate;

    // 0 - Для неактивных разделов
    // 1 - Для активного раздела погоды
    // 2 - Для активного раздела видео
    public static int selectedButton = 0;
    public static final int NOTHING_SELECTED = 0;
    public static final int WEATHER_SELECTED = 1;
    public static final int VIDEO_SELECTED = 2;
    private boolean isVisibleOnScreen;

    //////////////////////// do another time ///////////////////////////////////
    public static final long TIME_REPEAT_WEATHER_REQUEST = 10 * 1000;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initializeFragments(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisibleOnScreen = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisibleOnScreen = true;
        if (selectedButton == WEATHER_SELECTED) {
            checkTimeToUpdateAndGetWeather();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeFragment(R.id.mainContainer, MainFragment.newInstance(this));
        selectedButton = NOTHING_SELECTED;
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

    @AfterViews
    protected void init() {
        MyApplication.BUS.register(this);
        initializeFragments(isPortraitConfiguration());
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

        checkTimeToUpdateAndGetWeather();
    }

    private void checkTimeToUpdateAndGetWeather(){

        //Блок определения координат
        double lat = 54.1838;
        double lon = 45.1749;

        if (countDownTimer != null) {
            setWeatherFragment();
        } else {
            MyApplication.networkService.getWeather(lat, lon);
        }
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
        setErrorFragment(null);
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
            setErrorFragment(weather.getMessage());
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
