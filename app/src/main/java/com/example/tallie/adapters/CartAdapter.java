package com.example.tallie.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tallie.R;
import com.example.tallie.models.Book;
import com.example.tallie.models.Error;
import com.example.tallie.models.Order;
import com.example.tallie.services.BookService;
import com.example.tallie.services.OrderService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final BookService bookService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(BookService.class);
    private final OrderService orderService = RetrofitClient.getInstance("https://tallie-shipping.herokuapp.com/").create(OrderService.class);
    private final ArrayList<Order> orders;
    private final ItemClickListener itemClickListener;

    public CartAdapter(ArrayList<Order> orders, ItemClickListener itemClickListener) {
        this.orders = orders;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        bookService.getBookDetail(orders.get(position).getProductId()).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();

                    if (book.getPictures() == null) {
                        holder.imgBookPicture.setImageResource(R.drawable.ic_hmm);
                    } else {
                        Glide.with(holder.itemView.getContext())
                                .load("https://tallie-image.herokuapp.com/images/" + book.getPictures().get(0).getImg_id())
                                .fitCenter()
                                .into(holder.imgBookPicture);
                    }

                    holder.txtBookName.setText(book.getName());
                    holder.txtBookAuthor.setText(book.getAuthor());
                    holder.txtBookPrice.setText(String.valueOf(book.getPrice()));
                    holder.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position));
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(holder.itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Toast.makeText(holder.itemView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void deleteItem(Context context, int position, TextView txtCart) {
        orderService.deleteOrder(SharedPreferencesHandler.loadAppData(context), orders.get(position).get_id()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    orders.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();

                    String newText = "Your card: " + orders.size();
                    txtCart.setText(newText);
                } else if (response.errorBody() != null) {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Activity activity = (Activity) context;
                    activity.startActivity(activity.getIntent());
                    activity.finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBookPicture;
        TextView txtBookName, txtBookAuthor, txtBookPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBookPicture = itemView.findViewById(R.id.imgBookPicture);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtBookAuthor = itemView.findViewById(R.id.txtBookAuthor);
            txtBookPrice = itemView.findViewById(R.id.txtBookPrice);
        }
    }

    public interface ItemClickListener {
        void onClick(View v, int position);
    }
}
