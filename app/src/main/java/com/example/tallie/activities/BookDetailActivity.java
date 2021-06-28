package com.example.tallie.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.adapters.ReviewAdapter;
import com.example.tallie.models.Book;
import com.example.tallie.models.Review;
import com.example.tallie.models.ReviewList;
import com.example.tallie.models.User;
import com.example.tallie.services.BookService;
import com.example.tallie.services.ImageService;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);
    BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    ImageService imageService = RetrofitClient.getInstance("https://tallie-image.herokuapp.com/").create(ImageService.class);

    RoundedImageView imgBookPicture;
    Button btnAddToCart, btnAddToWishList;
    TextView txtBookName, txtBookAuthor, txtBookPrice, txtBookDescription, txtSellerUsername, txtSellerAddress;
    RecyclerView rcvReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        imgBookPicture = findViewById(R.id.imgBookPicture);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToWishList = findViewById(R.id.btnAddToWishList);
        txtBookName = findViewById(R.id.txtBookName);
        txtBookAuthor = findViewById(R.id.txtBookAuthor);
        txtBookPrice = findViewById(R.id.txtBookPrice);
        txtBookDescription = findViewById(R.id.txtBookDescription);
        txtSellerUsername = findViewById(R.id.txtSellerUsername);
        txtSellerAddress = findViewById(R.id.txtSellerAddress);
        rcvReviews = findViewById(R.id.rcvReviews);

        Book book = getIntentData();

        if (book == null) return;

        addToSeenList(book);
        populateBookData(book);
        getSellerData(book);
        getAllReviews(book);

        // TODO: 2021-06-27 handle events
        btnAddToCart.setOnClickListener(v -> {

        });

        btnAddToWishList.setOnClickListener(v -> bookService.addToWishList(SharedPreferencesHandler.loadAppData(this), new Book(book.getId())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(BookDetailActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(BookDetailActivity.this, "Book has been existed in your wishlist", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: Book has been existed in your wishlist \t" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        }));
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
                } else {
                    try {
                        assert response.errorBody() != null;
                        Log.e("TAG", "onResponse: Product has been existed \t" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(BookDetailActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(BookDetailActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    rcvReviews.setAdapter(new ReviewAdapter(reviews));
                    rcvReviews.setLayoutManager(new LinearLayoutManager(BookDetailActivity.this));
                } else {
                    try {
                        assert response.errorBody() != null;
                        Toast.makeText(BookDetailActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                Toast.makeText(BookDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}