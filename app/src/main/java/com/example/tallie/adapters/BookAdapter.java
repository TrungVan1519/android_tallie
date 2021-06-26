package com.example.tallie.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.models.Book;
import com.example.tallie.services.ImageService;
import com.example.tallie.utils.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final ImageService imageService = RetrofitClient.getInstance("https://tallie-image.herokuapp.com/").create(ImageService.class);
    private final ItemClickListener itemClickListener;
    private final ArrayList<Book> books;

    public BookAdapter(ArrayList<Book> books, ItemClickListener itemClickListener) {
        this.books = books;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (books.get(position).getPictures() == null) {
            holder.imgBookPicture.setImageResource(R.drawable.ic_hmm);
        } else {
            imageService.downloadImage(books.get(position).getPictures().get(0).getImg_id()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                        holder.imgBookPicture.setImageBitmap(image);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Toast.makeText(holder.itemView.getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onResponse: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });

            holder.txtBookName.setText(books.get(position).getName());
            holder.txtBookAuthor.setText(books.get(position).getAuthor());
            holder.txtBookPrice.setText(String.valueOf(books.get(position).getPrice()));
            holder.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
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
