package com.example.tallie.fragments;

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
import com.example.tallie.models.Error;
import com.example.tallie.models.PaymentCard;
import com.example.tallie.models.User;
import com.example.tallie.services.BookService;
import com.example.tallie.services.PaymentService;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.Constants;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        Map<String, String> userInfo = SharedPreferencesHandler.loadUserInfo(getContext());
        if (Objects.requireNonNull(userInfo.get(Constants.NAME)).isEmpty()
                || Objects.requireNonNull(userInfo.get(Constants.USERNAME)).isEmpty()
                || Objects.requireNonNull(userInfo.get(Constants.EMAIL)).isEmpty()
                || Objects.requireNonNull(userInfo.get(Constants.PHONE)).isEmpty()) {
            userService.getUserProfile(SharedPreferencesHandler.loadAppData(getContext())).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();

                        // TODO: set up TextView
                        txtUsername.setText(user.getUsername());
                        txtEmail.setText(user.getEmail());

                        SharedPreferencesHandler.saveUserInfo(getContext(), user.getName(), user.getUsername(), user.getEmail(), user.getPhone());
                    } else if (response.errorBody() != null) {
                        Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
        } else {
            txtUsername.setText(userInfo.get(Constants.USERNAME));
            txtEmail.setText(userInfo.get(Constants.EMAIL));
        }

        // TODO: set up RecyclerView
        setupRecyclerView(R.id.btnFavorite);

        // TODO: handle e
        btnLogout.setOnClickListener(v -> deregisterPaymentCard());
        btnSetting.setOnClickListener(v -> updateUserProfile());
        btnUpdatePayment.setOnClickListener(v -> registerPaymentCard());
        btnFavorite.setOnClickListener(v -> {
            setupRecyclerView(v.getId());
            changeBackgroundTint((FloatingActionButton) v);
        });
        btnSeen.setOnClickListener(v -> {
            setupRecyclerView(v.getId());
            changeBackgroundTint((FloatingActionButton) v);
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void deregisterPaymentCard() {
        paymentService.deregisterPaymentCard(SharedPreferencesHandler.loadAppData(getContext())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        SharedPreferencesHandler.clearData(getContext());
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        requireActivity().finish();
    }

    private void updateUserProfile() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_user_profile);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        EditText txtName = dialog.findViewById(R.id.txtName);
        EditText txtEmail = dialog.findViewById(R.id.txtEmail);
        EditText txtPassword = dialog.findViewById(R.id.txtPassword);
        EditText txtPhone = dialog.findViewById(R.id.txtPhone);
        EditText txtAddress = dialog.findViewById(R.id.txtAddress);
        EditText txtBio = dialog.findViewById(R.id.txtBio);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // TODO: binding data
        Map<String, String> userInfo = SharedPreferencesHandler.loadUserInfo(getContext());
        txtName.setText(userInfo.get(Constants.NAME));
        txtEmail.setText(userInfo.get(Constants.EMAIL));
        txtPhone.setText(userInfo.get(Constants.PHONE));

        // TODO: handle events
        btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        btnOK.setOnClickListener(v1 -> userService.updateUserProfile(
                SharedPreferencesHandler.loadAppData(getContext()),
                new User(
                        txtName.getText().toString(),
                        txtPassword.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPhone.getText().toString(),
                        txtAddress.getText().toString(),
                        txtBio.getText().toString()))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Update user profile successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else if (response.errorBody() != null) {
                            Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                }));
    }

    private void registerPaymentCard() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_payment);
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
        Map<String, String> payment = SharedPreferencesHandler.loadPayment(getContext());
        txtPaymentCardNumber.setText(payment.get(Constants.PAYMENT_CARD_NUMBER));
        txtPaymentName.setText(payment.get(Constants.PAYMENT_NAME));
        txtPaymentStartDate.setText(payment.get(Constants.PAYMENT_START_DATE));
        txtPaymentEndDate.setText(payment.get(Constants.PAYMENT_END_DATE));
        txtPaymentCVC.setText(payment.get(Constants.PAYMENT_CVC));

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
                    SharedPreferencesHandler.loadAppData(getContext()),
                    new PaymentCard(txtPaymentCardNumber.getText().toString(), txtPaymentName.getText().toString(), startDate, endDate, txtPaymentCVC.getText().toString()))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                                SharedPreferencesHandler.savePayment(
                                        getContext(),
                                        txtPaymentCardNumber.getText().toString(),
                                        txtPaymentName.getText().toString(),
                                        txtPaymentStartDate.getText().toString(),
                                        txtPaymentEndDate.getText().toString(),
                                        txtPaymentCVC.getText().toString());
                                dialog.dismiss();
                            } else if (response.errorBody() != null) {
                                Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Log.e("TAG", "onFailure: " + t.getMessage());
                        }
                    });
        });
    }

    private void getPaymentDate(EditText txt) {
        DatePickerDialog.OnDateSetListener e = (view, year, month, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            txt.setText(sdf.format(cal.getTime()));
        };

        DatePickerDialog date = new DatePickerDialog(
                getContext(),
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

    private void getWishList() {
        bookService.getWishList(SharedPreferencesHandler.loadAppData(getContext())).enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();
                    rcvMyCollection.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
                    rcvMyCollection.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getSeenList() {
        bookService.getSeenList(SharedPreferencesHandler.loadAppData(getContext())).enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(@NonNull Call<BookList> call, @NonNull Response<BookList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();
                    rcvMyCollection.setAdapter(new BookAdapter(books, (v, position) -> getBookDetail(books.get(position).getId())));
                    rcvMyCollection.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookList> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void changeBackgroundTint(FloatingActionButton selected) {
        for (FloatingActionButton btn : Arrays.asList(btnFavorite, btnSeen)) {
            btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
        selected.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
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
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}