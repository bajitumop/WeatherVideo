package ru.example.makaroff.wheathervideo.ui;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.Locale;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

    @FragmentArg
    OnButtonsClick onButtonsClick;

    public static MainFragment newInstance(OnButtonsClick inputOnButtonsClick){
        return MainFragment_.builder().onButtonsClick(inputOnButtonsClick).build();
    }

    @ViewById
    TextView tvSelectChapter;

    @ViewById
    protected Button btnVideo;

    @ViewById
    protected Button btnWeather;

    @Click(R.id.btnWeather)
    protected void getWeather(){
        initializeData(MainActivity.WEATHER_SELECTED);
        onButtonsClick.onWeatherClick();
    }

    @Click(R.id.btnVideo)
    protected void getVideo(){
        initializeData(MainActivity.VIDEO_SELECTED);
        onButtonsClick.onVideoClick();
    }

    @AfterViews
    protected void init(){
        initializeData(MainActivity.selectedButton);
    }

    private void initializeData (int flagActiveButton){
        String textForTextView = getString(R.string.SelectedChapter);
        switch(flagActiveButton){
            case MainActivity.WEATHER_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_active);
                btnVideo.setBackgroundResource(R.drawable.shape_button_inactive);
                tvSelectChapter.setText(String.format(Locale.getDefault(), "%s %s", textForTextView, getString(R.string.weather)));
                break;
            case MainActivity.VIDEO_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_inactive);
                btnVideo.setBackgroundResource(R.drawable.shape_button_active);
                tvSelectChapter.setText(String.format(Locale.getDefault(), "%s %s", textForTextView, getString(R.string.video)));
                break;
            case MainActivity.NOTHING_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_inactive);
                btnVideo.setBackgroundResource(R.drawable.shape_button_inactive);
                break;
        }
    }

    interface OnButtonsClick extends Serializable {
        void onWeatherClick();
        void onVideoClick();
    }
}
