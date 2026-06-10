package com.foretti.challengevorot.market;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foretti.challengevorot.models.MarketItem;

import java.util.ArrayList;
import java.util.List;

public class MarketViewModel extends ViewModel {
    private MutableLiveData<List<MarketItem>> items;

    public MarketViewModel() {
        items = new MutableLiveData<>();
        items.setValue(new ArrayList<>());
    }

    public void setItems(List<MarketItem> items) {
        this.items.postValue(items);
    }

    public LiveData<List<MarketItem>> getItems() {
        return items;
    }
}
