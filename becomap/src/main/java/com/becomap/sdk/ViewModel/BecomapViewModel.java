package com.becomap.sdk.ViewModel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BecomapViewModel extends AndroidViewModel {
    private final MutableLiveData<String> clientId = new MutableLiveData<>();
    private final MutableLiveData<String> clientSecret = new MutableLiveData<>();
    private final MutableLiveData<String> siteIdentifier = new MutableLiveData<>();

    public BecomapViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCredentials(String clientId, String clientSecret, String siteIdentifier) {
        this.clientId.setValue(clientId);
        this.clientSecret.setValue(clientSecret);
        this.siteIdentifier.setValue(siteIdentifier);
    }

    public LiveData<String> getClientId() {
        return clientId;
    }

    public LiveData<String> getClientSecret() {
        return clientSecret;
    }


    public LiveData<String> getSiteIdentifier() {
        return siteIdentifier;
    }
}

