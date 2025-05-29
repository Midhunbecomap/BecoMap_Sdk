package com.example.becomap_android_new.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.LocationModel;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.model.Store;

import java.util.List;
import java.util.ArrayList;
public class StoreAdapter extends BaseAdapter {
    private Context context;
    private List<LocationModel> stores;
    private OnStoreClickListener listener;

    public StoreAdapter(Context context, List<LocationModel> stores, OnStoreClickListener listener) {
        this.context = context;
        this.stores = stores != null ? stores : new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return stores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
            holder = new ViewHolder();
            holder.storeImage = convertView.findViewById(R.id.storeImage);
            holder.storeName = convertView.findViewById(R.id.storeName);
            holder.storeType = convertView.findViewById(R.id.storeType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationModel store = stores.get(position);
        if (store != null) {
            holder.storeName.setText(store.getName() != null ? store.getName() : "");
            holder.storeType.setText(store.getType() != null ? store.getType() : "");
            // Use a default placeholder image
            Glide.with(context)
                    .load(store.getLogo()) // Assuming getLogo() returns the image URL
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.storeImage);
        }
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoreClick(store);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView storeImage;
        TextView storeName;
        TextView storeType;
        TextView storeRating;
        TextView storeReviews;
    }
    public interface OnStoreClickListener {
        void onStoreClick(LocationModel location);
    }
}
