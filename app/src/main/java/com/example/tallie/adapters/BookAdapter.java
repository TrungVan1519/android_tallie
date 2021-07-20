package com.example.tallie.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tallie.R;
import com.example.tallie.models.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;
    private final ArrayList<Book> books;

    public BookAdapter(ArrayList<Book> books, ItemClickListener itemClickListener) {
        this.books = books;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (books.get(position).getPictures() == null) {
            holder.imgBookPicture.setImageResource(R.drawable.ic_hmm);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load("https://tallie-image.herokuapp.com/images/" + books.get(position).getPictures().get(0).getImg_id())
                    .fitCenter()
                    .into(holder.imgBookPicture);
        }
        holder.txtBookName.setText(books.get(position).getName());
        holder.txtBookAuthor.setText(books.get(position).getAuthor());
        holder.txtBookPrice.setText(String.valueOf(books.get(position).getPrice()));
        holder.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position));
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
