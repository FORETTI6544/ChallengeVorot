package com.foretti.challengevorot.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.foretti.challengevorot.ActivityViewmodel;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;
import com.squareup.picasso.Picasso;

public class ProfileWithRoomFragment extends Fragment {
    private ActivityViewmodel activityViewModel;
    private WebSocketManager.RoomsCallback roomsCallback;
    ProfileViewModel viewModel;
    private String userID;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewmodel.class);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        Bundle args = getArguments();
        if (args != null) {
            viewModel.setUserID(args.getString("userId"));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_with_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView genre = view.findViewById(R.id.genre);
        TextView game = view.findViewById(R.id.game);
        ImageView gamePreview = view.findViewById(R.id.gamePreview);
        activityViewModel.getGenre().observe(getViewLifecycleOwner(), userGenre ->{
            genre.setText("Жанр: " + userGenre);
        });
        activityViewModel.getGame().observe(getViewLifecycleOwner(), game::setText);
        activityViewModel.getGamePreview().observe(getViewLifecycleOwner(), preview -> {
            Picasso.get()
                    .load(preview)
                    .placeholder(R.drawable.heart_inactive)
                    .error(R.drawable.heart_inactive)
                    .into(gamePreview);
        });



        super.onViewCreated(view, savedInstanceState);
    }
}
