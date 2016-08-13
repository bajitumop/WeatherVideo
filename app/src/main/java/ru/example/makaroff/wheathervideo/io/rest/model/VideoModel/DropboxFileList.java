package ru.example.makaroff.wheathervideo.io.rest.model.VideoModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DropboxFileList implements Parcelable {


    List<DropboxFile> files;

    public DropboxFileList(List<DropboxFile> files){
        this.files = files;
    }

    public List<DropboxFile> getFiles() {
        return files;
    }

    protected DropboxFileList(Parcel in) {
    }

    public static final Creator<DropboxFileList> CREATOR = new Creator<DropboxFileList>() {
        @Override
        public DropboxFileList createFromParcel(Parcel in) {
            return new DropboxFileList(in);
        }

        @Override
        public DropboxFileList[] newArray(int size) {
            return new DropboxFileList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
