package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.rest.Weather;

@EFragment(R.layout.fragment_weather)
public class WeatherFragment extends Fragment {

    private final short WEATHER_POSITION = 0;
    private final String URL_START = "http://openweathermap.org/img/w/";
    private final String URL_END = ".png";

    @FragmentArg
    protected Weather weather;

    public static WeatherFragment newInstance(Weather inputWeather) {
        return WeatherFragment_.builder().weather(inputWeather).build();
    }

    @ViewById
    protected TextView tvTempWeather;

    @ViewById
    protected TextView tvTempDiffWeather;

    @ViewById
    protected TextView tvCityWeather;

    @ViewById
    protected TextView tvDescriptionWeather;

    @ViewById
    protected ImageView ivIconWeather;

    @AfterViews
    protected void init() {
        tvTempWeather.setText(String.format(Locale.getDefault(), "%d %cC", weather.getMain().getTemp(), (char) 0x00B0));
        tvTempDiffWeather.setText(String.format(
                Locale.getDefault(),
                "%d %cC - %d %cC",
                weather.getMain().getMinTemp(),
                (char) 0x00B0,
                weather.getMain().getMaxTemp(),
                (char) 0x00B0
                )
        );
        tvCityWeather.setText(weather.getName());
        tvDescriptionWeather.setText(weather.getWeatherBlockAtPosition(WEATHER_POSITION).getDescription());
        String url = String.format(
                Locale.getDefault(),
                "%s%s%s",
                URL_START,
                weather.getWeatherBlockAtPosition(WEATHER_POSITION).getIcon(),
                URL_END
        );
        Picasso.with(getContext()).load(url).into(ivIconWeather);
    }


}
