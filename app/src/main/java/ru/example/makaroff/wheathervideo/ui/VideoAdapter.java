package ru.example.makaroff.wheathervideo.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.example.makaroff.wheathervideo.R;
import ru.example.makaroff.wheathervideo.io.rest.model.VideoModel.DropboxFile;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DropboxFile> files = new ArrayList<>();
    OnElementListClick onElementListClick;

    public VideoAdapter(OnElementListClick onElementListClick, List<DropboxFile> files) {
        this.onElementListClick = onElementListClick;
        this.files = files;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ElementViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ElementViewHolder)holder).bind(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private class ElementViewHolder extends RecyclerView.ViewHolder {

        TextView textViewElementList;
        ImageView imageViewElementList;
        ProgressBar progressBarElementList;

        ElementViewHolder(View view) {
            super(view);
            textViewElementList = (TextView) view.findViewById(R.id.textViewElementList);
            imageViewElementList = (ImageView) view.findViewById(R.id.imageViewElementList);
            progressBarElementList = (ProgressBar) view.findViewById(R.id.progressBarElementList);
        }

        void bind(final DropboxFile file) {
            textViewElementList.setText(file.getName());
            if (file.getCode() == DropboxFile.DOWNLOADED)
                imageViewElementList.setImageResource(R.drawable.success);
            if (file.getCode() == DropboxFile.NOT_LOADED)
                imageViewElementList.setImageResource(R.drawable.abort);

            imageViewElementList.setVisibility((file.getCode() == DropboxFile.DOWNLOADING) ? View.GONE : View.VISIBLE);
            progressBarElementList.setVisibility((file.getCode() == DropboxFile.DOWNLOADING) ? View.VISIBLE : View.GONE);

            textViewElementList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onElementListClick.onElementListClick(file);
                }
            });
        }
    }

    public void changeElement(DropboxFile file){
        for(int i=0;i<files.size();i++) {
            if (files.get(i).getName().compareTo(file.getName())==0) {
                files.get(i).setCode(file.getCode());
                break;
            }
        }
        notifyDataSetChanged();
    }

    public interface OnElementListClick
    {
        void onElementListClick(DropboxFile file);
    }
}
