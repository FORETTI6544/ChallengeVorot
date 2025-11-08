package com.example.abchihba.ui.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.databinding.DialogSwapBinding;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.Users;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Base64;
import java.util.List;


public class dialog_swap extends DialogFragment {
    private DialogSwapBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This removes black background below corners.
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogSwapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ViewModel viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        List<Users> users = viewModel.getRoomUsers().getValue();

        binding.linear.removeAllViews();
        for (Users user : users) {
            ConstraintLayout userView = new ConstraintLayout(requireContext());
            ConstraintLayout.LayoutParams userLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            userLayout.setMargins(0, 10, 0, 10);
            userView.setLayoutParams(userLayout);
            userView.setPadding(20, 20, 20, 20);
            userView.setBackgroundResource(R.drawable.design_window);
            userView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String to = user.getTag();
                    Bundle result = new Bundle();
                    result.putString("result", to);
                    getParentFragmentManager().setFragmentResult("swap_result", result);
                    dismiss();
                }
            });

            TextView name = new TextView(getContext());
            name.setText(user.getName());
            name.setTextSize(20);
            name.setTypeface(null, Typeface.BOLD);
            name.setId(View.generateViewId());

            ShapeableImageView avatar = new ShapeableImageView(requireContext());
            ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
            avatar.setShapeAppearanceModel(shape);
            avatar.setImageBitmap(base64ToBitmap(user.getAvatar()));
            avatar.setId(View.generateViewId());

            TextView game = new TextView(getContext());
            game.setText(user.getGame());
            game.setTextSize(15);
            game.setTypeface(null, Typeface.BOLD);
            game.setId(View.generateViewId());



            ConstraintLayout.LayoutParams gameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            gameLayout.leftToRight = avatar.getId();
            gameLayout.topToBottom = name.getId();
            gameLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            gameLayout.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            gameLayout.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            gameLayout.leftMargin = 20;
            game.setLayoutParams(gameLayout);
            userView.addView(game);



            ConstraintLayout.LayoutParams avatarLayout = new ConstraintLayout.LayoutParams(150, 150);
            avatarLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            avatarLayout.setMargins(4, 4, 4, 4);
            avatar.setLayoutParams(avatarLayout);
            userView.addView(avatar);

            ConstraintLayout.LayoutParams nameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            nameLayout.leftToRight = avatar.getId();
            nameLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            nameLayout.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            nameLayout.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            nameLayout.leftMargin = 20;
            name.setLayoutParams(nameLayout);
            userView.addView(name);

            if (!user.getTag().equals(viewModel.getTag().getValue())) {
                binding.linear.addView(userView);
            }
        }


        return view;
    }

    public static Bitmap base64ToBitmap(String base64String) {
        try {
            // Декодируем Base64 в массив байтов
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            // Конвертируем байты в Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
