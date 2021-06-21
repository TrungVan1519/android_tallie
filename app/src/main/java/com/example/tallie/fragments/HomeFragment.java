package com.example.tallie.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookData;

import java.util.List;

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView rcvBestSellingBooks, rcvFeaturedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvBestSellingBooks = view.findViewById(R.id.rcvBestSellingBooks);
        rcvFeaturedBooks = view.findViewById(R.id.rcvFeaturedBooks);

        // TODO: set up RecyclerView
        setupRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void setupRecyclerView() {
        List<Book> books = BookData.allBooks();
        BookAdapter bookAdapter = new BookAdapter(R.layout.layout_book_row, books);
        setupAdapter(bookAdapter);

        rcvBestSellingBooks.setAdapter(bookAdapter);
        rcvBestSellingBooks.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        rcvFeaturedBooks.setAdapter(bookAdapter);
        rcvFeaturedBooks.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
    }

    private void setupAdapter(BookAdapter adapter) {
        adapter.setItemClickListener((v, position) -> startActivity(new Intent(getActivity(), BookDetailActivity.class)));
    }
}