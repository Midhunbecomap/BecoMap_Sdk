package com.example.becomap_android_new.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.model.BCHappenings;
import com.becomap.sdk.model.LocationModel;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Events_Adapter extends RecyclerView.Adapter<Events_Adapter.ViewHolder> {
    private Context context;
    private List<BCHappenings> happenings;
    private Events_Adapter.OnStoreClickListener listener;

    public Events_Adapter(Context context, List<BCHappenings> happenings, Events_Adapter.OnStoreClickListener listener) {
        this.context = context;
        this.happenings = happenings != null ? happenings : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public Events_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new Events_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Events_Adapter.ViewHolder holder, int position) {
        BCHappenings happening = happenings.get(position);

        if (happening != null) {
            holder.storeName.setText(happening.getName() != null ? happening.getName() : "");
            holder.storeFloor.setText(happening.getType() != null ? happening.getType() : "");

            String description = happening.getDescription();

            if (description == null || description.trim().isEmpty() || description.trim().equals(".")) {
                holder.storeDescription.setVisibility(View.GONE);
                holder.storeDescription.setText(""); // Clear any old recycled content
            } else {
                Log.e("storeDescription: ", description);
                holder.storeDescription.setVisibility(View.VISIBLE);
                holder.storeDescription.setText(description);
            }

            if (happening.getImages() != null && !happening.getImages().isEmpty()) {
                Glide.with(context)
                        .load(happening.getImages().get(0))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.storeImage);
            } else {
                holder.storeImage.setImageResource(R.drawable.ic_launcher_background); // fallback image
            }

            holder.navigate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStoreClick(happening.getLocationId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return happenings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImage;
        TextView storeName;
        TextView storeFloor;
        TextView storeDescription;
        MaterialButton navigate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeFloor = itemView.findViewById(R.id.storefloor);
            storeDescription = itemView.findViewById(R.id.storediscription);
            navigate = itemView.findViewById(R.id.navigate);
        }
    }

    public interface OnStoreClickListener {
        void onStoreClick(String locationid);
    }
}
