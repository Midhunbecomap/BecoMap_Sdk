package com.example.becomap_android_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.becomap.sdk.Becomap;
import com.becomap.sdk.Model.FloorData;
import com.becomap.sdk.util.TokenCallback;

public class MainActivity extends AppCompatActivity {
    private Becomap becomap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        FrameLayout mapContainer = findViewById(R.id.map_container);

        // Initialize Becomap
        becomap = new Becomap(this);
        
        // Set floor selection listener
        becomap.setOnFloorSelectedListener(selectedFloor -> {
            // Handle floor selection here
            Log.d("MainActivity", "Selected floor: " + selectedFloor.getShortName());
        });

        becomap.authenticate(
                "c079dfa3a77dad13351cfacd95841c2c2780fe08",  // clientId
                "f62a59675b2a47ddb75f1f994d88e653",          // clientSecret
                this,   // ViewModelStoreOwner (Activity)
                new TokenCallback() {
                    @Override
                    public void onSuccess(String accessToken) {
                        // ✅ Token received — now load the map
                        becomap.initializeMap(mapContainer);
                        Log.e("onSuccess: ", accessToken);
                        
                        // Fetch building data after successful authentication
                        fetchBuildingData();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(MainActivity.this, "Auth failed: " + error, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void fetchBuildingData() {
        // Replace these with your actual site and building IDs
        String siteId = "your_site_id";
        String buildingId = "your_building_id";
        
        becomap.fetchFloorData(siteId, buildingId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        becomap.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        becomap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        becomap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        becomap.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        becomap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        becomap.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        becomap.onLowMemory();
    }
}