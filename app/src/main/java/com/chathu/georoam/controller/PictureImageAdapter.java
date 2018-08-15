package com.chathu.georoam.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chathu.georoam.R;
import com.chathu.georoam.model.Pictures;
import com.squareup.picasso.Picasso;

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
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_pictures_card_view,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Pictures currentPicture = mPictures.get(position);
        holder.cardViewImageName.setText(currentPicture.getPictureName());
        holder.cardViewImageDescription.setText(currentPicture.getPictureDescription());
        Picasso.get().load(currentPicture.getPictureURL()).fit().centerCrop().into(holder.cardViewImage);
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
