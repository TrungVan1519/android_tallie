package com.example.tallie.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.services.BookService;
import com.example.tallie.utils.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView rcvMostViewed, rcvFeaturedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvMostViewed = view.findViewById(R.id.rcvMostViewed);
        rcvFeaturedBooks = view.findViewById(R.id.rcvFeaturedBooks);

        // TODO: set up RecyclerView
        setupRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void setupRecyclerView() {
        bookService.allMostViewed().enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getData() == null ? new ArrayList<>() : bookList.getData();
                    BookAdapter adapter = new BookAdapter(books, (v, position) -> startActivity(new Intent(getActivity(), BookDetailActivity.class)));
                    rcvMostViewed.setAdapter(adapter);
                    rcvMostViewed.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        bookService.allFeaturedBooks().enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    Log.e("TAG", "TEST: " + response.body());
                    ArrayList<Book> books = bookList.getData() == null ? new ArrayList<>() : bookList.getData();
                    BookAdapter adapter = new BookAdapter(books, (v, position) -> startActivity(new Intent(getActivity(), BookDetailActivity.class)));
                    rcvFeaturedBooks.setAdapter(adapter);
                    rcvFeaturedBooks.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}