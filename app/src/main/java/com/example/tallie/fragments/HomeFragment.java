package com.example.tallie.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.activities.BookListActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.adapters.CategoryAdapter;
import com.example.tallie.adapters.BookListViewAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.Category;
import com.example.tallie.models.CategoryList;
import com.example.tallie.services.BookService;
import com.example.tallie.utils.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView rcvCategories, rcvMostViewed, rcvFeaturedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvCategories = view.findViewById(R.id.rcvCategories);
        rcvMostViewed = view.findViewById(R.id.rcvMostViewed);
        rcvFeaturedBooks = view.findViewById(R.id.rcvFeaturedBooks);

        // TODO: set up RecyclerView
        setupRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void setupRecyclerView() {
        bookService.allCategories().enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(@NonNull Call<CategoryList> call, @NonNull Response<CategoryList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CategoryList categoryList = response.body();
                    List<Category> categories = categoryList.getCategories() == null ? new ArrayList<>() : categoryList.getCategories();
                    rcvCategories.setAdapter(new CategoryAdapter(categories, (v, position) -> bookService.getBooksByCategory(categories.get(position).getId()).enqueue(new Callback<BookList>() {
                        @Override
                        public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                BookList bookList = response.body();
                                ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();

                                if (books.size() == 0) {
                                    Toast.makeText(getContext(), "This category does not have any books.", Toast.LENGTH_SHORT).show();
                                } else {
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.layout_dialog);
                                    lp.copyFrom(dialog.getWindow().getAttributes());
                                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    dialog.show();
                                    dialog.getWindow().setAttributes(lp);

                                    // TODO: handle events
                                    dialog.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());
                                    dialog.findViewById(R.id.btnFullScreen).setOnClickListener(v -> {
                                        Intent i = new Intent(getContext(), BookListActivity.class);
                                        i.putExtra("bookList", books);
                                        requireContext().startActivity(i);
                                    });

                                    ListView lsvBooks = dialog.findViewById(R.id.lsvBooks);
                                    lsvBooks.setAdapter(new BookListViewAdapter<>(requireContext(), R.layout.layout_book_row, books));
                                    lsvBooks.setOnItemClickListener((parent, view, position1, id) -> getBookDetail(books.get(position1).getId()));
                                }
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
                    })));
                    rcvCategories.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
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
            public void onFailure(@NonNull Call<CategoryList> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        bookService.allMostViewed().enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getData() == null ? new ArrayList<>() : bookList.getData();
                    rcvMostViewed.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
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
                    ArrayList<Book> books = bookList.getData() == null ? new ArrayList<>() : bookList.getData();
                    rcvFeaturedBooks.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
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

    private void getBookDetail(int bookId) {
        bookService.getBookDetail(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    Intent i = new Intent(getActivity(), BookDetailActivity.class);
                    i.putExtra("book", book);
                    startActivity(i);
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
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}