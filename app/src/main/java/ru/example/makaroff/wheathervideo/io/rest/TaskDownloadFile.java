package ru.example.makaroff.wheathervideo.io.rest;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TaskDownloadFile extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {

        String fileName = params[0];

        DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxAPI<>(buildSession());
        File file = new File(fileName.substring(fileName.lastIndexOf('.')));
        if (file.exists())
            file.delete();
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            dropboxApi.getFile(fileName, null, outputStream, null);
        } catch (FileNotFoundException e) {
            return false;
        } catch (DropboxException e){
            return false;
        }

        return true;
    }

    private static final String APP_KEY = "3qbao0627j4akqw";
    private static final String APP_SECRET = "qgfco2w66g1sel6";
    private static final String ACCESS_TOKEN = "90zEdXtYDfAAAAAAAAAACKjElosrFCbADS-ABbMe-vOIPlCC1JkbhFXmF71-enHG";

    private AndroidAuthSession buildSession()
    {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESS_TOKEN);
        return session;
    }

}
