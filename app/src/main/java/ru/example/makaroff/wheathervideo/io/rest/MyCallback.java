package ru.example.makaroff.wheathervideo.io.rest;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.example.makaroff.wheathervideo.MyApplication;

public class MyCallback<T extends Weather> implements Callback<T> {

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
            t.setSuccess(t.getMessage() == null);
            MyApplication.BUS.post(t);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        T t;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e){
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        t.setSuccess(false);
        MyApplication.BUS.post(t);
    }
}