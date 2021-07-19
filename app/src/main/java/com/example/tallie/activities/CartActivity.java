package com.example.tallie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.adapters.CartAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.Error;
import com.example.tallie.models.Order;
import com.example.tallie.models.OrderList;
import com.example.tallie.services.BookService;
import com.example.tallie.services.OrderService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.example.tallie.utils.SwipeToDeleteCallback;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    OrderService orderService = RetrofitClient.getInstance("https://tallie-shipping.herokuapp.com/").create(OrderService.class);

    int orderCount = 0;
    TextView txtCart;
    RecyclerView rcvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtCart = findViewById(R.id.txtCart);
        rcvCart = findViewById(R.id.rcvCart);

        orderService.allOrders(SharedPreferencesHandler.loadAppData(this)).enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(@NonNull Call<OrderList> call, @NonNull Response<OrderList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderList orderList = response.body();
                    ArrayList<Order> orders = orderList.getOrders() == null ? new ArrayList<>() : orderList.getOrders();
                    rcvCart.setAdapter(new CartAdapter(orders, (v, position) -> getBookDetail(orders.get(position).getProductId())));
                    rcvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));

                    orderCount = orders.size();
                    txtCart.setText(txtCart.getText().toString().concat(": " + orderCount));
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderList> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        new ItemTouchHelper(new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (rcvCart.getAdapter() == null) return;
                ((CartAdapter) rcvCart.getAdapter()).deleteItem(CartActivity.this, viewHolder.getAdapterPosition(), txtCart);
            }
        }).attachToRecyclerView(rcvCart);
    }

    private void getBookDetail(int bookId) {
        bookService.getBookDetail(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    Intent i = new Intent(CartActivity.this, BookDetailActivity.class);
                    i.putExtra("book", book);
                    startActivity(i);
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
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
                Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}