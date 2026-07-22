package com.foretti.challengevorot.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.adapters.RoomsAdapter;
import com.foretti.challengevorot.network.WebSocketManager;

public class ProfileWithoutRoomFragment extends Fragment {

    private String userID;
    ProfileViewModel viewModel;
    private RecyclerView rvRooms;
    private Button createRoomBtn;
    private RoomsAdapter roomsAdapter;
    private WebSocketManager.RoomsCallback roomsCallback;
    private WebSocketManager.JoinRoomCallback joinRoomCallback;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        Bundle args = getArguments();
        if (args != null) {
            viewModel.setUserID(args.getString("userId"));
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

        createRoomBtn = view.findViewById(R.id.createRoomBtn);
        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateRoomDialog();
            }
        });

        roomsCallback = rooms -> viewModel.setRooms(rooms);
        WebSocketManager.getInstance().setRoomsCallback(roomsCallback);
        WebSocketManager.getInstance().send("{\"type\":\"get_rooms\"}");

        NavController navController = Navigation.findNavController(requireView());
        joinRoomCallback = () -> {
            requireActivity().runOnUiThread(() -> {
                navController.navigate(R.id.ProfileWithRoomFragment);
            });
        };
        WebSocketManager.getInstance().setJoinRoomCallback(joinRoomCallback);

        viewModel.getRooms().observe(getViewLifecycleOwner(), rooms -> {
            roomsAdapter.updateRooms(rooms);
        });
    }
    private void showCreateRoomDialog() {
        CreateRoomDialog dialog = new CreateRoomDialog(getContext());
        dialog.show();
    }

}
