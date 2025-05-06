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

        FrameLayout mapContainer = findViewById(R.id.map_container);
        becomap = new Becomap(this);
        becomap.initializeMap(mapContainer);
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