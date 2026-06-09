package com.foretti.challengevorot.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.WebSocketManager;

public class ChangeNameDialog {
    private Context context;
    private EditText editTextName;
    private AlertDialog dialog;

    public ChangeNameDialog(Context context) {
        this.context = context;
    }

    public void show(String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_change_name, null);

        editTextName = dialogView.findViewById(R.id.editTextName);
        editTextName.setText(currentName);

        Button btnCancel = dialogView.findViewById(R.id.btnCancelName);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmName);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String newName = editTextName.getText().toString().trim();
            if (!newName.isEmpty()) {
                WebSocketManager.getInstance().send("{\"type\":\"set_name\",\"name\":\"" + newName + "\"}");
            }
            dialog.dismiss();
        });

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }
}
