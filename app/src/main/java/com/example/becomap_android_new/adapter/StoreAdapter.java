package com.example.becomap_android_new.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.becomap_android_new.R;
import com.example.becomap_android_new.model.Store;

import java.util.List;
import java.util.ArrayList;
public class StoreAdapter extends BaseAdapter {
    private Context context;
    private List<Store> stores;

    public StoreAdapter(Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores != null ? stores : new ArrayList<>();
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
            holder.storeRating = convertView.findViewById(R.id.storeRating);
            holder.storeReviews = convertView.findViewById(R.id.storeReviews);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Store store = stores.get(position);
        if (store != null) {
            holder.storeName.setText(store.getName() != null ? store.getName() : "");
            holder.storeType.setText(store.getType() != null ? store.getType() : "");
            holder.storeRating.setText(String.format("%.1f", store.getRating()));
            holder.storeReviews.setText(String.format("(%d)", store.getTotalReviews()));

            // Use a default placeholder image
            holder.storeImage.setImageResource(R.drawable.ic_launcher_background);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView storeImage;
        TextView storeName;
        TextView storeType;
        TextView storeRating;
        TextView storeReviews;
    }
}
