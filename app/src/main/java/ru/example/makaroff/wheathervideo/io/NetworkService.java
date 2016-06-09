package ru.example.makaroff.wheathervideo.io;

import com.squareup.okhttp.OkHttpClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import ru.example.makaroff.wheathervideo.io.rest.ApiRetrofit;
import ru.example.makaroff.wheathervideo.io.rest.MyCallback;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EBean(scope = EBean.Scope.Singleton)
public class NetworkService {

    protected ApiRetrofit service;

    @AfterInject
    protected void init() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiRetrofit.WEATHER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
        service = restAdapter.create(ApiRetrofit.class);
    }

    public void getWeather(double lat, double lon) {
        service.getWeather(lat, lon, "bccb923f56b2eb578252e27b401a1eb9", new MyCallback<>(Weather.class));
    }

}
