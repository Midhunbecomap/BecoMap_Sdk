package com.example.becomap_android_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.becomap.sdk.Becomap;
import com.becomap.sdk.util.TokenCallback;

public class MainActivity extends AppCompatActivity {
    private Becomap becomap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout mapContainer = findViewById(R.id.map_container);
        becomap = new Becomap(this);
        becomap.authenticate(
                "fb3acc6fc0fb6d700fe03d2a66f3a20aa082aa8a",  // clientId
                "c74d4db106594188ba7993f4c1999144",          // clientSecret
                this,   // ViewModelStoreOwner (Activity)
                new TokenCallback() {
                    @Override
                    public void onSuccess(String accessToken) {
                        // ✅ Token received — now load the map
                        becomap.initializeMap(mapContainer);
                        Log.e( "onSuccess: ",accessToken );
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(MainActivity.this, "Auth failed: " + error, Toast.LENGTH_LONG).show();
                    }
                }
        );
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