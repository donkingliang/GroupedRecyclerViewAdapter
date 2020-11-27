package com.donkingliang.groupedadapter.decoration;

import android.graphics.drawable.Drawable;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

/**
 * @Author donkingliang QQ:1043214265 github:https://github.com/donkingliang
 * @Description
 * @Date 2020/7/7
 */
public class GroupedGridItemDecoration extends AbsGroupedGridItemDecoration {

    private int mHeaderDividerSize;

    private Drawable mHeaderDivider;

    private int mFooterDividerSize;

    private Drawable mFooterDivider;

    private int mChildRowDividerSize;

    private Drawable mChildRowDivider;

    private int mChildColumnDividerSize;

    private Drawable mChildColumnDivider;

    public GroupedGridItemDecoration(GroupedRecyclerViewAdapter adapter,
                                     int headerDividerSize, Drawable headerDivider,
                                     int footerDividerSize, Drawable footerDivider,
                                     int childRowDividerSize, Drawable childRowDivider,
                                     int childColumnDividerSize, Drawable childColumnDivider) {
        super(adapter);
        this.mHeaderDividerSize = headerDividerSize;
        this.mHeaderDivider = headerDivider;
        this.mFooterDividerSize = footerDividerSize;
        this.mFooterDivider = footerDivider;
        this.mChildRowDividerSize = childRowDividerSize;
        this.mChildRowDivider = childRowDivider;
        this.mChildColumnDividerSize = childColumnDividerSize;
        this.mChildColumnDivider = childColumnDivider;
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
