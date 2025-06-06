package com.example.becomap_android_new.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.LocationModel;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.model.Store;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.ArrayList;
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private Context context;
    private List<LocationModel> stores;
    private OnStoreClickListener listener;

    public StoreAdapter(Context context, List<LocationModel> stores, OnStoreClickListener listener) {
        this.context = context;
        this.stores = stores != null ? stores : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        LocationModel store = stores.get(position);

        if (store != null) {
            holder.storeName.setText(store.getName() != null ? store.getName() : "");
            holder.storeFloor.setText(store.getType() != null ? store.getType() : "");

            String description = store.getDescription();

            if (description == null || description.trim().isEmpty() || description.trim().equals(".")) {
                holder.storeDescription.setVisibility(View.GONE);
                holder.storeDescription.setText(""); // Clear any old recycled content
            } else {
                Log.e("storeDescription: ", description);
                holder.storeDescription.setVisibility(View.VISIBLE);
                holder.storeDescription.setText(description);
            }

            String fallbackIcon = null;

            if (store.getCategories() != null && !store.getCategories().isEmpty()) {
                fallbackIcon = store.getCategories().get(0).getIconName();
            }

// Now use the fallbackIcon safely
            if (store.getLogo() == null || store.getLogo().isEmpty()) {
                Glide.with(context)
                        .load(fallbackIcon)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.storeImage);
            } else {
                Glide.with(context)
                        .load(store.getLogo())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.storeImage);
            }
            holder.navigate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStoreClick(store);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImage;
        TextView storeName;
        TextView storeFloor;
        TextView storeDescription;
        MaterialButton navigate;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeFloor = itemView.findViewById(R.id.storefloor);
            storeDescription = itemView.findViewById(R.id.storediscription);
            navigate = itemView.findViewById(R.id.navigate);
        }
    }

    public interface OnStoreClickListener {
        void onStoreClick(LocationModel location);
    }
}