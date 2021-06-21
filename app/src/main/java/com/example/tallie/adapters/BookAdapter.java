package com.example.tallie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onClick(View v, int position);
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

    private ItemClickListener itemClickListener;
    private final int resource;
    private final List<Book> books;

    public BookAdapter(int resource, List<Book> books) {
        this.resource = resource;
        this.books = books;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgBookPicture.setImageResource(books.get(position).getPicture());
        holder.txtBookName.setText(books.get(position).getName());
        holder.txtBookAuthor.setText(books.get(position).getAuthor());
        holder.txtBookPrice.setText(String.valueOf(books.get(position).getPrice()));
        holder.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
