package ru.example.makaroff.wheathervideo;

import android.app.Application;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.greenrobot.eventbus.EventBus;

import ru.example.makaroff.wheathervideo.io.NetworkService;

@EApplication
public class MyApplication extends Application {

    public final static EventBus BUS = new EventBus();

    @Bean
    public static NetworkService networkService;
}
