package com.example.becomap_android_new.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.model.Location;

import java.util.List;

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {

    private List<FloorModel> shortNames;
    private Context context;
    private OnItemClickListener listener;


    public FloorAdapter(Context context, List<FloorModel> shortNames, OnItemClickListener listener){
        this.context = context;
        this.shortNames = shortNames;
        this.listener = listener;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.floor_item, parent, false);
        return new FloorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FloorViewHolder holder, int position) {
        FloorModel floorName = shortNames.get(position);
        holder.floorName.setText(floorName.getShortName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onClick: ",floorName.getShortName());
                listener.onItemClick(floorName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shortNames.size();
    }

    public class FloorViewHolder extends RecyclerView.ViewHolder {
        public TextView floorName;

        public FloorViewHolder(View itemView) {
            super(itemView);
            floorName = itemView.findViewById(R.id.floor_name);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(FloorModel floorModel);
    }
}

