package com.example.movieapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.movieapp.favorite.FavoritesFragment;
import com.example.movieapp.home.HomeFragment;
import com.example.movieapp.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        navigateToHome();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        navigateToProfile();
                        break;
                    case R.id.action_home:
                        navigateToHome();
                        break;
                    case R.id.action_favorite:
                        navigateToFavorites();
                        break;
                }
                return true;
            }

        });

    }

    private void navigateToProfile() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance())
                .commit();
    }

    private void navigateToHome() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commit();
    }

    private void navigateToFavorites() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, FavoritesFragment.newInstance())
                .commit();
    }
}
