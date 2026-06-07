package com.foretti.challengevorot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNavView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setPadding(0, 0, 0, 0);
        bottomNavView.setItemIconTintList(null);
        bottomNavView.setItemActiveIndicatorEnabled(false);

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHost.getNavController();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateMenuVisibility(destination.getId());
        });

        // Связываем BottomNavigationView с NavController
        NavigationUI.setupWithNavController(bottomNavView, navController);
        bottomNavView.post(() -> {
            NavDestination current = navController.getCurrentDestination();
            if (current != null) {
                updateMenuVisibility(current.getId());
            } else {
                // Если всё ещё null, скрываем меню по умолчанию
                bottomNavView.setVisibility(View.GONE);
            }
        });
    }
    private void updateMenuVisibility(int destinationId) {
        if (destinationId == R.id.ProfileWithRoomFragment ||
                destinationId == R.id.RotationFragment ||
                destinationId == R.id.WheelFragment ||
                destinationId == R.id.MarketFragment ||
                destinationId == R.id.ReviewsFragment) {
            bottomNavView.setVisibility(View.VISIBLE);
        } else {
            bottomNavView.setVisibility(View.GONE);
        }
    }
}