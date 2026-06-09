package com.foretti.challengevorot.rotation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foretti.challengevorot.models.User;

import java.util.ArrayList;
import java.util.List;

public class RotationViewModel extends androidx.lifecycle.ViewModel {
    public RotationViewModel() {
        users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());
        rotationStatus = new MutableLiveData<>();
        rotationStatus.setValue(true);
    }
    MutableLiveData<List<User>> users;
    MutableLiveData<Boolean> rotationStatus;
    public void setUsers(List<User> users) { this.users.postValue(users); }

    public LiveData<List<User>> getUsers() { return users; }

    public void setRotationStatus(Boolean rotationStatus) {
        this.rotationStatus.postValue(rotationStatus);
    }
    public LiveData<Boolean> getRotationStatus(){
        return rotationStatus;
    }
}
