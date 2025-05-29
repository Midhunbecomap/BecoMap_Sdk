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

import com.becomap.sdk.model.SearchResult;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.model.Location;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<SearchResult> locations;
    private SearchResultAdapter.OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(SearchResult location);
    }

    public SearchResultAdapter(List<SearchResult> locations, SearchResultAdapter.OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new SearchResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        SearchResult location = locations.get(position);
        Log.e( "onBindViewHolder: ", location.getName());
        holder.locationName.setText(location.getName());
        holder.locationType.setText(location.getType());
        Glide.with(holder.itemView.getContext())
                .load(location.getLogo()) // Replace with location.getImageUrl() if available
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.icon);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLocationClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateLocations(List<SearchResult> newLocations) {
        this.locations = newLocations;
        Log.e( "onBindViewHolder: ", newLocations.get(0).getName());
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView locationType;
        ImageView icon;
        TextView floor;

        ViewHolder(View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            locationType = itemView.findViewById(R.id.type);
            floor = itemView.findViewById(R.id.floor_name);
            icon=itemView.findViewById(R.id.image);
        }
    }
}