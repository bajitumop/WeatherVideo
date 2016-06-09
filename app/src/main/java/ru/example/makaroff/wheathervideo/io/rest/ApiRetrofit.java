package ru.example.makaroff.wheathervideo.io.rest;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiRetrofit {

    String WEATHER_URL = "http://api.openweathermap.org";

    @GET("/data/2.5/weather")
    void getWeather (@Query("lat") double lat,
                     @Query("lon") double lon,
                     @Query("APPID") String token,
                     MyCallback<Weather> cb);
}
