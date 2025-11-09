package com.example.abchihba.ui.rooms;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentChooseRoomBinding;
import com.example.abchihba.ui.Rooms;
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_enter_room;

import java.util.Objects;

public class chooseRoom extends Fragment {

    private FragmentChooseRoomBinding binding;
    private ViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        // Инициализация binding
        binding = FragmentChooseRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.getRooms().observe(getViewLifecycleOwner(), rooms -> {
            binding.container.removeAllViews();
            for (Rooms room : rooms) {
                binding.container.addView(createRoomFrame(room));
            }
        });

        return root;
    }

    public ConstraintLayout createRoomFrame(Rooms room){
        ConstraintLayout roomFrame = new ConstraintLayout(getContext());
        roomFrame.setId(View.generateViewId());
        roomFrame.setBackgroundResource(R.drawable.frame_secondary);
        roomFrame.setPadding(20,20,20,20);

        TextView roomName = new TextView(getContext());
        roomName.setId(View.generateViewId());
        roomName.setText(room.getName());
        roomName.setTypeface(null, Typeface.BOLD);

        LinearLayout roomPlayers = new LinearLayout(getContext());
        roomPlayers.setId(View.generateViewId());
        roomPlayers.setOrientation(LinearLayout.VERTICAL);
        for (String userTag : room.getUsers()) {
            TextView player = new TextView(getContext());
            for (Users user : viewModel.getAllUsers().getValue()) {
                if (Objects.equals(user.getTag(), userTag)) {
                    player.setText(user.getName());
                }
            }
            roomPlayers.addView(player);
        }

        Button enterBtn = new Button(getContext());
        enterBtn.setId(View.generateViewId());
        enterBtn.setBackgroundResource(R.drawable.proshol);
        enterBtn.setText("Войти");
        enterBtn.setTypeface(null, Typeface.BOLD);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog_enter_room = new dialog_enter_room();
                Bundle bundle = new Bundle();
                bundle.putString("name", room.getName());
                bundle.putString("password", room.getPassword());
                dialog_enter_room.setArguments(bundle);
                getParentFragmentManager().setFragmentResultListener("dialog_enter_room_result", getViewLifecycleOwner(), (requestKey, result) -> {
                    Navigation.findNavController(view).navigate(R.id.navigation_profile);
                });
                dialog_enter_room.show(getParentFragmentManager(), "dialog_enter_room");
            }
        });

        ConstraintLayout.LayoutParams roomFrameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        ConstraintLayout.LayoutParams roomNameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        roomNameLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        roomNameLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        roomNameLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;

        ConstraintLayout.LayoutParams roomPlayersLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        roomPlayersLayout.topToBottom = roomName.getId();
        roomPlayersLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        roomPlayersLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;

        ConstraintLayout.LayoutParams enterBtnLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        enterBtnLayout.topToBottom = roomPlayers.getId();
        enterBtnLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        enterBtnLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        enterBtnLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        roomFrame.setLayoutParams(roomFrameLayout);
        roomName.setLayoutParams(roomNameLayout);
        roomPlayers.setLayoutParams(roomPlayersLayout);
        enterBtn.setLayoutParams(enterBtnLayout);

        roomFrame.addView(roomName);
        roomFrame.addView(roomPlayers);
        roomFrame.addView(enterBtn);

        return roomFrame;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
