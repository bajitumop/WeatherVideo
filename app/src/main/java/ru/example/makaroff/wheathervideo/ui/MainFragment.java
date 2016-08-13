package ru.example.makaroff.wheathervideo.ui;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

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
    protected TextView tvSelectChapter;

    @ViewById
    protected Button btnVideo;

    @ViewById
    protected Button btnWeather;

    @Click(R.id.btnWeather)
    protected void getWeather(){
        onButtonsClick.onWeatherClick();
    }

    @Click(R.id.btnVideo)
    protected void getVideo(){
       onButtonsClick.onVideoClick();
    }

    public void changeViews (int flagActiveButton){
        String textForTextView = getString(R.string.selectedChapter);
        switch(flagActiveButton){
            case MainActivity.WEATHER_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_active);
                btnVideo.setBackgroundResource(R.drawable.shape_button_inactive);
                tvSelectChapter.setText(String.format(Locale.getDefault(), "%s\n%s", textForTextView, getString(R.string.weather)));
                break;
            case MainActivity.VIDEO_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_inactive);
                btnVideo.setBackgroundResource(R.drawable.shape_button_active);
                tvSelectChapter.setText(String.format(Locale.getDefault(), "%s\n%s", textForTextView, getString(R.string.video)));
                break;
            case MainActivity.NOTHING_SELECTED:
                btnWeather.setBackgroundResource(R.drawable.shape_button_inactive);
                btnVideo.setBackgroundResource(R.drawable.shape_button_inactive);
                tvSelectChapter.setText(getString(R.string.selectChapter));
                break;
        }
    }

    public interface OnButtonsClick extends Parcelable {
        void onWeatherClick();
        void onVideoClick();
    }
}
