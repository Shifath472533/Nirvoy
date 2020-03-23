package com.example.nirvoy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ExampleViewHolder> {
    private ArrayList<PlaceDetails> placeDetailsList;
    private OnItemClickListener placeListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCallClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        placeListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView typeLogo;
        public TextView placeName;
        public TextView placeNumber;
        public ImageView mCallImage;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            typeLogo = itemView.findViewById(R.id.type_logo);
            placeName = itemView.findViewById(R.id.place_name);
            placeNumber = itemView.findViewById(R.id.place_number);
            mCallImage = itemView.findViewById(R.id.image_call);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mCallImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCallClick(position);
                        }
                    }
                }
            });
        }
    }

    public PlaceAdapter(ArrayList<PlaceDetails> exampleList) {
        placeDetailsList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_details, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, placeListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        PlaceDetails currentItem = placeDetailsList.get(position);

        holder.typeLogo.setImageResource(currentItem.getImageResource());
        holder.placeName.setText(currentItem.getPlaceName());
        holder.placeNumber.setText(currentItem.getPlaceNumber());
    }

    @Override
    public int getItemCount() {
        return placeDetailsList.size();
    }
}
