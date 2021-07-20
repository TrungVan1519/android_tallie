package com.example.tallie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tallie.R;
import com.example.tallie.models.Book;

import java.util.List;

public class BookListViewAdapter<T> extends ArrayAdapter<T> {

    private final Context mContext;
    private final int mLayout;
    private final List<T> mData;

    public BookListViewAdapter(@NonNull Context mContext, int mLayout, @NonNull List<T> mData) {
        super(mContext, mLayout, mData);

        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mData = mData;
    }

    private View getCustomView(int position, @NonNull ViewGroup parent) {
        View convertView = LayoutInflater.from(mContext).inflate(mLayout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.imgBookPicture = convertView.findViewById(R.id.imgBookPicture);
        holder.txtBookName = convertView.findViewById(R.id.txtBookName);
        holder.txtBookAuthor = convertView.findViewById(R.id.txtBookAuthor);
        holder.txtBookPrice = convertView.findViewById(R.id.txtBookPrice);

        Book book = (Book) mData.get(position);
        if (book.getPictures() == null) {
            holder.imgBookPicture.setImageResource(R.drawable.ic_hmm);
        } else {
            Glide.with(parent.getContext())
                    .load("https://tallie-image.herokuapp.com/images/" + book.getPictures().get(0).getImg_id())
                    .fitCenter()
                    .into(holder.imgBookPicture);
        }
        holder.txtBookName.setText(book.getName());
        holder.txtBookAuthor.setText(book.getAuthor());
        holder.txtBookPrice.setText(String.valueOf(book.getPrice()));

        convertView.setTag(holder);

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    static class ViewHolder {
        ImageView imgBookPicture;
        TextView txtBookName, txtBookAuthor, txtBookPrice;
    }
}

