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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chathu.georoam.R;
import com.chathu.georoam.model.EventsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventImageAdapter extends RecyclerView.Adapter<EventImageAdapter.ImageHolderView> {

    private Context mContext;
    private List<EventsModel> mEvents;
    private EventImageAdapter.OnItemClickListener mListener;

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
        holder.cardViewStatus.setText(events.getIsPrivate());
        Picasso.get().load(events.getEventImageURL()).fit().centerCrop().into(holder.cardViewEventImage);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }



    public class ImageHolderView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener{

        public TextView cardViewEventName;
        public TextView cardViewEventDescription;
        public TextView cardViewStatus;
        public ImageView cardViewEventImage;

        public ImageHolderView(View itemView) {
            super(itemView);

            cardViewEventName = itemView.findViewById(R.id.cardViewEventNameText);
            cardViewEventDescription = itemView.findViewById(R.id.cardViewEventDescription);
            cardViewStatus = itemView.findViewById(R.id.cardViewEventStatus);
            cardViewEventImage = itemView.findViewById(R.id.cardViewEventImage);

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
            menu.setHeaderTitle("Select Action");
            MenuItem viewEvent =  menu.add(Menu.NONE, 1, 1, "View Event");
            MenuItem deleteEvent = menu.add(Menu.NONE, 1, 1, "Delete Event");

            viewEvent.setOnMenuItemClickListener(this);
            deleteEvent.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener !=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1: mListener.onViewEventClick(position); return true;
                        case 2: mListener.onDeleteEventClick(position); return true;
                    }
                }
            }
            return false;
        }
    }


    public interface OnItemClickListener {
        void onViewEventClick(int position);

        void OnItemClick(int position);

        void onDeleteEventClick(int position);
    }

    public void setOnItemClickListener(EventImageAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
