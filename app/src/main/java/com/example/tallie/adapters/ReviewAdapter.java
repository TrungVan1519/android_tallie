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
import com.example.tallie.models.Review;
import com.example.tallie.models.User;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);
    List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        super();
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        userService.lookupUser(reviews.get(position).getUser_id()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    holder.txtReviewerUsername.setText(user.getUsername());
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(holder.itemView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        holder.txtReviewRating.setText(String.valueOf(reviews.get(position).getStar()));
        holder.txtReviewContent.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgReviewerAvatar;
        TextView txtReviewerUsername,txtReviewContent, txtReviewRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgReviewerAvatar = itemView.findViewById(R.id.imgReviewerAvatar);
            txtReviewerUsername = itemView.findViewById(R.id.txtReviewerUsername);
            txtReviewRating = itemView.findViewById(R.id.txtReviewRating);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
        }
    }
}
