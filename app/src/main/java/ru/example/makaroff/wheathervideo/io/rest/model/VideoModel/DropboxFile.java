package ru.example.makaroff.wheathervideo.io.rest.model.VideoModel;

import android.os.Parcel;
import android.os.Parcelable;

public class DropboxFile implements Parcelable{

    public static final int NOT_LOADED = 0;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOADED = 2;

    private String name;
    private int code;

    public DropboxFile(String name, int code) {
        this.name = name;
        this.code = code;
    }

    protected DropboxFile(Parcel in) {
        name = in.readString();
        code = in.readInt();
    }

    public static final Creator<DropboxFile> CREATOR = new Creator<DropboxFile>() {
        @Override
        public DropboxFile createFromParcel(Parcel in) {
            return new DropboxFile(in);
        }

        @Override
        public DropboxFile[] newArray(int size) {
            return new DropboxFile[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(code);
    }
}
