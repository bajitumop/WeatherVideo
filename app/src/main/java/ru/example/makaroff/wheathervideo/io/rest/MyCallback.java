package ru.example.makaroff.wheathervideo.io.rest;

import org.greenrobot.eventbus.EventBus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.example.makaroff.wheathervideo.MyApplication;

public class MyCallback<T> implements Callback<T> {

    private Class<T> clazz;

    public Class<T> getClazz() {
        return clazz;
    }

    private EventBus eventBus;

    public MyCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void success(T t, Response response) {
        if (t != null) {
            sendResponse(t);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        T body;

        if (error.getResponse() != null) {
            try {
                body = (T) error.getBodyAs(clazz);
                sendResponse(body);
            } catch (Exception c) {
                try {
                    body = clazz.newInstance();
                    sendResponse(body);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                sendResponse(clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException c) {
                c.printStackTrace();
            }
        }
    }

    private void sendResponse(T body) {
        if (eventBus == null) {
            MyApplication.BUS.post(body);
        } else {
            eventBus.post(body);
        }
    }
}