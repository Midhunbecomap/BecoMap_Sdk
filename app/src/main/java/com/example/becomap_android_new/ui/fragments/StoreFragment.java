package com.example.becomap_android_new.ui.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.becomap_android_new.R;
import com.example.becomap_android_new.adapter.StoreAdapter;
import com.example.becomap_android_new.model.Store;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment {
    private static final String TAG = "StoreFragment";
    private GridView storeGridView;
    private List<Store> stores = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        try {
            storeGridView = root.findViewById(R.id.storeGridView);
            if (storeGridView == null) {
                Log.e(TAG, "storeGridView is null");
                Toast.makeText(requireContext(), "Error initializing store grid", Toast.LENGTH_SHORT).show();
                return root;
            }

            loadStores();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView", e);
            Toast.makeText(requireContext(), "Error initializing store view", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void loadStores() {
        try {
            InputStream is = requireContext().getAssets().open("store.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            Log.d(TAG, "Loaded JSON: " + json.substring(0, Math.min(100, json.length())) + "...");

            Gson gson = new Gson();
            Type type = new TypeToken<StoreResponse>() {}.getType();
            StoreResponse response = gson.fromJson(json, type);

            if (response != null && response.getStores() != null) {
                stores = response.getStores();
                Log.d(TAG, "Loaded " + stores.size() + " stores");

                if (storeGridView != null) {
                    StoreAdapter adapter = new StoreAdapter(requireContext(), stores);
                    storeGridView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "storeGridView is null when setting adapter");
                }
            } else {
                Log.e(TAG, "Response or stores list is null");
                Toast.makeText(requireContext(), "Failed to load stores", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading store.json", e);
            Toast.makeText(requireContext(), "Error reading store data", Toast.LENGTH_SHORT).show();
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Error parsing store.json", e);
            Toast.makeText(requireContext(), "Error parsing store data", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error loading stores", e);
            Toast.makeText(requireContext(), "Unexpected error loading stores", Toast.LENGTH_SHORT).show();
        }
    }

    private static class StoreResponse {
        private List<Store> stores;

        public StoreResponse() {
            this.stores = new ArrayList<>();
        }

        public List<Store> getStores() {
            return stores;
        }
    }
}