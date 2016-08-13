package ru.example.makaroff.wheathervideo.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.Dropbox;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFile;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFileList;

@EFragment(R.layout.fragment_video)
public class VideoFragment extends Fragment implements VideoAdapter.OnElementListClick {


    @FragmentArg
    DropboxFileList files;

    public static VideoFragment newInstance(DropboxFileList files) {
        return VideoFragment_.builder().files(files).build();
    }

    @ViewById
    protected RecyclerView recyclerView;

    VideoAdapter videoAdapter;

    @AfterViews
    protected void init() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        videoAdapter = new VideoAdapter(this, files.getFiles());
        recyclerView.setAdapter(videoAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void changeElement(DropboxFile file) {
        videoAdapter.changeElement(file);
    }

    @Override
    public void onElementListClick(DropboxFile file) {
        switch(file.getCode()){
            case DropboxFile.NOT_LOADED:
                file.setCode(DropboxFile.DOWNLOADED);
                changeElement(file);
                Dropbox.downloadFile(file.getName());
                break;
            case DropboxFile.DOWNLOADED:
                VideoActivity.show(getContext(), "/storage/sdcard0/201.mp4");
                break;
        }
    }

}
