package com.example.tallie.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tallie.R;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final Drawable icDelete;
    private final Paint clearPaint = new Paint();

    public SwipeToDeleteCallback(Context context) {
        super(0, ItemTouchHelper.LEFT);

        icDelete = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        /*
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (viewHolder.getAdapterPosition() == 10) return 0;
        return super.getMovementFlags(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;

        if (dX == 0f && !isCurrentlyActive) {
            clearCanvas(c, itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            return;
        }

        // TODO: draw the red background
        ColorDrawable background = new ColorDrawable();
        background.setColor(Color.RED);
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        // TODO: calculate position of delete icon and draw the delete icon
        int intrinsicWidth = icDelete.getIntrinsicWidth();
        int intrinsicHeight = icDelete.getIntrinsicHeight();
        int height = itemView.getBottom() - itemView.getTop();
        int margin = (height - intrinsicHeight) / 2;

        int top = itemView.getTop() + (height - intrinsicHeight) / 2;
        int bottom = top + intrinsicHeight;
        int left = itemView.getRight() - margin - intrinsicWidth;
        int right = itemView.getRight() - margin;

        icDelete.setBounds(left, top, right, bottom);
        icDelete.draw(c);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}
