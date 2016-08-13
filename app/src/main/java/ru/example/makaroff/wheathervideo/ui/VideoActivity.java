package ru.example.makaroff.wheathervideo.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import ru.example.makaroff.wheathervideo.R;

@EActivity(R.layout.activity_video)
public class VideoActivity extends AppCompatActivity{

    @Extra
    protected String path;

    public static void show(Context context, String path ){
        VideoActivity_.intent(context).path(path).start();
    }

    @ViewById
    protected VideoView videoView;

    @AfterViews
    protected void play(){
        videoView.setVideoPath(path);
        videoView.start();
    }

}
