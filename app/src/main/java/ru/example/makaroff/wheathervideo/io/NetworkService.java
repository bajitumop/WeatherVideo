package ru.example.makaroff.wheathervideo.io;

import com.squareup.okhttp.OkHttpClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.example.makaroff.wheathervideo.io.rest.MyCallback;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EBean(scope = EBean.Scope.Singleton)
public class NetworkService {

    String WEATHER_URL = "http://api.openweathermap.org";

    public interface ApiRetrofit {
        @GET("/data/2.5/weather")
        void getWeather (@Query("lat") double lat,
                         @Query("lon") double lon,
                         @Query("APPID") String token,
                         MyCallback<Weather> cb);
    }

    protected ApiRetrofit service;

    @AfterInject
    protected void init() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEATHER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
        service = restAdapter.create(ApiRetrofit.class);
    }

    public void getWeather(double lat, double lon) {
        service.getWeather(lat, lon, "bccb923f56b2eb578252e27b401a1eb9", new MyCallback<>(Weather.class));
    }
}
