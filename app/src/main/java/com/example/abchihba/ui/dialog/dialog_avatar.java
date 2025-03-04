package com.example.abchihba.ui.dialog;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.databinding.DialogEditAvatarBinding;
import com.example.abchihba.ui.ViewModel;
import com.google.android.material.imageview.ShapeableImageView;

public class dialog_avatar extends DialogFragment {
    private DialogEditAvatarBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditAvatarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);

        ShapeableImageView avatar1 = binding.avatar1;
        ShapeableImageView avatar2 = binding.avatar2;
        ShapeableImageView avatar3 = binding.avatar3;
        ShapeableImageView avatar4 = binding.avatar4;
        ShapeableImageView avatar5 = binding.avatar5;
        ShapeableImageView avatar6 = binding.avatar6;
        ShapeableImageView avatar7 = binding.avatar7;
        ShapeableImageView avatar8 = binding.avatar8;
        ShapeableImageView avatar9 = binding.avatar9;

        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "1";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "2";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "3";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "4";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "5";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "6";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "7";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "8";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
            }
        });
        avatar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newavatar = "9";
                Bundle result = new Bundle();
                result.putString("new_avatar", newavatar);
                getParentFragmentManager().setFragmentResult("edit_avatar_result", result);
                dismiss();
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
