package ru.example.makaroff.wheathervideo.io.rest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import ru.example.makaroff.wheathervideo.MyApplication;

public class MyCallback<T> implements Callback<T> {

    private Class<T> clazz;

    public Class<T> getClazz() {
        return clazz;
    }

    public MyCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void success(T t, Response response) {
        if (t != null) {
            MyApplication.BUS.post(t);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("TAG", "error loading");
    }
}