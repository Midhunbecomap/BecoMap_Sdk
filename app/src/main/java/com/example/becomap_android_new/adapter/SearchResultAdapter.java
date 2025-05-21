package com.example.becomap_android_new.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.model.SearchResult;
import com.example.becomap_android_new.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<SearchResult> searchResults;
    private OnSearchResultClickListener listener;

    public interface OnSearchResultClickListener {
        void onSearchResultClick(SearchResult result);
    }

    public SearchResultAdapter(List<SearchResult> searchResults, OnSearchResultClickListener listener) {
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false); // Define this layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult result = searchResults.get(position);
        Log.e( "andry bird: ",result.getExternalId() );
        holder.resultName.setText(result.getExternalId());
        holder.resultType.setText(result.getType());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSearchResultClick(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateResults(List<SearchResult> newResults) {
        this.searchResults = newResults;
        this.searchResults.clear();
        this.searchResults.addAll(newResults);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resultName;
        TextView resultType;

        ViewHolder(View itemView) {
            super(itemView);
            resultName = itemView.findViewById(R.id.locationName);
            resultName = itemView.findViewById(R.id.locationType);
        }
    }
}