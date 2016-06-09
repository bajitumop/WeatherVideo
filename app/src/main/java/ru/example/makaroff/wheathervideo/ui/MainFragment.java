package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.io.Serializable;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    @FragmentArg
    OnButtonsClick onButtonsClick;

    public static MainFragment newInstance(OnButtonsClick inputOnButtonsClick){
        return MainFragment_.builder().onButtonsClick(inputOnButtonsClick).build();
    }

    @Click(R.id.btnWeather)
    protected void getWeather(){
        onButtonsClick.onWeatherClick();
    }

    @Click(R.id.btnVideo)
    protected void getVideo(){
        onButtonsClick.onVideoClick();
    }

    interface OnButtonsClick extends Serializable {
        void onWeatherClick();
        void onVideoClick();
    }
}
