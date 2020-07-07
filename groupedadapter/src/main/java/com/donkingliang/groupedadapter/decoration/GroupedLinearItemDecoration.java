package com.donkingliang.groupedadapter.decoration;

import android.graphics.drawable.Drawable;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

/**
 * @Author donkingliang QQ:1043214265 github:https://github.com/donkingliang
 * @Description
 * @Date 2020/7/6
 */
public class GroupedLinearItemDecoration extends AbsGroupedLinearItemDecoration {

    private int mHeaderDividerSize;

    private Drawable mHeaderDivider;

    private int mFooterDividerSize;

    private Drawable mFooterDivider;

    private int mChildDividerSize;

    private Drawable mChildDivider;

    public GroupedLinearItemDecoration(GroupedRecyclerViewAdapter adapter,
                                       int mHeaderDividerSize, Drawable mHeaderDivider,
                                       int mFooterDividerSize, Drawable mFooterDivider,
                                       int mChildDividerSize, Drawable mChildDivider) {
        super(adapter);
        this.mHeaderDividerSize = mHeaderDividerSize;
        this.mHeaderDivider = mHeaderDivider;
        this.mFooterDividerSize = mFooterDividerSize;
        this.mFooterDivider = mFooterDivider;
        this.mChildDividerSize = mChildDividerSize;
        this.mChildDivider = mChildDivider;
    }

    @Override
    public int getChildDividerSize(int groupPosition, int ChildPosition) {
        return mHeaderDividerSize;
    }

    @Override
    public Drawable getChildDivider(int groupPosition, int ChildPosition) {
        return mHeaderDivider;
    }

    @Override
    public int getHeaderDividerSize(int groupPosition) {
        return mFooterDividerSize;
    }

    @Override
    public Drawable getHeaderDivider(int groupPosition) {
        return mFooterDivider;
    }

    @Override
    public int getFooterDividerSize(int groupPosition) {
        return mChildDividerSize;
    }

    @Override
    public Drawable getFooterDivider(int groupPosition) {
        return mChildDivider;
    }
}
