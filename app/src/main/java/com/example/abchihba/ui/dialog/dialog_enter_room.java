package com.example.abchihba.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.databinding.DialogEnterRoomBinding;
import com.example.abchihba.ui.ViewModel;

import java.util.Objects;


public class dialog_enter_room extends DialogFragment {
    private DialogEnterRoomBinding binding;
    private ViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEnterRoomBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);


        View view = binding.getRoot();

        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        String password = bundle.getString("password");

        binding.name.setText(name);
        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(password, binding.password.getText().toString())) {
                    viewModel.setRoom("name");
                    Bundle result = new Bundle();
                    getParentFragmentManager().setFragmentResult("dialog_enter_room_result", result);
                    dismiss();
                }
            }
        });



        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}