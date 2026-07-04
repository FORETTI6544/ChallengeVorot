package com.foretti.challengevorot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import com.foretti.challengevorot.converters.Converter;
import com.foretti.challengevorot.network.WebSocketManager;
import com.foretti.challengevorot.profile.ChangeNameDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNavView;
    private ConstraintLayout topPanel;
    private TextView username;
    private ShapeableImageView avatar;
    private TextView balance;
    WebSocketManager.UserCallback userCallback;
    ActivityViewmodel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        viewmodel = new ViewModelProvider(this).get(ActivityViewmodel.class);
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
                                byte[] bytes = Converter.bitmapToByteArray(Converter.cropToSquare(bitmap), 50, Bitmap.CompressFormat.JPEG);
                                Toast.makeText(MainActivity.this, Arrays.toString(bytes), Toast.LENGTH_SHORT).show();
                                String base64 = Base64.getEncoder().encodeToString(bytes);
                                WebSocketManager.getInstance().send("{\"type\":\"set_avatar\",\"base64\":\"" + base64 + "\"}");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


        topPanel = findViewById(R.id.topPanel);
        username = findViewById(R.id.name);
        avatar = findViewById(R.id.avatar);
        balance = findViewById(R.id.balance);

        username.setOnClickListener(v -> {
            showChangeNameDialog();
        });

        avatar.setOnClickListener(view -> {
            pickImageLauncher.launch("image/*");
        });



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
        NavigationUI.setupWithNavController(bottomNavView, navController);
        bottomNavView.post(() -> {
            NavDestination current = navController.getCurrentDestination();
            if (current != null) {
                updateMenuVisibility(current.getId());
            } else {
                bottomNavView.setVisibility(View.GONE);
            }
        });

        WebSocketManager.getInstance().setUserCallback((userId, username, avatar, balance,
                                                        askTo, readiness, genre, game, gamePreview,
                                                        gameStatus, gameStartedDate, allowWheelSpinning, rerollsCount) -> {
            viewmodel.setUser(userId, username, avatar, balance,
                    askTo, readiness, genre, game, gamePreview,
                    gameStatus, gameStartedDate, allowWheelSpinning, rerollsCount);
        });
        WebSocketManager.getInstance().send("{\"type\":\"get_user\"}");
        viewmodel.getUsername().observe(this, n -> username.setText(n));
        viewmodel.getAvatar().observe(this, a -> avatar.setImageBitmap(Converter.base64ToBitmap(a)));
        viewmodel.getBalance().observe(this, b -> balance.setText(String.valueOf(b)));


    }

    private void showChangeNameDialog() {
        ChangeNameDialog dialog = new ChangeNameDialog(this);
        dialog.show(username.getText().toString());
    }


    private void updateMenuVisibility(int destinationId) {
        if (destinationId == R.id.ProfileWithRoomFragment ||
                destinationId == R.id.RotationFragment ||
                destinationId == R.id.WheelFragment ||
                destinationId == R.id.MarketFragment ||
                destinationId == R.id.ReviewsFragment) {
            topPanel.setVisibility(View.VISIBLE);
            bottomNavView.setVisibility(View.VISIBLE);
        } else if (destinationId == R.id.ProfileWithoutRoomFragment) {
            topPanel.setVisibility(View.VISIBLE);
            bottomNavView.setVisibility(View.GONE);
        } else {
            topPanel.setVisibility(View.GONE);
            bottomNavView.setVisibility(View.GONE);
        }
    }
}