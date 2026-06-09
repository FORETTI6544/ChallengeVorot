package com.foretti.challengevorot.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;

public class ConfirmDropDialog {
    private Context context;
    private String gameName;
    private AlertDialog dialog;

    public interface OnConfirmListener {
        void onConfirm();
    }

    private OnConfirmListener listener;

    public ConfirmDropDialog(Context context, String gameName, OnConfirmListener listener) {
        this.context = context;
        this.gameName = gameName != null ? gameName : "Неизвестная игра";
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_confirm_drop, null);

        TextView textConfirmMessage = dialogView.findViewById(R.id.textConfirmMessage);
        View btnCancel = dialogView.findViewById(R.id.btnCancelDrop);
        View btnConfirm = dialogView.findViewById(R.id.btnConfirmDrop);

        textConfirmMessage.setText("Вы уверены, что хотите дропнуть " + gameName + "?");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            String message = "{\"type\":\"drop_game\"}";
            WebSocketManager.getInstance().send(message);
            dialog.dismiss();
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }
}
