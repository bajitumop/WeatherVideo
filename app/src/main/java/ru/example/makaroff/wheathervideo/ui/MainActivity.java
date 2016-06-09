package ru.example.makaroff.wheathervideo.ui;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.greenrobot.eventbus.Subscribe;

import ru.example.makaroff.wheathervideo.MyApplication;
import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.NetworkService;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainFragment.OnButtonsClick {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.BUS.unregister(this);
    }

    @Bean
    protected NetworkService networkService;

    @InstanceState
    Weather weather;

    public static int selectedButton = 0;
    public static final int WEATHER_SELECTED = 0;
    public static final int VIDEO_SELECTED = 1;

    private double lat;
    private double lon;

    @AfterViews
    protected void init(){
        MyApplication.BUS.register(this);
        changeFragment(R.id.mainContainer, MainFragment.newInstance(this));
        if (!isPortraitConfiguration()) {
            getWeather();
        }
    }

    private void getWeather() {
        if (isPortraitConfiguration()) {
            changeFragment(R.id.mainContainer, ProgressFragment.newInstance());
        } else {
            changeFragment(R.id.rightContainer, ProgressFragment.newInstance());
        }

        //Блок определения координат
        lat = 54.11;        lon = 45.11;
        networkService.getWeather(lat, lon);
    }

    private void getVideo() {
    }

    @Subscribe
    protected void onWeatherGet(Weather weather){
        this.weather = weather;
        if (weather.isSuccess()) {
            if (isPortraitConfiguration()) {
                changeFragment(R.id.mainContainer, WeatherFragment.newInstance(weather));
            } else {
                changeFragment(R.id.rightContainer, WeatherFragment.newInstance(weather));
            }
        } else {
            if (isPortraitConfiguration()) {
                changeFragment(R.id.mainContainer, ErrorFragment.newInstance());
            } else {
                changeFragment(R.id.rightContainer, ErrorFragment.newInstance());
            }
        }
    }

    private boolean isPortraitConfiguration() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private void changeFragment(int resId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(resId, fragment).commit();
    }

    @Override
    public void onWeatherClick() {
        getWeather();
    }

    @Override
    public void onVideoClick() {
        getVideo();
    }
}
