package com.example.abchihba;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.abchihba.ui.ViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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