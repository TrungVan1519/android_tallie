package com.example.tallie.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.activities.BookDetailActivity;
import com.example.tallie.activities.LoginActivity;
import com.example.tallie.adapters.BookAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.PaymentCard;
import com.example.tallie.models.User;
import com.example.tallie.services.BookService;
import com.example.tallie.services.PaymentService;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);
    PaymentService paymentService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(PaymentService.class);

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    RoundedImageView imgUserAvatar;
    TextView txtUsername, txtEmail;
    FloatingActionButton btnLogout, btnSetting, btnUpdatePayment, btnFavorite, btnSeen;
    RecyclerView rcvMyCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgUserAvatar = view.findViewById(R.id.imgUserAvatar);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnUpdatePayment = view.findViewById(R.id.btnUpdatePayment);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnSeen = view.findViewById(R.id.btnSeen);
        rcvMyCollection = view.findViewById(R.id.rcvMyCollection);

        if (SharedPreferencesHandler.loadUserInfo(requireContext()).get(0).isEmpty()  // name
                || SharedPreferencesHandler.loadUserInfo(requireContext()).get(1).isEmpty()  // username
                || SharedPreferencesHandler.loadUserInfo(requireContext()).get(2).isEmpty()  // email
                || SharedPreferencesHandler.loadUserInfo(requireContext()).get(3).isEmpty()) // phone
        {
            userService.getUserProfile(SharedPreferencesHandler.loadAppData(requireContext())).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // TODO: set up TextView
                        User user = response.body();
                        txtUsername.setText(user.getUsername());
                        txtEmail.setText(user.getEmail());
                        Log.i("TAG", "onResponse: " + user);
                        SharedPreferencesHandler.saveUserInfo(requireContext(), user.getName(), user.getUsername(), user.getEmail(), user.getPhone());
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
        } else {
            List<String> userInfo = SharedPreferencesHandler.loadUserInfo(requireContext());
            txtUsername.setText(userInfo.get(1));
            txtEmail.setText(userInfo.get(2));
        }

        // TODO: set up RecyclerView
        setupRecyclerView(R.id.btnFavorite);

        // TODO: handle e
        btnLogout.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnUpdatePayment.setOnClickListener(this);
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
            case R.id.btnUpdatePayment:
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.layout_dialog_payment);
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                EditText txtPaymentCardNumber = dialog.findViewById(R.id.txtPaymentCardNumber);
                EditText txtPaymentName = dialog.findViewById(R.id.txtPaymentName);
                EditText txtPaymentStartDate = dialog.findViewById(R.id.txtPaymentStartDate);
                EditText txtPaymentEndDate = dialog.findViewById(R.id.txtPaymentEndDate);
                EditText txtPaymentCVC = dialog.findViewById(R.id.txtPaymentCVC);
                Button btnOK = dialog.findViewById(R.id.btnOK);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);

                // TODO: binding data
                List<String> payment = SharedPreferencesHandler.loadPayment(requireContext());
                txtPaymentCardNumber.setText(payment.get(0));
                txtPaymentName.setText(payment.get(1));
                txtPaymentStartDate.setText(payment.get(2));
                txtPaymentEndDate.setText(payment.get(3));
                txtPaymentCVC.setText(payment.get(4));

                txtPaymentStartDate.setFocusable(false);
                txtPaymentEndDate.setFocusable(false);

                // TODO: handle events
                txtPaymentStartDate.setOnClickListener(v1 -> getPaymentDate((EditText) v1));
                txtPaymentEndDate.setOnClickListener(v1 -> getPaymentDate((EditText) v1));
                btnCancel.setOnClickListener(v1 -> dialog.dismiss());
                btnOK.setOnClickListener(v1 -> {
                    Date startDate = null, endDate = null;
                    try {
                        startDate = sdf.parse(txtPaymentStartDate.getText().toString());
                        endDate = sdf.parse(txtPaymentEndDate.getText().toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    paymentService.registerPaymentCard(
                            SharedPreferencesHandler.loadAppData(requireContext()),
                            new PaymentCard(txtPaymentCardNumber.getText().toString(), txtPaymentName.getText().toString(), startDate, endDate, txtPaymentCVC.getText().toString()))
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(requireContext(), response.body(), Toast.LENGTH_SHORT).show();
                                        SharedPreferencesHandler.savePayment(
                                                requireContext(),
                                                txtPaymentCardNumber.getText().toString(),
                                                txtPaymentName.getText().toString(),
                                                txtPaymentStartDate.getText().toString(),
                                                txtPaymentEndDate.getText().toString(),
                                                txtPaymentCVC.getText().toString());
                                        dialog.dismiss();
                                    } else {
                                        try {
                                            assert response.errorBody() != null;
                                            Toast.makeText(requireContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                            Log.e("TAG", "onResponse: " + response.code() + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "onFailure: " + t.getMessage());
                                }
                            });
                });
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

    private void getPaymentDate(EditText txt) {
        DatePickerDialog.OnDateSetListener e = (view, year, month, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            txt.setText(sdf.format(cal.getTime()));
        };

        DatePickerDialog date = new DatePickerDialog(
                requireContext(),
                e,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        date.show();
    }

    private void setupRecyclerView(int id) {
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
                    rcvMyCollection.setLayoutManager(new LinearLayoutManager(requireContext()));
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
                    rcvMyCollection.setLayoutManager(new LinearLayoutManager(requireContext()));
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