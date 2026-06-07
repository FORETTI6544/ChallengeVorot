package com.foretti.challengevorot.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.RoomsAdapter;
import com.foretti.challengevorot.network.WebSocketManager;

public class ProfileWithoutRoomFragment extends Fragment {

    private String userID;
    ProfileViewModel viewModel;
    private RecyclerView rvRooms;
    private RoomsAdapter roomsAdapter;
    private WebSocketManager.RoomsCallback roomsCallback;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bundle args = getArguments();
        if (args != null) {
            viewModel = new ProfileViewModel(args.getString("userId"));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LifeCycle", "onCreateView");

        return inflater.inflate(R.layout.fragment_profile_without_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("LifeCycle", "onViewCreated");

        super.onViewCreated(view, savedInstanceState);
        rvRooms = view.findViewById(R.id.rvRooms);
        roomsAdapter = new RoomsAdapter();
        rvRooms.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRooms.setAdapter(roomsAdapter);

        roomsCallback = rooms -> viewModel.setRooms(rooms);
        WebSocketManager.getInstance().setRoomsCallback(roomsCallback);
        WebSocketManager.getInstance().send("{\"type\":\"get_rooms\"}");

        viewModel.getRooms().observe(getViewLifecycleOwner(), rooms -> {
            roomsAdapter.updateRooms(rooms);
        });
    }
}
