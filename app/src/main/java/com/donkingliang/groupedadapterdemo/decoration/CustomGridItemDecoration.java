package com.donkingliang.groupedadapterdemo.decoration;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.decoration.AbsGroupedGridItemDecoration;
import com.donkingliang.groupedadapterdemo.R;

/**
 * @Author teach liang
 * @Description
 * @Date 2020/11/26
 */
public class CustomGridItemDecoration extends AbsGroupedGridItemDecoration {

    private Drawable headerDivider;
    private Drawable footerDivider;
    private Drawable childColumnDivider1;
    private Drawable childColumnDivider2;
    private Drawable childRowDivider1;
    private Drawable childRowDivider2;

    public CustomGridItemDecoration(Context context, GroupedRecyclerViewAdapter adapter) {
        super(adapter);

        headerDivider = context.getResources().getDrawable(R.drawable.green_divider);
        footerDivider = context.getResources().getDrawable(R.drawable.purple_divider);
        childColumnDivider1 = context.getResources().getDrawable(R.drawable.pink_divider);
        childColumnDivider2 = context.getResources().getDrawable(R.drawable.orange_divider);
        childRowDivider1 = context.getResources().getDrawable(R.drawable.red_divider);
        childRowDivider2 = context.getResources().getDrawable(R.drawable.pink_divider);
    }

    @Override
    public int getHeaderDividerSize(int groupPosition) {
        // 根据position返回分割线的大小
        return 30;
    }

    @Override
    public Drawable getHeaderDivider(int groupPosition) {
        // 根据position返回Drawable 可以为null
        return headerDivider;
    }

    @Override
    public int getFooterDividerSize(int groupPosition) {
        // 根据position返回分割线的大小
        return 30;
    }

    @Override
    public Drawable getFooterDivider(int groupPosition) {
        // 根据position返回Drawable 可以为null
        return footerDivider;
    }

    @Override
    public int getChildColumnDividerSize(int groupPosition, int childPosition) {
        // 根据position返回分割线的大小
        return 20;
    }

    @Override
    public Drawable getChildColumnDivider(int groupPosition, int childPosition) {
        // 根据position返回Drawable 可以为null
        if(groupPosition % 2 == 0){
            return childColumnDivider1;
        } else {
            return childColumnDivider2;
        }
    }

    @Override
    public int getChildRowDividerSize(int groupPosition, int childPosition) {
        // 根据position返回分割线的大小
        return 10;
    }

    @Override
    public Drawable getChildRowDivider(int groupPosition, int childPosition) {
        // 根据position返回Drawable 可以为null
        if(groupPosition % 2 == 0){
            return childRowDivider1;
        } else {
            return childRowDivider2;
        }
    }
}
