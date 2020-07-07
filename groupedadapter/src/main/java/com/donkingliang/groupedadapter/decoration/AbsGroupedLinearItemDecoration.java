package com.donkingliang.groupedadapter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

/**
 * @Author donkingliang QQ:1043214265 github:https://github.com/donkingliang
 * @Description
 * @Date 2020/7/6
 */
public abstract class AbsGroupedLinearItemDecoration extends RecyclerView.ItemDecoration implements IGroupedItemDecoration {

    protected GroupedRecyclerViewAdapter mAdapter;

    private final Rect mBounds = new Rect();

    public AbsGroupedLinearItemDecoration(GroupedRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (!checkLayoutManager(parent)) {
            return;
        }

        canvas.save();

        if (parent.getClipToPadding()) {
            // 设置绘制画布去掉padding
            canvas.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                    parent.getWidth() - parent.getPaddingRight(), parent.getHeight() - parent.getPaddingBottom());
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        int itemCount = layoutManager.getItemCount();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            int itemType = mAdapter.judgeType(position);
            int groupPosition = mAdapter.getGroupPositionForPosition(position);
            int childPosition = mAdapter.getChildPositionForPosition(groupPosition, position);
            Drawable divider = getDividerForType(itemType, groupPosition, childPosition, orientation);

//
//            // 最后的item是否显示Divider
//            if (!isShowLastDivider() && isLastItem(position, itemCount)) {
//                continue;
//            }

            if (divider != null) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                int dividerSize = getDividerSizeForType(itemType, groupPosition, childPosition, orientation);

                if (orientation == RecyclerView.VERTICAL) {
                    final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                    final int top = bottom - dividerSize;
                    divider.setBounds(mBounds.left, top, mBounds.right, bottom);
                    divider.draw(canvas);
                } else {
                    final int right = mBounds.right + Math.round(child.getTranslationX());
                    final int left = right - dividerSize;
                    divider.setBounds(left, mBounds.top, right, mBounds.bottom);
                    divider.draw(canvas);
                }
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (!checkLayoutManager(parent)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        int itemCount = layoutManager.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int itemType = mAdapter.judgeType(position);
        int groupPosition = mAdapter.getGroupPositionForPosition(position);
        int childPosition = mAdapter.getChildPositionForPosition(groupPosition, position);

//        // 最后的item是否显示Divider
//        if (!isShowLastDivider() && isLastItem(position, itemCount)) {
//            outRect.set(0, 0, 0, 0);
//            return;
//        }

        int dividerSize = getDividerSizeForType(itemType, groupPosition, childPosition, orientation);

        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(0, 0, 0, dividerSize);
        } else {
            outRect.set(0, 0, dividerSize, 0);
        }
    }

    private Drawable getDividerForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            return getHeaderDivider(groupPosition);
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            return getFooterDivider(groupPosition);
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            if (orientation == RecyclerView.VERTICAL) {
                return getChildRowDivider(groupPosition, childPosition);
            } else {
                return getChildColumnDivider(groupPosition, childPosition);
            }
        } else {
            return null;
        }
    }

    private int getDividerSizeForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            return getHeaderDividerSize(groupPosition);
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            return getFooterDividerSize(groupPosition);
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            if (orientation == RecyclerView.VERTICAL) {
                return getChildRowDividerSize(groupPosition, childPosition);
            } else {
                return getChildColumnDividerSize(groupPosition, childPosition);
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getChildColumnDividerSize(int groupPosition, int childPosition) {
        return getChildDividerSize(groupPosition, childPosition);
    }

    @Override
    public Drawable getChildColumnDivider(int groupPosition, int childPosition) {
        return getChildDivider(groupPosition, childPosition);
    }

    @Override
    public int getChildRowDividerSize(int groupPosition, int childPosition) {
        return getChildDividerSize(groupPosition, childPosition);
    }

    @Override
    public Drawable getChildRowDivider(int groupPosition, int childPosition) {
        return getChildDivider(groupPosition, childPosition);
    }

    @Override
    public boolean checkLayoutManager(RecyclerView view) {
        return view.getLayoutManager() != null && view.getLayoutManager() instanceof LinearLayoutManager;
    }

    public abstract int getChildDividerSize(int groupPosition, int ChildPosition);

    public abstract Drawable getChildDivider(int groupPosition, int ChildPosition);
}
