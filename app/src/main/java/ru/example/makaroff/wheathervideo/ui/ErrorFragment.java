package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_error)
public class ErrorFragment extends Fragment {

    @FragmentArg
    protected String message;

    public static ErrorFragment newInstance(String message) {
        return ErrorFragment_.builder().message(message).build();
    }

    @ViewById
    protected TextView tvErrorConnection;

    @AfterViews
    public void setMessage(){
        tvErrorConnection.setText(message);
    }
}
