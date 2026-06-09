package com.foretti.challengevorot.wheel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class WheelViewModel extends ViewModel {
    private MutableLiveData<List<String>> genres;
    private MutableLiveData<String> spinningResult;
    private MutableLiveData<Boolean> isSpinning;

    public WheelViewModel() {
        genres = new MutableLiveData<>();
        genres.setValue(new ArrayList<>());
        spinningResult = new MutableLiveData<>();
        spinningResult.setValue("");
        isSpinning = new MutableLiveData<>();
        isSpinning.setValue(false);
    }

    public LiveData<List<String>> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genresList) {
        genres.postValue(genresList);
    }

    public LiveData<String> getSpinningResult() {
        return spinningResult;
    }

    public void setSpinningResult(String result) {
        spinningResult.postValue(result);
    }

    public LiveData<Boolean> getIsSpinning() {
        return isSpinning;
    }

    public void setIsSpinning(Boolean spinning) {
        isSpinning.postValue(spinning);
    }
}
