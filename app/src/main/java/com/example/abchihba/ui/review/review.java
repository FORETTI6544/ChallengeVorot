package com.example.abchihba.ui.review;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentReviewBinding;
import com.example.abchihba.ui.Reviews;
import com.example.abchihba.ui.ViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class review extends Fragment {

    private FragmentReviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Инициализация ViewModel
        ViewModel viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        // Инициализация binding
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        viewModel.getName().observe(getViewLifecycleOwner(), name -> binding.name.setText(name));
        viewModel.getAvatar().observe(getViewLifecycleOwner(), avatar -> {

            if ("1".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_1);
            } else if ("2".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_2);
            } else if ("3".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_3);
            } else if ("4".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_4);
            } else if ("5".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_5);
            } else if ("6".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_6);
            } else if ("7".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_7);
            } else if ("8".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_8);
            } else if ("9".equals(avatar)) {
                binding.avatar.setImageResource(R.mipmap.avatar_9);
            } else {
                binding.avatar.setImageResource(R.drawable.avatar_add);
            }

        });
        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> binding.balance.setText(balance));
        viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            binding.reviewsLinear.removeAllViews();
            for (int i = reviews.size() - 1; i >= 0; i--) {
                Reviews review = reviews.get(i);
                ConstraintLayout reviewFrame = new ConstraintLayout(getContext());
                reviewFrame.setBackgroundResource(R.drawable.design_window);
                reviewFrame.setPadding(30, 20, 30, 20);
                ConstraintLayout.LayoutParams frameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                frameLayout.setMargins(15, 20, 15, 20);
                reviewFrame.setLayoutParams(frameLayout);


                ConstraintLayout AVATAR = new ConstraintLayout(getContext());
                AVATAR.setId(View.generateViewId());

                ShapeableImageView avatar = new ShapeableImageView(getContext());
                ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
                avatar.setShapeAppearanceModel(shape);
                db.collection("users").document(review.getTag())
                        .addSnapshotListener((querySnapshot, e) -> {
                            if (e != null) {
                                return;
                            }
                            if (querySnapshot != null) {
                                String avatar_id = querySnapshot.getString("avatar");
                                if ("1".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_1);
                                } else if ("2".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_2);
                                } else if ("3".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_3);
                                } else if ("4".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_4);
                                } else if ("5".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_5);
                                } else if ("6".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_6);
                                } else if ("7".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_7);
                                } else if ("8".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_8);
                                } else if ("9".equals(avatar_id)) {
                                    avatar.setImageResource(R.mipmap.avatar_9);
                                } else {
                                    avatar.setImageResource(R.drawable.avatar_add);
                                }
                            }
                        });

                TextView name = new TextView(getContext());
                name.setTypeface(null, Typeface.BOLD);
                name.setTextSize(30);
                name.setSingleLine();
                db.collection("users").document(review.getTag())
                        .addSnapshotListener((querySnapshot, e) -> {
                            if (e != null) {
                                return;
                            }
                            if (querySnapshot != null) {
                                String name_id = querySnapshot.getString("name");
                                name.setText(name_id);
                            }
                        });
                name.setId(View.generateViewId());

                ImageView preview = new ImageView(getContext());
                Picasso.with(getContext())
                        .load(review.getPreview())
                        .placeholder(R.drawable.avatar_add)
                        .error(R.drawable.avatar_add)
                        .into(preview);
                preview.setId(View.generateViewId());

                TextView game = new TextView(getContext());
                game.setTypeface(null, Typeface.BOLD_ITALIC);
                game.setTextSize(25);
                game.setText(review.getGame());
                game.setId(View.generateViewId());

                TextView review1 = new TextView(getContext());
                review1.setTypeface(null, Typeface.ITALIC);
                review1.setTextSize(15);
                String REVIEW1 = "   " + review.getReview();
                review1.setText(REVIEW1);
                review1.setId(View.generateViewId());

                TextView rating = new TextView(getContext());
                rating.setTypeface(null, Typeface.BOLD);
                rating.setTextSize(30);
                String RATING = review.getRating() + "/10";
                rating.setText(RATING);
                rating.setId(View.generateViewId());

                TextView date = new TextView(getContext());
                date.setTextSize(18);
                date.setTypeface(null, Typeface.ITALIC);
                date.setText(review.getDate());

                ConstraintLayout.LayoutParams AVATARlayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                AVATARlayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                AVATARlayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                AVATARlayout.setMargins(0, 5, 10, 5);
                AVATAR.setLayoutParams(AVATARlayout);
                AVATAR.addView(avatar);

                ConstraintLayout.LayoutParams avatarLayout = new ConstraintLayout.LayoutParams(150, 150);
                avatarLayout.setMargins(4, 4, 4, 4);
                avatarLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                avatarLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                avatarLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                avatarLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                avatar.setLayoutParams(avatarLayout);

                ConstraintLayout.LayoutParams nameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                nameLayout.leftToRight = AVATAR.getId();
                nameLayout.topToTop = AVATAR.getId();
                nameLayout.bottomToBottom = AVATAR.getId();
                name.setLayoutParams(nameLayout);

                ConstraintLayout.LayoutParams previewLayout = new ConstraintLayout.LayoutParams(0, 450);
                previewLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                previewLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                previewLayout.topToBottom = game.getId();
                previewLayout.setMargins(20, 10, 20, 10);
                preview.setLayoutParams(previewLayout);

                ConstraintLayout.LayoutParams gameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                gameLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                gameLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                gameLayout.topToBottom = AVATAR.getId();
                game.setLayoutParams(gameLayout);

                ConstraintLayout.LayoutParams review1Layout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                review1Layout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                review1Layout.topToBottom = preview.getId();
                review1.setLayoutParams(review1Layout);

                ConstraintLayout.LayoutParams ratingLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                ratingLayout.topToBottom = review1.getId();
                ratingLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                rating.setLayoutParams(ratingLayout);

                ConstraintLayout.LayoutParams dateLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                dateLayout.topToBottom = rating.getId();
                dateLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                date.setLayoutParams(dateLayout);

                reviewFrame.addView(AVATAR);
                reviewFrame.addView(name);
                reviewFrame.addView(preview);
                reviewFrame.addView(game);
                reviewFrame.addView(review1);
                reviewFrame.addView(rating);
                reviewFrame.addView(date);
                binding.reviewsLinear.addView(reviewFrame);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}