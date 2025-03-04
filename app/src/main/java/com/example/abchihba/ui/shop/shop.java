package com.example.abchihba.ui.shop;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abchihba.R;
import com.example.abchihba.databinding.FragmentShopBinding;
import com.example.abchihba.ui.Items;
import com.example.abchihba.ui.Localusers;
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_genre;
import com.example.abchihba.ui.dialog.dialog_swap;
import com.example.abchihba.ui.dialog.dialog_game;
import com.example.abchihba.ui.dialog.dialog_theme;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class shop extends Fragment {

    private FragmentShopBinding binding;
    private ViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        binding = FragmentShopBinding.inflate(inflater, container, false);
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


        LinearLayout linearLayout = binding.items;
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                // Выводим сообщение или заполняем пустое состояние
                TextView emptyView = new TextView(getContext());
                emptyView.setText("No items available");
                linearLayout.addView(emptyView);
                return;
            }

            linearLayout.removeAllViews();
            for (Items item : items) {
                ConstraintLayout itemView = new ConstraintLayout(getContext());
                itemView.setBackgroundResource(R.drawable.design_window);
                itemView.setPadding(30, 20, 30, 20);

                ConstraintLayout.LayoutParams itemLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                itemLayout.setMargins(15, 20, 15, 20);
                itemView.setLayoutParams(itemLayout);

                TextView name = new TextView(getContext());
                name.setText(item.getName());
                name.setTextSize(30);
                name.setTypeface(null, Typeface.BOLD);
                name.setId(View.generateViewId());

                Button buyBtn = new Button(getContext());
                buyBtn.setText("Купить");
                buyBtn.setTextSize(18);
                buyBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.buy_text));
                buyBtn.setTypeface(null, Typeface.BOLD);
                buyBtn.setBackgroundResource(R.drawable.button_buy);
                buyBtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buy_click(item.getName(), item.getPrice());
                    }
                });
                buyBtn.setId(View.generateViewId());

                TextView price = new TextView(getContext());
                price.setText(item.getPrice());
                price.setId(View.generateViewId());
                price.setTypeface(null, Typeface.BOLD);

                ImageView coin = new ImageView(getContext());
                coin.setImageResource(R.drawable.money);
                coin.setId(View.generateViewId());

                TextView description = new TextView(getContext());
                description.setText(item.getDescription());
                description.setId(View.generateViewId());


                ConstraintLayout.LayoutParams nameLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                nameLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                nameLayout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                nameLayout.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                nameLayout.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                name.setLayoutParams(nameLayout);
                itemView.addView(name);

                ConstraintLayout.LayoutParams descriptionLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                descriptionLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                descriptionLayout.topToBottom = name.getId();
                descriptionLayout.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                descriptionLayout.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                description.setLayoutParams(descriptionLayout);

                ConstraintLayout.LayoutParams coinLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                coinLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                coinLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                coinLayout.topToBottom = name.getId();
                coinLayout.width = 50;
                coinLayout.height = 50;
                coin.setLayoutParams(coinLayout);
                itemView.addView(coin);

                ConstraintLayout.LayoutParams priceLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                priceLayout.rightToLeft = coin.getId();
                priceLayout.bottomToBottom = coin.getId();
                priceLayout.topToTop = coin.getId();
                priceLayout.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                priceLayout.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                price.setLayoutParams(priceLayout);
                itemView.addView(price);

                ConstraintLayout.LayoutParams buyLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                buyLayout.width = 400;
                buyLayout.height = 150;
                buyLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                buyLayout.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                buyLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                buyLayout.topToBottom = description.getId();
                buyLayout.topMargin = 30;
                buyBtn.setLayoutParams(buyLayout);

                final boolean[] isExpanded = {false};

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isExpanded[0]) {
                            itemView.removeView(description);
                            coinLayout.topToTop = ConstraintLayout.LayoutParams.UNSET;
                            coinLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                            coinLayout.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                            coinLayout.topToBottom = name.getId();
                            coin.setLayoutParams(coinLayout);
                            itemView.removeView(buyBtn);
                        } else {
                            itemView.addView(description);
                            coinLayout.topToBottom = ConstraintLayout.LayoutParams.UNSET;
                            coinLayout.topToTop = buyBtn.getId();
                            coinLayout.bottomToBottom = buyBtn.getId();
                            coin.setLayoutParams(coinLayout);
                            itemView.addView(buyBtn);
                        }
                        isExpanded[0] = !isExpanded[0];
                    }
                });

                linearLayout.addView(itemView);
            }
        });

        return root;
    }

    private void buy_click(String name, String price) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Users> users = viewModel.getUsers().getValue();
        int int_price = Integer.parseInt(price);
        int int_balance = Integer.parseInt(viewModel.getBalance().getValue());
        boolean flag = true;
        boolean all_flag = false;
        int counter = 0;
        for (Users user : users) {
            if (!user.getGame().equals("Игра отсутствует")) {
                flag = false;
                counter++;
            }
        }
        if (counter == users.size()) {
            all_flag = true;
        }
        //==================ПРОВЕРКА БАЛАНСА========================================================
        if (int_balance >= int_price) {
            //==============РОТАЦИЯ ЗАКОНЧИЛАСЬ, НО ЕЩЕ НЕ НАЧАЛАСЬ=================================
            if (viewModel.getRotationStatus().getValue() != null && !viewModel.getRotationStatus().getValue()) {
                if (Objects.equals(name, "Phanthom Lancer")) {
                    DialogFragment dialog_game = new dialog_game();
                    Bundle genre = new Bundle();
                    genre.putString("genre", "Phanthom Lancer");
                    dialog_game.setArguments(genre);
                    dialog_game.show(getParentFragmentManager(), "dialog_game");
                    getParentFragmentManager().setFragmentResultListener("edit_game_result", getViewLifecycleOwner(), (requestKey, result) -> {
                        String newGame = result.getString("new_game");
                        String preview = result.getString("preview");
                        if (newGame != null) {
                            for (Users user : users) {
                                db.collection("users").document(user.getTag())
                                        .update("genre", "Phanthom Lancer");
                                String tagValue = user.getTag();
                                if (tagValue == null || tagValue.isEmpty()) {
                                    Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
                                } else {
                                    db.collection("users").document(tagValue)
                                            .update("game", newGame);
                                    db.collection("users").document(tagValue)
                                            .update("status", "playing");
                                    db.collection("users").document(tagValue)
                                            .update("preview", preview);
                                    Toast.makeText(getContext(), "Загадано: " + newGame, Toast.LENGTH_SHORT).show();
                                }
                            }
                            viewModel.updateRotationStatus(true);
                            viewModel.setBalance(String.valueOf(int_balance - int_price));
                        }
                    });
                }
                if (Objects.equals(name, "Рогаликук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Roguelike");
                }
                if (Objects.equals(name, "Фильмокук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Фильмокук");
                }
                if (Objects.equals(name, "Сериалокук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Сериалокук");
                }
                if (Objects.equals(name, "Геймпадокук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Геймпадокук");
                }
                if (Objects.equals(name, "Выжикук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Survival");
                }
                if (Objects.equals(name, "Музыкук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Музыкук");
                }
                if (Objects.equals(name, "Анимекук")) {
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                    newrotation("Анимекук");
                }
                if (Objects.equals(name, "Тематическая ротация")) {
                    DialogFragment dialog_theme = new dialog_theme();
                    dialog_theme.show(getParentFragmentManager(), "dialog_theme");
                    getParentFragmentManager().setFragmentResultListener("theme_result", getViewLifecycleOwner(), (requestKey, result) -> {
                        String theme = result.getString("theme");
                        if (theme != null) {
                            newrotation(theme);
                            viewModel.setBalance(String.valueOf(int_balance - int_price));
                        }
                    });
                }
            }
            //=============ЕЩЕ НИ ОДНА ИГРА НЕ ЗАГАДАНА=============================================
            if (flag) {

            }
            //=============ВСЕ ИГРЫ ЗАГАДАНЫ========================================================
            if (all_flag) {
                if (Objects.equals(name, "Свап играми")) {
                    if (Objects.equals(viewModel.getStatus().getValue(), "done")) {
                        Toast.makeText(requireContext(), "Вы уже прошли свою игру", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DialogFragment dialog_swap = new dialog_swap();
                    dialog_swap.show(getParentFragmentManager(), "dialog_swap");

                    getParentFragmentManager().setFragmentResultListener("drop_result", this, (requestKey, result) -> {
                        if (!result.isEmpty()) {
                            String swap = result.getString("result");
                            for (Users user : users) {
                                if (Objects.equals(user.getTag(), swap)) {
                                    if (user.getStatus().equals("done")) {
                                        Toast.makeText(requireContext(), "Этот пользователь уже прошел свою игру", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }


                            String my_genre = viewModel.getGenre().getValue();
                            String my_game = viewModel.getGame().getValue();
                            String my_preview = viewModel.getPreview().getValue();

                            String genre = "";
                            String game = "";
                            String preview = "";

                            for (Users user : users) {
                                if (Objects.equals(user.getTag(), swap)) {
                                    genre = user.getGenre();
                                    game = user.getGame();
                                    preview = user.getPreview();
                                }
                            }
                            db.collection("users").document(swap)
                                    .update("genre", my_genre);
                            db.collection("users").document(swap)
                                    .update("game", my_game);
                            db.collection("users").document(swap)
                                    .update("preview", my_preview);
                            db.collection("users").document(viewModel.getTag().getValue())
                                    .update("genre", genre);
                            db.collection("users").document(viewModel.getTag().getValue())
                                    .update("game", game);
                            db.collection("users").document(viewModel.getTag().getValue())
                                    .update("preview", preview);
                            viewModel.setBalance(String.valueOf(int_balance - int_price));
                        }
                    });
                }
            }
            //=============У ВЫПАВШЕГО ЧЕЛОВЕКА ЕЩЕ НЕ ЗАГАДАНА ИГРА================================
            for (Users user : users) {
                if (Objects.equals(user.getTag(), viewModel.getTo().getValue())) {
                    if (user.getGame().equals("Игра отсутствует")) {
                        if (Objects.equals(name, "Загадываешь игру без колеса жанров.")) {
                            DialogFragment dialog_game = new dialog_game();
                            Bundle genre = new Bundle();
                            genre.putString("genre", "Свободный");
                            dialog_game.setArguments(genre);
                            dialog_game.show(getParentFragmentManager(), "dialog_game");
                            getParentFragmentManager().setFragmentResultListener("edit_game_result", getViewLifecycleOwner(), (requestKey, result) -> {
                                String newGame = result.getString("new_game");
                                String preview = result.getString("preview");
                                if (newGame != null) {
                                    db.collection("users").document(viewModel.getTo().getValue())
                                            .update("genre", "Свободный");
                                    String tagValue = viewModel.getTo().getValue();
                                    if (tagValue == null || tagValue.isEmpty()) {
                                        Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
                                    } else {
                                        db.collection("users").document(tagValue)
                                                .update("game", newGame);
                                        db.collection("users").document(tagValue)
                                                .update("status", "playing");
                                        db.collection("users").document(tagValue)
                                                .update("preview", preview);
                                        Toast.makeText(getContext(), "Загадано: " + newGame, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                viewModel.setBalance(String.valueOf(int_balance - int_price));
                            });
                        }
                        if (Objects.equals(name, "100%")) {
                            DialogFragment dialog_game = new dialog_game();
                            Bundle genre = new Bundle();
                            genre.putString("genre", "100%");
                            dialog_game.setArguments(genre);
                            dialog_game.show(getParentFragmentManager(), "dialog_game");
                            getParentFragmentManager().setFragmentResultListener("edit_game_result", getViewLifecycleOwner(), (requestKey, result) -> {
                                String newGame = result.getString("new_game");
                                String preview = result.getString("preview");
                                if (newGame != null) {
                                    db.collection("users").document(viewModel.getTo().getValue())
                                            .update("genre", "100%");
                                    String tagValue = viewModel.getTo().getValue();
                                    if (tagValue == null || tagValue.isEmpty()) {
                                        Log.e("ViewModel", "Tag is null or empty. Cannot update Firestore document.");
                                    } else {
                                        db.collection("users").document(tagValue)
                                                .update("game", newGame);
                                        db.collection("users").document(tagValue)
                                                .update("status", "playing");
                                        db.collection("users").document(tagValue)
                                                .update("preview", preview);
                                        Toast.makeText(getContext(), "Загадано: " + newGame, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                viewModel.setBalance(String.valueOf(int_balance - int_price));
                            });
                        }
                    }
                }
            }
            //=============У МЕНЯ ЕЩЕ НЕТ ЖАНРА=====================================================
            if (Objects.equals(viewModel.getGenre().getValue(), "Отсутствует")){
                if(Objects.equals(name,"Выбор жанра")) {
                    DialogFragment dialog_genre = new dialog_genre();
                    dialog_genre.show(getParentFragmentManager(), "dialog_genre");
                    getParentFragmentManager().setFragmentResultListener("edit_genre_result", getViewLifecycleOwner(), (requestKey, result) -> {
                        String newGenre = result.getString("new_genre");
                        if (newGenre != null && !newGenre.equals("Жанр")) {
                            viewModel.setGenre(newGenre);
                            viewModel.setAllow("no");
                            viewModel.setBalance(String.valueOf(int_balance - int_price));
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "У вас уже есть жанр", Toast.LENGTH_SHORT).show();
            }
            //=============У МЕНЯ СТАТУС ПРОХОДИТ===================================================
            if (Objects.equals(viewModel.getStatus().getValue(), "playing")) {
                if (Objects.equals(name, "Скип")) {
                    viewModel.setStatus("done");
                    viewModel.setBalance(String.valueOf(int_balance - int_price));
                }
            } else if (Objects.equals(viewModel.getStatus().getValue(), "done")) {
                Toast.makeText(getContext(), "Вы уже прошли свою игру", Toast.LENGTH_SHORT).show();
            } else if (Objects.equals(viewModel.getStatus().getValue(), "drop")) {
                Toast.makeText(getContext(), "Вы уже дропнули свою игру", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Недостаточно средств", Toast.LENGTH_SHORT).show();
        }
    }

    public void newrotation(String type) {
        new Thread(() -> {
            List<Users> users = viewModel.getUsers().getValue();
            for (Users user : users) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.getTag())
                        .update("game", "Игра отсутствует");
                db.collection("users").document(user.getTag())
                        .update("preview", "0");
                db.collection("users").document(user.getTag())
                        .update("status", "Новая ротация");
                db.collection("users").document(user.getTag())
                        .update("genre", type);
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
            viewModel.updateRotationStatus(true);
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}