package com.example.tallie.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.models.Error;
import com.example.tallie.models.Review;
import com.example.tallie.models.User;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    ItemClickListener itemClickListener;
    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);
    List<Review> reviews;

    public ReviewAdapter(List<Review> reviews, ItemClickListener itemClickListener) {
        super();
        this.reviews = reviews;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        userService.lookupUser(reviews.get(position).getUser_id()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    holder.txtReviewerUsername.setText(user.getUsername());
                } else if (response.errorBody() != null) {
                    Error error = new Gson().fromJson(response.errorBody().charStream(), Error.class);
                    Toast.makeText(holder.itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(holder.itemView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        holder.txtReviewRating.setText(String.valueOf(reviews.get(position).getStar()));
        holder.txtReviewContent.setText(reviews.get(position).getContent());
        holder.itemView.setOnClickListener(v -> itemClickListener.onClick(v, position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgReviewerAvatar;
        TextView txtReviewerUsername, txtReviewContent, txtReviewRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgReviewerAvatar = itemView.findViewById(R.id.imgReviewerAvatar);
            txtReviewerUsername = itemView.findViewById(R.id.txtReviewerUsername);
            txtReviewRating = itemView.findViewById(R.id.txtReviewRating);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
        }
    }

    public interface ItemClickListener {
        void onClick(View v, int position);
    }
}
