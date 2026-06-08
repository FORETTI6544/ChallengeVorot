package com.foretti.challengevorot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewmodel extends androidx.lifecycle.ViewModel {
    MutableLiveData<String> username;
    MutableLiveData<String> avatar;
    MutableLiveData<Integer> balance;
    public ActivityViewmodel() {
        username = new MutableLiveData<>();
        username.setValue("");
        avatar = new MutableLiveData<>();
        avatar.setValue("");
        balance = new MutableLiveData<>();
        balance.setValue(0);
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public LiveData<String> getAvatar() {
        return avatar;
    }
    public LiveData<Integer> getBalance() {
        return balance;
    }

    public void setAvatar(MutableLiveData<String> avatar) {
        this.avatar = avatar;
    }
    public void setUser(String n, String a, Integer b) {
        this.username.postValue(n);
        this.avatar.postValue(a);
        this.balance.postValue(b);
    }
}
