package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_error)
public class ErrorFragment extends Fragment {
    public static ErrorFragment newInstance() {
        return ErrorFragment_.builder().build();
    }
}
