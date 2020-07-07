package com.donkingliang.groupedadapter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

/**
 * @Author donkingliang QQ:1043214265 github:https://github.com/donkingliang
 * @Description
 * @Date 2020/7/7
 */
public abstract class AbsGroupedGridItemDecoration extends RecyclerView.ItemDecoration implements IGroupedItemDecoration {

    protected GroupedRecyclerViewAdapter mAdapter;

    private final Rect mBounds = new Rect();

    public AbsGroupedGridItemDecoration(GroupedRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (!checkLayoutManager(parent)) {
            return;
        }

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();

        canvas.save();

        if (parent.getClipToPadding()) {
            // 设置绘制画布去掉padding
            canvas.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                    parent.getWidth() - parent.getPaddingRight(), parent.getHeight() - parent.getPaddingBottom());
        }

        final int childCount = parent.getChildCount();

        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanCount = layoutManager.getSpanCount();
        int itemCount = layoutManager.getItemCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);

            int position = parent.getChildAdapterPosition(child);
            int itemType = mAdapter.judgeType(position);
            int groupPosition = mAdapter.getGroupPositionForPosition(position);
            int childPosition = mAdapter.getChildPositionForPosition(groupPosition, position);

            Drawable columnDivider = getColumnDividerForType(itemType, groupPosition, childPosition, orientation);
            boolean hasRightDivider = columnDivider != null;
            if (hasRightDivider) {
                final int right = mBounds.right + Math.round(child.getTranslationX());
                final int left = right - getColumnDividerSizeForType(itemType, groupPosition, childPosition, orientation);
                columnDivider.setBounds(left, mBounds.top, right, mBounds.bottom);
                columnDivider.draw(canvas);
            }

            Drawable rowDivider = getRowDividerForType(itemType, groupPosition, childPosition, orientation);
            boolean hasBottomDivider = rowDivider != null;
            if (hasBottomDivider) {
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - getColumnDividerSizeForType(itemType, groupPosition, childPosition, orientation);
                rowDivider.setBounds(mBounds.left, top, mBounds.right, bottom);
                rowDivider.draw(canvas);
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

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanCount = layoutManager.getSpanCount();
        int itemCount = layoutManager.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int itemType = mAdapter.judgeType(position);
        int groupPosition = mAdapter.getGroupPositionForPosition(position);
        int childPosition = mAdapter.getChildPositionForPosition(groupPosition, position);
        int right = getColumnDividerSizeForType(itemType, groupPosition, childPosition, orientation);

        int bottom = getRowDividerSizeForType(itemType, groupPosition, childPosition, orientation);

        outRect.set(0, 0, right, bottom);
    }

    private Drawable getRowDividerForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getHeaderDivider(groupPosition);
            } else {
                return null;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getFooterDivider(groupPosition);
            } else {
                return null;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            return getChildRowDivider(groupPosition, childPosition);
        } else {
            return null;
        }
    }

    private int getRowDividerSizeForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getHeaderDividerSize(groupPosition);
            } else {
                return 0;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getFooterDividerSize(groupPosition);
            } else {
                return 0;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            return getChildRowDividerSize(groupPosition, childPosition);
        } else {
            return 0;
        }
    }

    private Drawable getColumnDividerForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getHeaderDivider(groupPosition);
            } else {
                return null;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getFooterDivider(groupPosition);
            } else {
                return null;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            return getChildColumnDivider(groupPosition, childPosition);
        } else {
            return null;
        }
    }

    private int getColumnDividerSizeForType(int itemType, int groupPosition, int childPosition, int orientation) {
        if (itemType == GroupedRecyclerViewAdapter.TYPE_HEADER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getHeaderDividerSize(groupPosition);
            } else {
                return 0;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_FOOTER) {
            if (orientation == RecyclerView.VERTICAL) {
                return getFooterDividerSize(groupPosition);
            } else {
                return 0;
            }
        } else if (itemType == GroupedRecyclerViewAdapter.TYPE_CHILD) {
            return getChildColumnDividerSize(groupPosition, childPosition);
        } else {
            return 0;
        }
    }

    /**
     * 判断是否是位于右边的item
     *
     * @param view
     * @param position
     * @return
     */
    public boolean isRightItem(RecyclerView view, int position) {
        if (!checkLayoutManager(view)) {
            return false;
        }

        GridLayoutManager layoutManager = (GridLayoutManager) view.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanCount = layoutManager.getSpanCount();
        int itemCount = layoutManager.getItemCount();
        return isRightItem(position, spanCount, itemCount, spanSizeLookup, orientation);
    }

    /**
     * 判断是否是位于右边的item
     */
    private boolean isRightItem(int position, int spanCount, int itemCount, GridLayoutManager.SpanSizeLookup spanSizeLookup, int orientation) {

        if (orientation == RecyclerView.VERTICAL) {
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            int positionSpanSize = spanSizeLookup.getSpanSize(position);
            return positionSpanSize + spanIndex == spanCount;
        } else {

            // 最后一列的开始位置
            int lastColumnPosition = itemCount - 1;
            while (lastColumnPosition >= 0 && spanSizeLookup.getSpanIndex(lastColumnPosition, spanCount) != 0) {
                lastColumnPosition--;
            }

            return lastColumnPosition <= position;
        }
    }

    /**
     * 判断是否是位于底部的item
     *
     * @param view
     * @param position
     * @return
     */
    public boolean isBottomItem(RecyclerView view, int position) {
        if (!checkLayoutManager(view)) {
            return false;
        }

        GridLayoutManager layoutManager = (GridLayoutManager) view.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
        int spanCount = layoutManager.getSpanCount();
        int itemCount = layoutManager.getItemCount();
        return isBottomItem(position, spanCount, itemCount, spanSizeLookup, orientation);
    }

    /**
     * 判断是否是位于底部的item
     */
    private boolean isBottomItem(int position, int spanCount, int itemCount, GridLayoutManager.SpanSizeLookup spanSizeLookup, int orientation) {

        if (orientation == RecyclerView.VERTICAL) {
            // 最后一行的开始位置
            int lastRowPosition = itemCount - 1;
            while (lastRowPosition >= 0 && spanSizeLookup.getSpanIndex(lastRowPosition, spanCount) != 0) {
                lastRowPosition--;
            }
            return lastRowPosition <= position;
        } else {
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            int positionSpanSize = spanSizeLookup.getSpanSize(position);
            return positionSpanSize + spanIndex == spanCount;
        }
    }


    @Override
    public boolean checkLayoutManager(RecyclerView view) {
        return view.getLayoutManager() != null && view.getLayoutManager() instanceof GridLayoutManager;
    }


}
