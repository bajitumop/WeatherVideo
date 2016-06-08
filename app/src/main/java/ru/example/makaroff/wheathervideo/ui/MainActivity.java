package ru.example.makaroff.wheathervideo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru.example.makaroff.wheathervideo.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    protected Button btnVideo;

    @AfterViews
    protected void init(){
        btnVideo.setText("lol");
    }
}
