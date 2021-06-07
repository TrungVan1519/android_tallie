package com.example.tallie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tallie.fragments.HomeFragment;
import com.example.tallie.fragments.NotificationFragment;
import com.example.tallie.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navMenu = findViewById(R.id.navMenu);

        // TODO: handle e
        loadFragment(new HomeFragment());
        navMenu.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navItemHome: {
                    loadFragment(new HomeFragment());
                    return true;
                }
                case R.id.navItemNotification: {
                    loadFragment(new NotificationFragment());
                    return true;
                }
                case R.id.navItemProfile: {
                    loadFragment(new ProfileFragment());
                    return true;
                }
                default: {
                    Toast.makeText(this, "Nothing to do", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.navItemSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // IMPORTANT: prevent onQueryTextSubmit() method called twice
                searchView.clearFocus();

                String msg = "Submit: " + query;
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navItemCart) {
            startActivity(new Intent(this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}