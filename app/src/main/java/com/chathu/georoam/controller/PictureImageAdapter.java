package com.chathu.georoam.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chathu.georoam.R;
import com.chathu.georoam.model.Pictures;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PictureImageAdapter extends RecyclerView.Adapter<PictureImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Pictures> mPictures;
    private PictureImageAdapter.OnItemClickListener mListener;

    // Constructor
    public PictureImageAdapter(Context context, List<Pictures> pictures){
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
        holder.cardViewStatus.setText(currentPicture.getIsPrivate());
        Picasso.get().load(currentPicture.getPictureURL()).fit().centerCrop().into(holder.cardViewImage);
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener{
        public TextView cardViewImageName;
        public TextView cardViewImageDescription;
        public TextView cardViewStatus;
        public ImageView cardViewImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            cardViewImageName = itemView.findViewById(R.id.cardViewImageNameText);
            cardViewImageDescription = itemView.findViewById(R.id.cardViewImageNameDescription);
            cardViewStatus = itemView.findViewById(R.id.cardViewStatus);
            cardViewImage = itemView.findViewById(R.id.cardViewImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener !=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Select Action");
            //MenuItem viewImage =  menu.add(Menu.NONE, 1, 1, "View Image");
            MenuItem deleteImage = menu.add(Menu.NONE, 1, 1, "Delete Image");

            //viewImage.setOnMenuItemClickListener(this);
            deleteImage.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener !=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1:
                            mListener.onDeleteImageClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteImageClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }



    public interface OnItemClickListener {
        void onViewImageClick(int position);

        void OnItemClick(int position);

        void onDeleteImageClick(int position);
    }

    public void setOnItemClickListener(PictureImageAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
