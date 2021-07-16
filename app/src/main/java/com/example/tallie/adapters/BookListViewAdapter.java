package com.example.tallie.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tallie.R;
import com.example.tallie.models.Book;
import com.example.tallie.models.Error;
import com.example.tallie.services.ImageService;
import com.example.tallie.utils.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListViewAdapter<T> extends ArrayAdapter<T> {

    private final ImageService imageService = RetrofitClient.getInstance("https://tallie-image.herokuapp.com/").create(ImageService.class);
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
            imageService.downloadImage(book.getPictures().get(0).getImg_id()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                        holder.imgBookPicture.setImageBitmap(image);
                    } else if (response.errorBody() != null) {
                        Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                        Toast.makeText(parent.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(parent.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
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

