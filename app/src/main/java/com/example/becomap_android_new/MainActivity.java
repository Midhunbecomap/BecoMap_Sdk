package com.example.becomap_android_new;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.becomap.sdk.Becomap;

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
        
        // Initialize map with WebView
        becomap.initializeMap(mapContainer);
    }

    @Override
    public void onBackPressed() {
        if (becomap.canGoBack()) {
            becomap.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        becomap.onDestroy();
    }
}