package com.example.mock1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> update = new MutableLiveData<>();

    public void setUpdate(boolean isUpdated) {
        update.setValue(isUpdated);
    }

    public LiveData<Boolean> getUpdate() {
        return update;
    }
}
