package ru.example.makaroff.wheathervideo.io;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.example.makaroff.wheathervideo.io.rest.TaskDownloadFile;
import ru.example.makaroff.wheathervideo.io.rest.TaskGetDropboxFiles;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFile;

public class Dropbox {

    public static List<DropboxFile> getFileList(){
        TaskGetDropboxFiles taskGetDropboxFiles = new TaskGetDropboxFiles();
        taskGetDropboxFiles.execute();
        List<String> fileNames;
        try {
            fileNames = taskGetDropboxFiles.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }

        if(fileNames==null){
            return null;
        }

        List<DropboxFile> files = new ArrayList<>();
        for(String fileName : fileNames) {
            files.add(new DropboxFile(fileName, DropboxFile.NOT_LOADED));
        }
        return files;
    }

    public static void downloadFile(String name) {
        TaskDownloadFile task = new TaskDownloadFile();
        task.execute(name);
    }
}
