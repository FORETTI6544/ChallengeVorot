package com.example.abchihba;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abchihba.ui.ViewModel;
import com.example.abchihba.ui.dialog.dialog_avatar;
import com.example.abchihba.ui.dialog.dialog_name;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.abchihba.databinding.ActivityMainBinding;
import com.google.android.material.imageview.ShapeableImageView;

import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewModel viewModel = new ViewModelProvider(MainActivity.this).get(ViewModel.class);

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
            viewModel.getBalance().observe(this, balance -> binding.balance.setText(balance));
            ShapeableImageView avatar = binding.avatar;
            avatar.setOnClickListener(view -> {
                DialogFragment dialog_avatar = new dialog_avatar();
                dialog_avatar.show(getSupportFragmentManager(), "dialog_edit_avatar");
            });

            getSupportFragmentManager().setFragmentResultListener("edit_avatar_result", this, (requestKey, result) -> {
                String newAvatar = result.getString("new_avatar");
                if (newAvatar != null) {
                    viewModel.setAvatar(newAvatar);
                    Toast.makeText(this, "Аватар изменён!", Toast.LENGTH_SHORT).show();
                }
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
}