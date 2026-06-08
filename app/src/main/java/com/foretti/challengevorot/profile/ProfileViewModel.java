package com.foretti.challengevorot.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import com.foretti.challengevorot.models.Room;

public class ProfileViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<String> userID;
    private final MutableLiveData<List<Room>> rooms;

    public ProfileViewModel() {
        userID = new MutableLiveData<>();
        userID.setValue("");
        rooms = new MutableLiveData<>();
        rooms.setValue(new ArrayList<>());
    }

    public LiveData<String> getUserID() {
        return userID;
    }

    public LiveData<List<Room>> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> r) {
        rooms.postValue(r);
    }
    public void setUserID(String id) {
        userID.postValue(id);
    }
}
