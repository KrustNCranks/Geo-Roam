package com.chathu.georoam.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chathu.georoam.R;
import com.chathu.georoam.model.Pictures;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Pictures> mPictures;

    // Constructor
    public ImageAdapter(Context context, List<Pictures> pictures){
        mContext = context;
        mPictures = pictures;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView cardViewImageName;
        public TextView cardViewImageDescription;
        public ImageView cardViewImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            cardViewImageName = itemView.findViewById(R.id.cardViewImageNameText);
            cardViewImageDescription = itemView.findViewById(R.id.cardViewImageNameDescription);
            cardViewImage = itemView.findViewById(R.id.cardViewImage);
        }
    }
}
