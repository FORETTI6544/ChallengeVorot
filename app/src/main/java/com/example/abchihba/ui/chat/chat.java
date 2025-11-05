package com.example.abchihba.ui.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.Chat;
import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentChatBinding;
import com.example.abchihba.ui.Reviews;
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.context.AttributeContext;
import com.squareup.picasso.Picasso;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class chat extends Fragment {

    private FragmentChatBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Инициализация ViewModel
        ViewModel viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        // Инициализация binding
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Map<String, Users> userMap = new HashMap<>();
        viewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            userMap.clear();
            for (Users user : users) {
                userMap.put(user.getTag(), user);
            }
        });
        viewModel.loadChat().observe(getViewLifecycleOwner(), chat -> {
            binding.chat.removeAllViews();
            for (Chat msg : chat) {
                binding.chat.addView(createMessageFrame(msg, userMap));
            }
        });

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.sendMessage(binding.message.getText().toString());
                binding.message.setText("");
            }
        });

        return root;
    }
    public ConstraintLayout createMessageFrame(Chat msg, Map<String, Users> userMap){
        ConstraintLayout messageFrame = new ConstraintLayout(getContext());
        messageFrame.setId(View.generateViewId());
        messageFrame.setPadding(dpToPx(getContext(), 5), dpToPx(getContext(), 5), dpToPx(getContext(), 5), dpToPx(getContext(), 5));
        messageFrame.setBackgroundResource(R.drawable.design_window);

        ShapeableImageView avatar = new ShapeableImageView(getContext());
        ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
        avatar.setShapeAppearanceModel(shape);
        avatar.setId(View.generateViewId());
        avatar.setImageBitmap(base64ToBitmap(userMap.get(msg.getUserTag()).getAvatar()));

        TextView name = new TextView(getContext());
        name.setId(View.generateViewId());
        name.setTextColor(getResources().getColor(R.color.text));
        name.setTypeface(null, Typeface.BOLD);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        name.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        name.setText(userMap.get(msg.getUserTag()).getName());

        TextView message = new TextView(getContext());
        message.setId(View.generateViewId());
        message.setTextColor(getResources().getColor(R.color.secondaryText));
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        message.setText(msg.getMessage());

        ConstraintLayout.LayoutParams messageFrameLayout = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageFrameLayout.setMargins(dpToPx(getContext(), 10), dpToPx(getContext(), 10), dpToPx(getContext(), 10), dpToPx(getContext(), 10));
        messageFrame.setLayoutParams(messageFrameLayout);

        ConstraintLayout.LayoutParams avatarLayout = new ConstraintLayout.LayoutParams(dpToPx(getContext(), 30), dpToPx(getContext(), 30));
        avatarLayout.leftToLeft = messageFrame.getId();
        avatarLayout.topToTop = messageFrame.getId();
        avatar.setLayoutParams(avatarLayout);

        ConstraintLayout.LayoutParams nameLayout = new ConstraintLayout.LayoutParams(0, dpToPx(getContext(), 30));
        nameLayout.leftMargin = dpToPx(getContext(), 5);
        nameLayout.topToTop = messageFrame.getId();
        nameLayout.leftToRight = avatar.getId();
        nameLayout.rightToRight = messageFrame.getId();
        name.setLayoutParams(nameLayout);

        ConstraintLayout.LayoutParams messageLayout = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageLayout.topToBottom = name.getId();
        messageLayout.leftToLeft = messageFrame.getId();
        messageLayout.rightToRight = messageFrame.getId();
        messageLayout.bottomToBottom = messageFrame.getId();
        message.setLayoutParams(messageLayout);

        messageFrame.addView(avatar);
        messageFrame.addView(name);
        messageFrame.addView(message);

        return messageFrame;
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
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}