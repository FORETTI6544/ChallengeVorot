package com.example.abchihba.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentRoomsBinding;
import com.example.abchihba.ui.ViewModel;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class rooms extends Fragment {

    private FragmentRoomsBinding binding;
    private ViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        // Инициализация binding
        binding = FragmentRoomsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button createRoomBtn = binding.createRoom;
        Button chooseRoomBtn = binding.chooseRoom;



        createRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        chooseRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_choose_room);
            }
        });

        return root;
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
