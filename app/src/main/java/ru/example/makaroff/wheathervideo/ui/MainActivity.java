package ru.example.makaroff.wheathervideo.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import ru.example.makaroff.wheathervideo.MyApplication;
import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.NetworkService;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Bean
    protected NetworkService networkService;

    private double lat;
    private double lon;

    @Click(R.id.btnWeather)
    protected void getWeather(){
        networkService.getWeather(lat, lon);
    }

    @ViewById
    protected Button btnVideo;

    @ViewById
    protected Button btnWeather;

    @Click(R.id.btnVideo)
    protected void getVideo(){
        btnVideo.setText("lol");
    }

    @AfterViews
    protected void init(){
        MyApplication.BUS.register(this);
        lat = 54.11;
        lon = 45.11;
    }

    @Subscribe
    protected void onWeatherGet(Weather weather){
        btnWeather.setText(weather.getCod());
    }
}
