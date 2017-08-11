package com.justApp.RadioPlayer.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justApp.RadioPlayer.R;

/**
 * @author Sergey Rodionov
 */
public class SimpleHorizontalDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private int mLeftPadding;

    public SimpleHorizontalDivider(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        mLeftPadding = (int) context.getResources().getDimension(R.dimen.padding_medium);
        styledAttributes.recycle();
    }

    public SimpleHorizontalDivider(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + mLeftPadding;
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        final int position = params.getViewAdapterPosition();

        if (position < state.getItemCount() - 1) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicWidth());
        } else {
            outRect.setEmpty();
        }
    }
}
