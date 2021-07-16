package com.example.tallie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tallie.R;
import com.example.tallie.adapters.BookListViewAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.Error;
import com.example.tallie.services.BookService;
import com.example.tallie.utils.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    ListView lsvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        BookList bookList = (BookList) getIntent().getSerializableExtra("bookList");
        ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();

        // TODO: handle events
        lsvBooks = findViewById(R.id.lsvBooks);
        lsvBooks.setAdapter(new BookListViewAdapter<>(this, R.layout.row_book, books));
        lsvBooks.setOnItemClickListener((parent, view, position, id) -> getBookDetail(books.get(position).getId()));
    }

    private void getBookDetail(int bookId) {
        bookService.getBookDetail(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    Intent i = new Intent(BookListActivity.this, BookDetailActivity.class);
                    i.putExtra("book", book);
                    startActivity(i);
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(BookListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}