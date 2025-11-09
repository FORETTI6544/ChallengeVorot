package com.example.abchihba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;
import android.util.Patterns;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView texttosignin = findViewById(R.id.texttosignin);
        texttosignin.setOnClickListener(view -> {
            Intent intent = new Intent(signup.this, signin.class);
            startActivity(intent);
            finish();
        });

        EditText email = findViewById(R.id.emailup);
        EditText password = findViewById(R.id.passwordup);
        Button signup = findViewById(R.id.sign_up);
        signup.setOnClickListener(view -> {
            String emailInput = email.getText().toString().trim();
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                Toast.makeText(signup.this, "Введите корректный Email", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> user = new HashMap<>();
                user.put("email", email.getText().toString());
                user.put("password", password.getText().toString());
                user.put("name", "Изменить имя");
                user.put("avatar", "0");
                user.put("allow", "yes");
                user.put("rerolls", "2");
                user.put("balance", "0");
                user.put("genre", "Отсутствует");
                user.put("game", "Игра отсутствует");
                user.put("status", "new");
                user.put("to", "0");
                user.put("preview", "0");
                user.put("readiness", false);
                user.put("room", "0");
                user.put("started", 0);


                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            startActivity(new Intent(signup.this, signin.class));
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(signup.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show());
            }
        });

    }
}