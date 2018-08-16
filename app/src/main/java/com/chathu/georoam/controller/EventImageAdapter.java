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
import com.chathu.georoam.model.EventsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventImageAdapter extends RecyclerView.Adapter<EventImageAdapter.ImageHolderView> {

    private Context mContext;
    private List<EventsModel> mEvents;

    // Constructor
    public EventImageAdapter(Context context, List<EventsModel> events){
        mContext = context;
        mEvents = events;
    }

    @NonNull
    @Override
    public ImageHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_events_card_view,parent,false);
        return new ImageHolderView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolderView holder, int position) {
        EventsModel events = mEvents.get(position);
        holder.cardViewEventName.setText(events.getName());
        holder.cardViewEventDescription.setText(events.getDescription());
        Picasso.get().load(events.getEventImageURL()).fit().centerCrop().into(holder.cardViewEventImage);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ImageHolderView extends RecyclerView.ViewHolder {

        public TextView cardViewEventName;
        public TextView cardViewEventDescription;
        public ImageView cardViewEventImage;
        public ImageHolderView(View itemView) {
            super(itemView);

            cardViewEventName = itemView.findViewById(R.id.cardViewEventNameText);
            cardViewEventDescription = itemView.findViewById(R.id.cardViewEventDescription);
            cardViewEventImage = itemView.findViewById(R.id.cardViewEventImage);
        }
    }
}
