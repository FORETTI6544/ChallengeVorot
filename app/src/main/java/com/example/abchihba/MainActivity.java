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
            Menu menu = navView.getMenu();
            viewModel.getRoom().observe(this, room -> {
                        if (Objects.equals(viewModel.getRoom().getValue(), "0")) {
                            menu.findItem(R.id.navigation_wheel).setVisible(false);
                            menu.findItem(R.id.navigation_shop).setVisible(false);
                            menu.findItem(R.id.navigation_review).setVisible(false);
                            menu.findItem(R.id.navigation_rotation).setIcon(R.drawable.icon);
                            menu.findItem(R.id.navigation_rotation).setTitle(R.string.title_rooms);
                        } else {
                            menu.findItem(R.id.navigation_wheel).setVisible(true);
                            menu.findItem(R.id.navigation_shop).setVisible(true);
                            menu.findItem(R.id.navigation_review).setVisible(true);
                            menu.findItem(R.id.navigation_rotation).setIcon(R.drawable.icon_rotation);
                            menu.findItem(R.id.navigation_rotation).setTitle(R.string.title_rotation);
                        }
                    });
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_profile,
                    R.id.navigation_rotation,
                    R.id.navigation_wheel,
                    R.id.navigation_shop,
                    R.id.navigation_review)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            if (getIntent().getStringExtra("tag") != null) {
                NavigationUI.setupWithNavController(binding.navView, navController);
                navView.setOnItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.navigation_rotation) {
                        // Проверяем значение из ViewModel
                        if (Objects.equals(viewModel.getRoom().getValue(), "0")) {
                            // Если room == 0 → открыть fragment_rooms
                            navController.navigate(R.id.navigation_rooms);
                        } else {
                            // Иначе — обычный rotation
                            navController.navigate(R.id.navigation_rotation);
                        }
                        return true;

                    }
                    return NavigationUI.onNavDestinationSelected(item, navController);
                });
            } else {
                Toast.makeText(MainActivity.this, "Вход не выполнен", Toast.LENGTH_SHORT).show();
            }
        }
    }
}