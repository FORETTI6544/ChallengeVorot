package com.foretti.challengevorot.rotation;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.models.Game;
import com.foretti.challengevorot.models.User;
import com.foretti.challengevorot.network.WebSocketManager;

import java.util.ArrayList;
import java.util.List;

public class AskGameDialog {
    private Context context;
    private User user;
    private OnSaveListener listener;
    private List<Game> gameList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public interface OnSaveListener {
        void onSave(String game, String gamePreview);
    }

    public AskGameDialog(Context context, User user, OnSaveListener listener) {
        this.context = context;
        this.user = user;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_ask_game, null);

        TextView textGenre = dialogView.findViewById(R.id.textGenre);
        AutoCompleteTextView autoCompleteGame = dialogView.findViewById(R.id.autoCompleteGame);
        EditText editTextPreview = dialogView.findViewById(R.id.editTextPreview);
        LinearLayout suggestionsContainer = dialogView.findViewById(R.id.suggestionsContainer);

        // Устанавливаем жанр пользователя
        textGenre.setText("Жанр: " + (user.genre != null ? user.genre : "Не указан"));

        // Настраиваем адаптер для автодополнения
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        autoCompleteGame.setAdapter(adapter);
        autoCompleteGame.setThreshold(2);

        // Устанавливаем клик на элемент списка
        autoCompleteGame.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            for (Game game : gameList) {
                if (game.game_name.equals(selectedItem)) {
                    autoCompleteGame.setText(game.game_name);
                    editTextPreview.setText(game.preview_image);
                    suggestionsContainer.setVisibility(View.GONE);
                    break;
                }
            }
        });

        // Устанавливаем TextWatcher для отслеживания ввода
        autoCompleteGame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    // Отправляем запрос на сервер
                    WebSocketManager.getInstance().send("{\"type\":\"search_games\",\"query\":\"" + s.toString() + "\"}");

                    // Устанавливаем callback
                    WebSocketManager.getInstance().setGamesCallback(games -> {
                        // Обновление UI должно происходить на основном потоке
                        if (context instanceof android.app.Activity) {
                            ((android.app.Activity) context).runOnUiThread(() -> {
                                gameList.clear();
                                gameList.addAll(games);

                                List<String> gameNames = new ArrayList<>();
                                for (Game game : games) {
                                    gameNames.add(game.game_name);
                                }

                                adapter.clear();
                                adapter.addAll(gameNames);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String game = autoCompleteGame.getText().toString();
            String preview = editTextPreview.getText().toString();
            if (listener != null) {
                listener.onSave(game, preview);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

}
