package ru.example.makaroff.wheathervideo;

import android.app.Application;

import org.androidannotations.annotations.EApplication;
import org.greenrobot.eventbus.EventBus;

@EApplication
public class MyApplication extends Application {

    public final static EventBus BUS = new EventBus();
}
