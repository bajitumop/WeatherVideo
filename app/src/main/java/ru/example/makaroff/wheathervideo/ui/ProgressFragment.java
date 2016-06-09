package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_progress)
public class ProgressFragment extends Fragment {
    public static ProgressFragment newInstance(){
        return ProgressFragment_.builder().build();
    }
}
