package com.example.tallie.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.activities.LoginActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.User;
import com.example.tallie.services.BookService;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ArrayList<Book> orderBooks;
    BookAdapter orderAdapter;

    RoundedImageView imgUserAvatar;
    TextView txtUsername, txtEmail;
    Button btnLogout, btnSetting;
    FloatingActionButton btnPayCart, btnClearCart, btnFavorite, btnSeen;
    RecyclerView rcvMyOrders, rcvMyCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgUserAvatar = view.findViewById(R.id.imgUserAvatar);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnPayCart = view.findViewById(R.id.btnPayCart);
        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnSeen = view.findViewById(R.id.btnSeen);
        rcvMyOrders = view.findViewById(R.id.rcvMyOrders);
        rcvMyCollection = view.findViewById(R.id.rcvMyCollection);

        userService.getUserProfile(SharedPreferencesHandler.loadAppData(requireContext())).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // TODO: set up TextView
                    User user = response.body();
                    txtUsername.setText(user.getUsername());
                    txtEmail.setText(user.getEmail());
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        // TODO: set up RecyclerView
        setupRecyclerView(R.id.btnFavorite);

        // TODO: handle e
        btnLogout.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnPayCart.setOnClickListener(this);
        btnClearCart.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnSeen.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                SharedPreferencesHandler.saveAppData(requireActivity(), "");

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                requireActivity().finish();
                break;
            case R.id.btnSetting:
                Toast.makeText(getActivity(), "User setting is not available", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPayCart:
                Toast.makeText(getActivity(), "Pay cart done", Toast.LENGTH_SHORT).show();
                orderBooks.clear();
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.btnClearCart:
                orderBooks.clear();
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.btnFavorite:
            case R.id.btnSeen:
                setupRecyclerView(v.getId());
                changeBackgroundTint((FloatingActionButton) v);
                break;
            default:
                break;
        }
    }

    private void setupRecyclerView(int id) {
        orderBooks = new ArrayList<>();
        orderAdapter = new BookAdapter(orderBooks, (v, position) -> startActivity(new Intent(getActivity(), BookDetailActivity.class)));
        rcvMyOrders.setAdapter(orderAdapter);
        rcvMyOrders.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        if (id == R.id.btnFavorite) {
            getWishList();
        } else {
            getSeenList();
        }
    }

    private void changeBackgroundTint(FloatingActionButton selected) {
        for (FloatingActionButton btn : Arrays.asList(btnFavorite, btnSeen)) {
            btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
        selected.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
    }

    private void getWishList() {
        bookService.getWishList(SharedPreferencesHandler.loadAppData(requireContext())).enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();
                    rcvMyCollection.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
                    rcvMyCollection.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(requireContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getSeenList() {
        bookService.getSeenList(SharedPreferencesHandler.loadAppData(requireContext())).enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();
                    rcvMyCollection.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
                    rcvMyCollection.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(requireContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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