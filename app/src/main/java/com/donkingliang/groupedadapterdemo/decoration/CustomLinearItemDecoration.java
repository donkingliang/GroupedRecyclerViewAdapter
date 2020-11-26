package com.donkingliang.groupedadapterdemo.decoration;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.decoration.AbsGroupedLinearItemDecoration;
import com.donkingliang.groupedadapterdemo.R;

/**
 * @Author teach liang
 * @Description
 * @Date 2020/11/26
 */
public class CustomLinearItemDecoration extends AbsGroupedLinearItemDecoration {

    private Drawable headerDivider;
    private Drawable footerDivider;
    private Drawable childDivider1;
    private Drawable childDivider2;

    public CustomLinearItemDecoration(Context context, GroupedRecyclerViewAdapter adapter) {
        super(adapter);

        headerDivider = context.getResources().getDrawable(R.drawable.green_divider);
        footerDivider = context.getResources().getDrawable(R.drawable.purple_divider);
        childDivider1 = context.getResources().getDrawable(R.drawable.pink_divider);
        childDivider2 = context.getResources().getDrawable(R.drawable.orange_divider);
    }

    @Override
    public int getChildDividerSize(int groupPosition, int ChildPosition) {
        // 根据position返回分割线的大小
        return 20;
    }

    @Override
    public Drawable getChildDivider(int groupPosition, int ChildPosition) {
        // 根据position返回Drawable 可以为null
        if(groupPosition % 2 == 0){
            return childDivider1;
        } else {
            return childDivider2;
        }
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
}
