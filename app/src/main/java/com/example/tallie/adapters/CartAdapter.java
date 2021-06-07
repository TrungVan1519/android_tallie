package com.example.tallie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;
import com.example.tallie.models.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    interface ItemClickListener {
        void onClick(View v, int position, boolean isLongClick);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ItemClickListener itemClickListener;
        LinearLayout vCartRow;
        TextView txtCartTime, txtCartQty;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            vCartRow = itemView.findViewById(R.id.vCartRow);
            txtCartTime = itemView.findViewById(R.id.txtCartTime);
            txtCartQty = itemView.findViewById(R.id.txtCartQty);

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

    private final Context context;
    private final int resource;
    private final List<Cart> carts;

    public CartAdapter(Context context, int resource, List<Cart> carts) {
        this.context = context;
        this.resource = resource;
        this.carts = carts;
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
        holder.vCartRow.setBackgroundColor(stripes);
        holder.txtCartTime.setText(carts.get(position).getTime());
        holder.txtCartQty.setText(carts.get(position).getQty());

        // TODO: handle item events
        holder.itemClickListener = (v, position1, isLongClick) -> holder.vCartRow.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
