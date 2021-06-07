package com.example.tallie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.BookDetailActivity;
import com.example.tallie.R;
import com.example.tallie.models.Book;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    interface ItemClickListener {
        void onClick(View v, int position, boolean isLongClick);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ItemClickListener itemClickListener;
        LinearLayoutCompat vBookRow;
        ImageView imgBookPicture;
        TextView txtBookName, txtBookAuthorName, txtBookPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vBookRow = itemView.findViewById(R.id.vBookRow);
            imgBookPicture = itemView.findViewById(R.id.imgBookPicture);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtBookAuthorName = itemView.findViewById(R.id.txtBookAuthorName);
            txtBookPrice = itemView.findViewById(R.id.txtBookPrice);

            // TODO: add events to item
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    private final Context context; // Activity or Context
    private final int resource;
    private final List<Book> books;

    public BookAdapter(Context context, int resource, List<Book> books) {
        this.context = context;
        this.resource = resource;
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int stripes = position % 2 == 0
                ? ContextCompat.getColor(context, android.R.color.darker_gray)
                : ContextCompat.getColor(context, android.R.color.white);
        holder.vBookRow.setBackgroundColor(stripes);
        holder.imgBookPicture.setImageResource(books.get(position).getPicture());
        holder.txtBookName.setText(books.get(position).getName());
        holder.txtBookAuthorName.setText(books.get(position).getAuthorName());
        holder.txtBookPrice.setText(String.valueOf(books.get(position).getPrice()));

        // TODO: handle item events
        holder.itemClickListener = (v, position1, isLongClick) -> {
            holder.vBookRow.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_bottom_sheet, bottomSheetDialog.findViewById(R.id.vBottomSheet), false);

            RoundedImageView imgBookPictureDetail = bottomSheetView.findViewById(R.id.imgBookPictureDetail);
            TextView txtBookNameDetail = bottomSheetView.findViewById(R.id.txtBookNameDetail);
            TextView txtBookAuthorNameDetail = bottomSheetView.findViewById(R.id.txtBookAuthorNameDetail);
            TextView txtBookPriceDetail = bottomSheetView.findViewById(R.id.txtBookPriceDetail);
            FloatingActionButton btnFavoriteDetail = bottomSheetView.findViewById(R.id.btnFavoriteDetail);
            FloatingActionButton btnInfoDetail = bottomSheetView.findViewById(R.id.btnInfoDetail);

            imgBookPictureDetail.setImageResource(books.get(position).getPicture());
            txtBookNameDetail.setText(books.get(position).getName());
            txtBookAuthorNameDetail.setText(books.get(position).getAuthorName());
            txtBookPriceDetail.setText(String.valueOf(books.get(position).getPrice()));

            btnFavoriteDetail.setOnClickListener(view -> {
                Toast.makeText(context, "Add to favorite done", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            btnInfoDetail.setOnClickListener(view -> {
                context.startActivity(new Intent(context, BookDetailActivity.class));
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        };
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
