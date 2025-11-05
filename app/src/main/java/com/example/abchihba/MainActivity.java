package com.example.abchihba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_name;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.abchihba.databinding.ActivityMainBinding;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewModel viewModel = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
        ActivityResultLauncher<String> pickImageLauncher;
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                byte[] bytes = bitmapToByteArray(cropToSquare(bitmap), 50, Bitmap.CompressFormat.JPEG);
                                String base64 = Base64.getEncoder().encodeToString(bytes);
                                viewModel.setAvatar(base64);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        if (getIntent().getStringExtra("tag") == null) {
            Intent intent = new Intent(MainActivity.this, signin.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else {
            viewModel.setTag(getIntent().getStringExtra("tag"));
            viewModel.getName().observe(this, name -> {
                binding.name.setText(name);
            });
            viewModel.getAvatar().observe(this, avatar -> {
                binding.avatar.setImageBitmap(base64ToBitmap(avatar));
            });
            viewModel.getBalance().observe(this, balance -> binding.balance.setText(balance));
            ShapeableImageView avatar = binding.avatar;
            avatar.setOnClickListener(view -> {
                pickImageLauncher.launch("image/*");
            });

            TextView edit = binding.name;
            edit.setOnClickListener(view -> {
                DialogFragment dialog_name = new dialog_name();
                dialog_name.show(getSupportFragmentManager(), "dialog_edit_name");
            });

            getSupportFragmentManager().setFragmentResultListener("edit_name_result", this, (requestKey, result) -> {
                String newName = result.getString("new_name");
                if (newName != null) {
                    viewModel.setName(newName);
                    Toast.makeText(this, "Имя изменено на: " + newName, Toast.LENGTH_SHORT).show();
                }
            });




            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.setBackgroundResource(R.drawable.design_window);
            ConstraintLayout.LayoutParams navLayout = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            navLayout.setMargins(10, 0, 10, 20);
            navView.setPadding(20, 0, 20, 0);
            navLayout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            navView.setLayoutParams(navLayout);
            navView.setItemIconTintList(null);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            viewModel.getRoom().observe(this, room -> {
                // Очищаем текущее меню
                navView.getMenu().clear();
                // В зависимости от значения room загружаем нужный файл меню
                if ("0".equals(room)) {
                    navView.inflateMenu(R.menu.bottom_nav_menu_cut);
                } else {
                    navView.inflateMenu(R.menu.bottom_nav_menu);
                }
                // Повторно привязываем navController к меню BottomNavigationView
                NavigationUI.setupWithNavController(navView, navController);
            });

            if (getIntent().getStringExtra("tag") != null) {
                NavigationUI.setupWithNavController(binding.navView, navController);
            } else {
                Toast.makeText(MainActivity.this, "Вход не выполнен", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static byte[] bitmapToByteArray(Bitmap bitmap, int quality, Bitmap.CompressFormat format) {
        if (bitmap == null || bitmap.isRecycled()) {
            return new byte[0];
        }
        quality = Math.max(0, Math.min(quality, 100));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            if (!bitmap.compress(format, quality, byteArrayOutputStream)) {
                return new byte[0];
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignored) {

            }
        }
    }

    public static Bitmap base64ToBitmap(String base64String) {
        try {
            // Декодируем Base64 в массив байтов
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            // Конвертируем байты в Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Bitmap cropToSquare(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        int size = Math.min(width, height);

        int startX = (width - size) / 2;
        int startY = (height - size) / 2;

        return Bitmap.createBitmap(originalBitmap, startX, startY, size, size);
    }
}