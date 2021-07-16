package com.example.tallie.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.adapters.ReviewAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.BookList;
import com.example.tallie.models.Error;
import com.example.tallie.models.Order;
import com.example.tallie.models.Review;
import com.example.tallie.models.ReviewList;
import com.example.tallie.models.User;
import com.example.tallie.services.BookService;
import com.example.tallie.services.ImageService;
import com.example.tallie.services.PaymentService;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.Constants;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);
    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    ImageService imageService = RetrofitClient.getInstance("https://tallie-image.herokuapp.com/").create(ImageService.class);
    PaymentService paymentService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(PaymentService.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    RoundedImageView imgBookPicture;
    FloatingActionButton btnAddToCart, btnWishList;
    TextView txtBookName, txtBookAuthor, txtBookPrice, txtBookDescription, txtSellerUsername, txtSellerAddress;
    Button btnAddNewReview;
    RecyclerView rcvReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        imgBookPicture = findViewById(R.id.imgBookPicture);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnWishList = findViewById(R.id.btnWishList);
        txtBookName = findViewById(R.id.txtBookName);
        txtBookAuthor = findViewById(R.id.txtBookAuthor);
        txtBookPrice = findViewById(R.id.txtBookPrice);
        txtBookDescription = findViewById(R.id.txtBookDescription);
        txtSellerUsername = findViewById(R.id.txtSellerUsername);
        txtSellerAddress = findViewById(R.id.txtSellerAddress);
        btnAddNewReview = findViewById(R.id.btnAddNewReview);
        rcvReviews = findViewById(R.id.rcvReviews);

        if (getIntentData() == null) return;

        Book book = getIntentData();
        new CheckIfExistInWishListTask().execute(book);
        addToSeenList(book);
        populateBookData(book);
        getSellerData(book);
        getAllReviews(book);

        // TODO: handle events
        btnAddToCart.setOnClickListener(v -> addToCart(book));
        btnWishList.setOnClickListener(v -> addOrDeleteFromWishList(book));
        btnAddNewReview.setOnClickListener(v -> addReview(book));
    }

    private Book getIntentData() {
        return (Book) getIntent().getSerializableExtra("book");
    }

    private void addToSeenList(Book book) {
        bookService.addToSeenList(SharedPreferencesHandler.loadAppData(this), new Book(book.getId())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("TAG", "onResponse: " + response.body());
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Log.e("TAG", "onResponse: " + error.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void populateBookData(Book book) {
        imageService.downloadImage(book.getPictures().get(0).getImg_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                    imgBookPicture.setImageBitmap(image);
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        txtBookName.setText(book.getName());
        txtBookAuthor.setText(book.getAuthor());
        txtBookPrice.setText(String.valueOf(book.getPrice()));
        txtBookDescription.setText(book.getDescription());
    }

    private void getSellerData(Book book) {
        userService.lookupUser(book.getSeller_id()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User seller = response.body();
                    txtSellerUsername.setText(seller.getUsername());
                    txtSellerAddress.setText(seller.getAddress());
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getAllReviews(Book book) {
        bookService.allReviews(book.getId()).enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(@NonNull Call<ReviewList> call, @NonNull Response<ReviewList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewList reviewList = response.body();
                    List<Review> reviews = reviewList.getReviews() == null ? new ArrayList<>() : reviewList.getReviews();
                    rcvReviews.setAdapter(new ReviewAdapter(reviews, (v, position) -> {
                        PopupMenu popupMenu = new PopupMenu(BookDetailActivity.this, v);
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == R.id.menuEditReview) {
                                updateReview(book);
                            } else {
                                deleteReview(book);
                            }
                            return false;
                        });
                        popupMenu.show();
                    }));
                    rcvReviews.setLayoutManager(new LinearLayoutManager(BookDetailActivity.this));
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void addToCart(Book book) {
        bookService.orderBook(
                SharedPreferencesHandler.loadAppData(BookDetailActivity.this),
                new Order(book.getId(), 1,
                        SharedPreferencesHandler.loadPayment(BookDetailActivity.this).get(Constants.PAYMENT_CARD_NUMBER),
                        SharedPreferencesHandler.loadUserInfo(BookDetailActivity.this).get(Constants.NAME),
                        SharedPreferencesHandler.loadUserInfo(BookDetailActivity.this).get(Constants.PHONE),
                        "default address"))
                .enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(BookDetailActivity.this, "Order book successfully", Toast.LENGTH_SHORT).show();
                        } else if (response.errorBody() != null) {
                            Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                            Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                        Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
    }

    private void addOrDeleteFromWishList(Book book) {
        if (btnWishList.getBackgroundTintList() == ColorStateList.valueOf(getResources().getColor(R.color.white))) {
            bookService.addToWishList(SharedPreferencesHandler.loadAppData(this), new Book(book.getId())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(BookDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        btnWishList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                    } else if (response.errorBody() != null) {
                        Toast.makeText(BookDetailActivity.this, "Add to wish list fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
        } else {
            bookService.deleteFromWishList(SharedPreferencesHandler.loadAppData(this), new Book(book.getId())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(BookDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        btnWishList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    } else if (response.errorBody() != null) {
                        Toast.makeText(BookDetailActivity.this, "Delete from wish list fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void addReview(Book book) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_review);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        TextView txtOverview = dialog.findViewById(R.id.txtOverview);
        TextView txtContent = dialog.findViewById(R.id.txtContent);
        Spinner spnStar = dialog.findViewById(R.id.spnStar);
        CheckBox ckbPreventSpoiler = dialog.findViewById(R.id.ckbPreventSpoiler);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        spnStar.setAdapter(new ArrayAdapter<>(BookDetailActivity.this, android.R.layout.simple_list_item_1, Arrays.asList(1, 2, 3, 4, 5)));
        btnSubmit.setOnClickListener(v1 -> bookService.addReview(
                SharedPreferencesHandler.loadAppData(this), book.getId(),
                new Review(Integer.parseInt(spnStar.getSelectedItem().toString()), txtOverview.getText().toString(), txtContent.getText().toString(), ckbPreventSpoiler.isChecked()))
                .enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BookDetailActivity.this, "Add review successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getAllReviews(book);
                        } else if (response.errorBody() != null) {
                            Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                            Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                        Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                }));
        btnCancel.setOnClickListener(v1 -> dialog.dismiss());
    }

    private void updateReview(Book book) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_review);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        TextView txtOverview = dialog.findViewById(R.id.txtOverview);
        TextView txtContent = dialog.findViewById(R.id.txtContent);
        Spinner spnStar = dialog.findViewById(R.id.spnStar);
        CheckBox ckbPreventSpoiler = dialog.findViewById(R.id.ckbPreventSpoiler);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        spnStar.setAdapter(new ArrayAdapter<>(BookDetailActivity.this, android.R.layout.simple_list_item_1, Arrays.asList(1, 2, 3, 4, 5)));
        btnSubmit.setOnClickListener(v1 -> bookService.updateReview(
                SharedPreferencesHandler.loadAppData(this), book.getId(),
                new Review(Integer.parseInt(spnStar.getSelectedItem().toString()), txtOverview.getText().toString(), txtContent.getText().toString(), ckbPreventSpoiler.isChecked()))
                .enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(BookDetailActivity.this, "Update review successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getAllReviews(book);
                        } else if (response.errorBody() != null) {
                            Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                            Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                        Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                }));
        btnCancel.setOnClickListener(v1 -> dialog.dismiss());
    }

    private void deleteReview(Book book) {
        bookService.deleteReview(SharedPreferencesHandler.loadAppData(this), book.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookDetailActivity.this, "Delete review successfully", Toast.LENGTH_SHORT).show();
                    getAllReviews(book);
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class CheckIfExistInWishListTask extends AsyncTask<Book, Void, Boolean> {

        CheckIfExistInWishListTask() {
            super();
        }

        @Override
        protected Boolean doInBackground(Book... params) {
            boolean isExist = false;
            try {
                Response<BookList> response = bookService.getWishList(SharedPreferencesHandler.loadAppData(BookDetailActivity.this)).execute();
                if (response.isSuccessful() && response.body() != null) {
                    BookList bookList = response.body();
                    ArrayList<Book> books = bookList.getProducts() == null ? new ArrayList<>() : bookList.getProducts();

                    if (books.size() > 0) {
                        for (Book b : books) {
                            if (b.getId() == params[0].getId()) {
                                isExist = true;
                                break;
                            }
                        }
                    }
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(BookDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return isExist;
        }

        @Override
        protected void onPostExecute(Boolean isExist) {
            super.onPostExecute(isExist);

            if (isExist) {
                btnWishList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            } else {
                btnWishList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        }
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
                Toast.makeText(BookDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        }

        return super.onOptionsItemSelected(item);
    }
}