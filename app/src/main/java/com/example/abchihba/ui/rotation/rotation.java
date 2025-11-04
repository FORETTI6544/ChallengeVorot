package com.example.abchihba.ui.rotation;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.abchihba.R;
import com.example.abchihba.SteamGameSearcher;
import com.example.abchihba.databinding.FragmentRotationBinding;
import com.example.abchihba.ui.Localusers;
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_game;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class rotation extends Fragment {

    private FragmentRotationBinding binding;

    private ViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        binding = FragmentRotationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        new Thread(() -> {
            SteamGameSearcher searcher = SteamGameSearcher.getInstance(getContext());
            while (searcher.updateData()) {
                System.out.println("Data updated...");
            }
            System.out.println("All data saved to CSV.");
        }).start();

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

        LinearLayout linearLayout = binding.frameLinear;
        LinearLayout rotationLinear = binding.rotationLinear;
        viewModel.getRoomUsers().observe(getViewLifecycleOwner(), users -> {
            if (users == null || users.isEmpty()) {
                // Выводим сообщение или заполняем пустое состояние
                TextView emptyView = new TextView(getContext());
                emptyView.setText("No users available");
                linearLayout.addView(emptyView);
                return;
            }
            rotationLinear.removeAllViews();
            rotationLinear.setPadding(32, 20, 20, 20);
            linearLayout.removeAllViews();
            int counter = 0;
            for (Users user : users) {
                ConstraintLayout status = new ConstraintLayout(getContext());
                if ("playing".equals(user.getStatus())) {
                    status.setBackgroundResource(R.drawable.prohodit);
                } else if ("done".equals(user.getStatus())) {
                    status.setBackgroundResource(R.drawable.proshol);
                    counter += 1;
                } else if ("drop".equals(user.getStatus())) {
                    status.setBackgroundResource(R.drawable.dropnul);
                    counter += 1;
                } else {
                    status.setBackgroundResource(R.drawable.design_window);
                }
                if (counter == users.size()) {
                    viewModel.setRotationStarted(false);
                } else {
                    viewModel.setRotationStarted(true);
                }

                ConstraintLayout.LayoutParams statusLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                statusLayout.setMargins(15, 20, 15, 20);
                status.setLayoutParams(statusLayout);


                ConstraintLayout userView = new ConstraintLayout(getContext());
                ConstraintLayout.LayoutParams userLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);


                viewModel.getTo().observe(getViewLifecycleOwner(), to -> {
                    if (to.equals(user.getTag()) && ("Игра отсутствует".equals(user.getGame())) && !"Отсутствует".equals(user.getGenre())) {
                        userView.setBackgroundResource(R.drawable.proshol);
                        userLayout.setMargins(0, 0, 0, 0);
                        userView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment dialog_game = new dialog_game();

                                Bundle genre = new Bundle();
                                genre.putString("genre", user.getGenre());
                                dialog_game.setArguments(genre);

                                dialog_game.show(getParentFragmentManager(), "dialog_game");

                                getParentFragmentManager().setFragmentResultListener("edit_game_result", getViewLifecycleOwner(), (requestKey, result) -> {
                                    String newGame = result.getString("new_game");
                                    String preview = result.getString("preview");
                                    if (newGame != null) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        String tagValue = user.getTag();
                                        if (tagValue == null || tagValue.isEmpty()) {
                                            Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
                                        }
                                        db.collection("users").document(tagValue)
                                                .update("game", newGame);
                                        db.collection("users").document(tagValue)
                                                .update("status", "playing");
                                        db.collection("users").document(tagValue)
                                                .update("preview", preview);
                                        Toast.makeText(getContext(), "Загадано: " + newGame, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else {
                        userView.setBackgroundResource(R.drawable.design_window);
                        userLayout.setMargins(0, 0, 30, 0);
                    }
                });

                userView.setPadding(20, 20, 20, 20);
                userView.setLayoutParams(userLayout);




                TextView name = new TextView(getContext());
                name.setText(user.getName());
                name.setTextSize(20);
                name.setTypeface(null, Typeface.BOLD);
                name.setId(View.generateViewId());

                ShapeableImageView avatar = new ShapeableImageView(getContext());
                ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
                avatar.setShapeAppearanceModel(shape);
                if ("1".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_1);
                } else if ("2".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_2);
                } else if ("3".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_3);
                } else if ("4".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_4);
                } else if ("5".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_5);
                } else if ("6".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_6);
                } else if ("7".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_7);
                } else if ("8".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_8);
                } else if ("9".equals(user.getAvatar())) {
                    avatar.setImageResource(R.mipmap.avatar_9);
                } else {
                    avatar.setImageResource(R.drawable.avatar_add);
                }
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
                ShapeableImageView newRotation = new ShapeableImageView(getContext());
                viewModel.getRotationStarted().observe(getViewLifecycleOwner(), rotationStarted -> {
                    if (!rotationStarted) {
                        ShapeAppearanceModel shape1 = ShapeAppearanceModel.builder()
                                .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
                        newRotation.setShapeAppearanceModel(shape1);
                        newRotation.setBackgroundResource(R.drawable.proshol);
                        newRotation.setImageResource(R.mipmap.refresh_foreground);
                        ConstraintLayout.LayoutParams newRotationLayout = new ConstraintLayout.LayoutParams(180, 180);
                        newRotationLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                        newRotationLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        newRotationLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        newRotationLayout.bottomMargin = 220;
                        newRotation.setLayoutParams(newRotationLayout);
                        newRotation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newrotation();
                            }
                        });
                        binding.container.addView(newRotation);
                    } else {
                        binding.container.removeView(newRotation);
                    }

                });

                status.addView(userView);




                
                linearLayout.addView(status);

            }
            String currentUser = null;
            String firstUser = null;
            String nextUser = null;
            for (Users user : users) {
                if (currentUser == null) {
                    currentUser = user.getTag();
                    firstUser = currentUser;
                }

                String avatar_num = null;
                for (Users useruser : users) {
                    if (Objects.equals(currentUser, useruser.getTag())) {
                        avatar_num = useruser.getAvatar();
                        nextUser = useruser.getTo();
                    }
                }
                ConstraintLayout AVATAR = new ConstraintLayout(getContext());
                ConstraintLayout.LayoutParams AVATARlayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                AVATAR.setLayoutParams(AVATARlayout);


                ShapeableImageView avatar = new ShapeableImageView(getContext());
                ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
                avatar.setShapeAppearanceModel(shape);
                if ("1".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_1);
                } else if ("2".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_2);
                } else if ("3".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_3);
                } else if ("4".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_4);
                } else if ("5".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_5);
                } else if ("6".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_6);
                } else if ("7".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_7);
                } else if ("8".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_8);
                } else if ("9".equals(avatar_num)) {
                    avatar.setImageResource(R.mipmap.avatar_9);
                } else {
                    avatar.setImageResource(R.drawable.avatar_add);
                }
                avatar.setId(View.generateViewId());
                ConstraintLayout.LayoutParams avatarLayout = new ConstraintLayout.LayoutParams(150, 150);
                avatarLayout.setMargins(4, 4, 4, 4);
                avatarLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                avatarLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                avatarLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                avatar.setLayoutParams(avatarLayout);
                AVATAR.addView(avatar);
                currentUser = nextUser;

                TextView arrow = new TextView(getContext());
                arrow.setTypeface(null, Typeface.BOLD);
                arrow.setTextColor(Color.parseColor("#989898"));
                arrow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                arrow.setText(">>");
                ConstraintLayout.LayoutParams arrowLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                arrowLayout.setMargins(4, 4, 4, 4);
                arrowLayout.leftToRight = avatar.getId();
                arrowLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                arrowLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                arrow.setLayoutParams(arrowLayout);
                AVATAR.addView(arrow);

                rotationLinear.addView(AVATAR);
            }
            String avatar_num = null;
            for (Users useruser : users) {
                if (Objects.equals(firstUser, useruser.getTag())) {
                    avatar_num = useruser.getAvatar();
                }
            }

            ConstraintLayout AVATAR = new ConstraintLayout(getContext());
            ConstraintLayout.LayoutParams AVATARlayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            AVATAR.setLayoutParams(AVATARlayout);

            ShapeableImageView avatar = new ShapeableImageView(getContext());
            ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ShapeAppearanceModel.PILL).build();
            avatar.setShapeAppearanceModel(shape);
            if ("1".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_1);
            } else if ("2".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_2);
            } else if ("3".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_3);
            } else if ("4".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_4);
            } else if ("5".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_5);
            } else if ("6".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_6);
            } else if ("7".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_7);
            } else if ("8".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_8);
            } else if ("9".equals(avatar_num)) {
                avatar.setImageResource(R.mipmap.avatar_9);
            } else {
                avatar.setImageResource(R.drawable.avatar_add);
            }
            avatar.setId(View.generateViewId());
            ConstraintLayout.LayoutParams avatarLayout = new ConstraintLayout.LayoutParams(150, 150);
            avatarLayout.setMargins(4, 4, 4, 4);
            avatarLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            avatarLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            avatarLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            avatar.setLayoutParams(avatarLayout);
            AVATAR.addView(avatar);
            rotationLinear.addView(AVATAR);
        });






        return root;
    }

    public void newrotation() {
        new Thread(() -> {
            List<Users> users = viewModel.getRoomUsers().getValue();
            for (Users user : users) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.getTag())
                        .update("game", "Игра отсутствует");
                db.collection("users").document(user.getTag())
                        .update("preview", "0");
                db.collection("users").document(user.getTag())
                        .update("status", "Новая ротация");
                db.collection("users").document(user.getTag())
                        .update("genre", "Отсутствует");
                db.collection("users").document(user.getTag())
                        .update("allow", "yes");
                viewModel.setTo(user.getTag(), "0");
            }

            List<Localusers> localusers = new ArrayList<>();

            for (int j = 0; j < users.size(); j++) {
                localusers.add(new Localusers(users.get(j).getTag(), "0"));
            }

            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                indices.add(i);
            }
            Collections.shuffle(indices);


            for (int i = 0; i < users.size(); i++) {
                int currentIndex = indices.get(i);
                int nextIndex = indices.get((i + 1) % users.size());

                String currentTag = users.get(currentIndex).getTag();
                String nextTag = users.get(nextIndex).getTag();

                localusers.get(currentIndex).setTo(nextTag);
            }

            for (Localusers localuser : localusers) {
                viewModel.setTo(localuser.getTag(), localuser.getTo());
            }
            viewModel.setRotationStarted(true);
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}