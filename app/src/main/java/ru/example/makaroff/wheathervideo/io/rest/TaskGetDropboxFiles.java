package ru.example.makaroff.wheathervideo.io.rest;

import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TaskGetDropboxFiles extends AsyncTask<Void, Void, List<String>> {
    @Override
    protected List<String> doInBackground(Void... params) {
        //String ACCESS_TOKEN = "BEboJ6poqvEAAAAAAAAEXVxWuNnm1wF6OiwYTupIJVvmz-W-g9IXeRQZd72sOQpj";
        String ACCESS_TOKEN = "90zEdXtYDfAAAAAAAAAACKjElosrFCbADS-ABbMe-vOIPlCC1JkbhFXmF71-enHG";
        List<String> names = new ArrayList<>();
        try
        {
            List<Metadata> metadatas = new DbxClientV2(new DbxRequestConfig(""), ACCESS_TOKEN).files().listFolder("").getEntries();
            for (Metadata metadata : metadatas) {
                    names.add(metadata.getName());
            }
        } catch (DbxException d) {
            return null;
        }
        return names;
    }
}
