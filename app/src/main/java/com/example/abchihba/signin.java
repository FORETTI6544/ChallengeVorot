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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class signin extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_in_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView texttosignup = findViewById(R.id.texttosignup);
        texttosignup.setOnClickListener(view -> {
            Intent intent = new Intent(signin.this, signup.class);
            startActivity(intent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText email = findViewById(R.id.emailin);
        EditText password = findViewById(R.id.passwordin);
        Button signinButton = findViewById(R.id.sign_in);

        signinButton.setOnClickListener(view -> {
            final boolean[] have = {false};

            db.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("email").equals(email.getText().toString())) {
                                    if (document.getString("password").equals(password.getText().toString())) {
                                        Intent intent = new Intent(signin.this, MainActivity.class);
                                        intent.putExtra("tag", document.getId());
                                        startActivity(intent);
                                        finish();
                                        have[0] = true;
                                        break;
                                    }
                                }
                            }
                            if (!have[0]) {
                                Toast.makeText(signin.this, "Email не существует или пароль неверный", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(signin.this, "Ошибка входа", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}