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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.activities.LoginActivity;
import com.example.tallie.activities.MainActivity;
import com.example.tallie.activities.SignUpActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookData;
import com.example.tallie.models.User;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build();
    Gson gson = new GsonBuilder().setLenient().create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://tallie.herokuapp.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    List<Book> orderBooks, favBooks, seenBooks;
    BookAdapter orderAdapter, favAdapter, seenAdapter;

    RoundedImageView imgUserAvatar;
    TextView txtUsername;
    Button btnLogout, btnSetting;
    FloatingActionButton btnPayCart, btnClearCart, btnFavorite, btnSeen;
    RecyclerView rcvMyOrders, rcvMyCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgUserAvatar = view.findViewById(R.id.imgUserAvatar);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnPayCart = view.findViewById(R.id.btnPayCart);
        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnSeen = view.findViewById(R.id.btnSeen);
        rcvMyOrders = view.findViewById(R.id.rcvMyOrders);
        rcvMyCollection = view.findViewById(R.id.rcvMyCollection);

        UserService userService = retrofit.create(UserService.class);
        Call<User> userCallback = userService.getUserProfile(SharedPreferencesHandler.loadAppData(getContext()));
        userCallback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // TODO: set up TextView
                    txtUsername.setText(response.body().getName());
                } else {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
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
                SharedPreferencesHandler.saveAppData(getActivity(), "");

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();
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
        orderAdapter = new BookAdapter(R.layout.layout_book_row, orderBooks);
        setupAdapter(orderAdapter);
        rcvMyOrders.setAdapter(orderAdapter);
        rcvMyOrders.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        if (id == R.id.btnFavorite) {
            favBooks = BookData.favBooks();
            favAdapter = new BookAdapter(R.layout.layout_book_row, favBooks);
            setupAdapter(favAdapter);
            rcvMyCollection.setAdapter(favAdapter);
        } else {
            seenBooks = BookData.seenBooks();
            seenAdapter = new BookAdapter(R.layout.layout_book_row, seenBooks);
            setupAdapter(seenAdapter);
            rcvMyCollection.setAdapter(seenAdapter);
        }
        rcvMyCollection.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
    }

    private void setupAdapter(BookAdapter adapter) {
        adapter.setItemClickListener((v, position) -> startActivity(new Intent(getActivity(), BookDetailActivity.class)));
    }

    private void changeBackgroundTint(FloatingActionButton selected) {
        for (FloatingActionButton btn : Arrays.asList(btnFavorite, btnSeen)) {
            btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
        selected.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
    }
}