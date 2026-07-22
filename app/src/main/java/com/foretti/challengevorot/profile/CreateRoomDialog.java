package com.foretti.challengevorot.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;

public class CreateRoomDialog {
    private Context context;
    private AlertDialog dialog;
    private EditText etName;
    private EditText etPassword;
    private Button btnCancel;
    private Button btnConfirm;
    public CreateRoomDialog(Context context) {
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_room, null);

        etName = dialogView.findViewById(R.id.etName);
        etPassword = dialogView.findViewById(R.id.etPassword);
        btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (!name.isEmpty() && !password.isEmpty()) {
                WebSocketManager.getInstance().send("{\"type\":\"create_room\",\"name\":\"" + name + "\",\"password\":\"" + password + "\"}");
            }
            dialog.dismiss();
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }


}
