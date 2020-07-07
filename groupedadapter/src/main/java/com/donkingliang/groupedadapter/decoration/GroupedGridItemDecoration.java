package com.donkingliang.groupedadapter.decoration;

import android.graphics.drawable.Drawable;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

/**
 * @Author donkingliang QQ:1043214265 github:https://github.com/donkingliang
 * @Description
 * @Date 2020/7/7
 */
class GroupedGridItemDecoration extends AbsGroupedGridItemDecoration {

    private int mHeaderDividerSize;

    private Drawable mHeaderDivider;

    private int mFooterDividerSize;

    private Drawable mFooterDivider;

    private int mChildRowDividerSize;

    private Drawable mChildRowDivider;

    private int mChildColumnDividerSize;

    private Drawable mChildColumnDivider;

    public GroupedGridItemDecoration(GroupedRecyclerViewAdapter adapter,
                                     int mHeaderDividerSize, Drawable mHeaderDivider,
                                     int mFooterDividerSize, Drawable mFooterDivider,
                                     int mChildRowDividerSize, Drawable mChildRowDivider,
                                     int mChildColumnDividerSize, Drawable mChildColumnDivider) {
        super(adapter);
        this.mHeaderDividerSize = mHeaderDividerSize;
        this.mHeaderDivider = mHeaderDivider;
        this.mFooterDividerSize = mFooterDividerSize;
        this.mFooterDivider = mFooterDivider;
        this.mChildRowDividerSize = mChildRowDividerSize;
        this.mChildRowDivider = mChildRowDivider;
        this.mChildColumnDividerSize = mChildColumnDividerSize;
        this.mChildColumnDivider = mChildColumnDivider;
    }

    @Override
    public int getHeaderDividerSize(int groupPosition) {
        return mHeaderDividerSize;
    }

    @Override
    public Drawable getHeaderDivider(int groupPosition) {
        return mHeaderDivider;
    }

    @Override
    public int getFooterDividerSize(int groupPosition) {
        return mFooterDividerSize;
    }

    @Override
    public Drawable getFooterDivider(int groupPosition) {
        return mFooterDivider;
    }

    @Override
    public int getChildColumnDividerSize(int groupPosition, int childPosition) {
        return mChildColumnDividerSize;
    }

    @Override
    public Drawable getChildColumnDivider(int groupPosition, int childPosition) {
        return mChildColumnDivider;
    }

    @Override
    public int getChildRowDividerSize(int groupPosition, int childPosition) {
        return mChildRowDividerSize;
    }

    @Override
    public Drawable getChildRowDivider(int groupPosition, int childPosition) {
        return mChildRowDivider;
    }
}
