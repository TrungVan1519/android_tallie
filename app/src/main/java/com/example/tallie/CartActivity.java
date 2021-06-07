package com.example.tallie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.tallie.adapters.CartAdapter;
import com.example.tallie.models.Cart;
import com.example.tallie.utils.DBHandler;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    List<Cart> carts;
    CartAdapter adapter;
    RecyclerView rcvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rcvCart = findViewById(R.id.rcvCart);

        // TODO: set up RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        carts = new DBHandler(this).allRecords();
        adapter = new CartAdapter(this, R.layout.layout_cart_row, carts);
        rcvCart.setAdapter(adapter);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
    }
}