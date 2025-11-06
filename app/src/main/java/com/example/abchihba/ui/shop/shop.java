package com.example.abchihba.ui.shop;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.abchihba.ui.Users;
import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_genre;
import com.example.abchihba.ui.dialog.dialog_swap;
import com.example.abchihba.ui.dialog.dialog_game;
import com.example.abchihba.ui.dialog.dialog_theme;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class shop extends Fragment {

    private FragmentShopBinding binding;
    private ViewModel viewModel;
    private List<Items> items;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        items = new ArrayList<>();
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout linearLayout = binding.items;
        linearLayout.removeAllViews();
        createItems();
        for (Items item : items) {
            linearLayout.addView(createItemFrame(item));
        }

        return root;
    }

    private void buy_click(String name, String price) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Users> users = viewModel.getRoomUsers().getValue();
        int intPrice = Integer.parseInt(price);
        int intBalance = Integer.parseInt(viewModel.getBalance().getValue());

        //======================ПРОВЕРКА ТИПА СЛЕДУЮЩЕЙ РОТАЦИИ=====================================
        if (Objects.equals(viewModel.getNextRotationType().getValue(), "Отсутствует")) {
            //==================ПРОВЕРКА БАЛАНСА====================================================
            if (intBalance >= intPrice) {
                //==============СЛЕДУЮЩАЯ РОТАЦИЯ===================================================
                if (Objects.equals(name, "Phanthom Lancer")) {
                    DialogFragment dialog_game = new dialog_game();
                    Bundle genre = new Bundle();
                    genre.putString("genre", "Phanthom Lancer");
                    dialog_game.setArguments(genre);
                    dialog_game.show(getParentFragmentManager(), "dialog_game");
                    getParentFragmentManager().setFragmentResultListener("edit_game_result", getViewLifecycleOwner(), (requestKey, result) -> {
                        String newGame = result.getString("new_game");
                        String preview = result.getString("preview");
                        if (newGame != null && preview != null) {
                            viewModel.setNextRotationType(name);
                            viewModel.setNextGame(newGame, preview);
                            viewModel.setBalance(String.valueOf(intBalance - intPrice));
                        }
                    });
                }
                if (Objects.equals(name, "Рогаликук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Фильмокук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Сериалокук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Геймпадокук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Выжикук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Музыкук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Анимекук")) {
                    viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    viewModel.setNextRotationType(name);
                }
                if (Objects.equals(name, "Тематическая ротация")) {
                    DialogFragment dialog_theme = new dialog_theme();
                    dialog_theme.show(getParentFragmentManager(), "dialog_theme");
                    getParentFragmentManager().setFragmentResultListener("theme_result", getViewLifecycleOwner(), (requestKey, result) -> {
                        String theme = result.getString("theme");
                        if (theme != null) {
                            viewModel.setNextRotationType(theme);
                            viewModel.setBalance(String.valueOf(intBalance - intPrice));
                        }
                    });
                }

                //=============ЕЩЕ НИ ОДНА ИГРА НЕ ЗАГАДАНА=============================================
                if (viewModel.nobodySpecifiedGame.getValue()) {
                }
                //=============ВСЕ ИГРЫ ЗАГАДАНЫ========================================================
                if (viewModel.everybodySpecifiedGame.getValue()) {
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
                                viewModel.setBalance(String.valueOf(intBalance - intPrice));
                            }
                        });
                    }
                }
                //=============У ВЫПАВШЕГО ЧЕЛОВЕКА ЕЩЕ НЕ ЗАГАДАНА ИГРА================================
                if (viewModel.targetUserGameIsEmpty.getValue()) {
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
                            viewModel.setBalance(String.valueOf(intBalance - intPrice));
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
                            viewModel.setBalance(String.valueOf(intBalance - intPrice));
                        });
                    }
                }
                //=============У МЕНЯ ЕЩЕ НЕТ ЖАНРА=====================================================
                if (viewModel.iDontHaveGenre.getValue()) {
                    if (Objects.equals(name, "Выбор жанра")) {
                        DialogFragment dialog_genre = new dialog_genre();
                        dialog_genre.show(getParentFragmentManager(), "dialog_genre");
                        getParentFragmentManager().setFragmentResultListener("edit_genre_result", getViewLifecycleOwner(), (requestKey, result) -> {
                            String newGenre = result.getString("new_genre");
                            if (newGenre != null && !newGenre.equals("Жанр")) {
                                viewModel.setGenre(newGenre);
                                viewModel.setAllow("no");
                                viewModel.setBalance(String.valueOf(intBalance - intPrice));
                            }
                        });
                    }
                }
                //=============У МЕНЯ СТАТУС ПРОХОДИТ===================================================
                if (viewModel.myStatusIsPlaying.getValue()) {
                    if (Objects.equals(name, "Скип")) {
                        viewModel.setStatus("done");
                        viewModel.setBalance(String.valueOf(intBalance - intPrice));
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Недостаточно средств", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Кто то уже купил предмет для следующей ротации", Toast.LENGTH_SHORT).show();
        }
    }

    public void createItems() {
        items.add(new Items("Phanthom Lancer", "Все проходят одну и ту же игру, выбирает купивший. Можно купить только до начала новой ротации.", "100"));
        items.add(new Items("Свап играми", "Меняешься играми с выбранным человеком. Можно купить только если у всех загаданы игры.", "60"));
        items.add(new Items("Рогаликук", "Загадываем рогалики, каждый делает 100 забегов, специально сливать нельзя. Можно купить только до начала новой ротации.", "120"));
        items.add(new Items("Фильмокук", "Все участники вместо игр смотрят фильмы. Можно купить только до начала новой ротации.", "80"));
        items.add(new Items("Геймпадокук", "Загадываем и проходим игры на геймпаде. Можно купить только до начала новой ротации.", "40"));
        items.add(new Items("Выжикук", "Загадываем сурвайвл, выживаем 50 дней. Можно купить только до начала новой ротации.", "130"));
        items.add(new Items("Сериалокук", "Все участники вместо игр смотрят сериалы или мульт-сериалы. Можно купить только до начала новой ротации.", "100"));
        items.add(new Items("Загадываешь игру без колеса жанров.", "Можно купить если ещё не загадал игру.", "60"));
        items.add(new Items("Выбор жанра", "Выбрать какой жанр тебе загадают. Можно купить если тебе ещё не загадали игру.", "60"));
        items.add(new Items("100%", "Загадать прохождение игры на 100%. Можно купить если ты ещё не загадал игру.", "250"));
        items.add(new Items("Тематическая ротация", "Игры на определённый тему. Тему выбираешь сам. Можно купить только до начала новой ротации.", "100"));
        items.add(new Items("Скип", "Скипнуть свою игру. Можно купить если ещё не прошёл свою игру.", "60"));
        items.add(new Items("Музыкук", "Вместо игр все слушают музыку. Нужно прослушать все альбомы определённого артиста. Можно купить только до начала новой ротации.", "100"));
        items.add(new Items("Анимекук", "Вместо игр все смотрят аниме. Можно купить только до начала новой ротации.", "100"));
    }

    public ConstraintLayout createItemFrame(Items item) {
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
        buyBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.secondaryText));
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

        return itemView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}