package ru.example.makaroff.wheathervideo.ui;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import ru.example.makaroff.wheathervideo.R;

@EFragment(R.layout.fragment_error)
public class ErrorFragment extends Fragment {

    @FragmentArg
    protected String message;

    public static ErrorFragment newInstance(String inputMessage) {
        return ErrorFragment_.builder().message(inputMessage).build();
    }

    @ViewById
    protected TextView tvErrorConnection;

    @AfterViews
    protected void init(){
        tvErrorConnection.setText(message);
    }
}
