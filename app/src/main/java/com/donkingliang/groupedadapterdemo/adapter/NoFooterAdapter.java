package com.donkingliang.groupedadapterdemo.adapter;

import android.content.Context;


import com.donkingliang.groupedadapterdemo.entity.GroupEntity;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * 这是不带组尾的Adapter。
 * 只需要{@link GroupedRecyclerViewAdapter#hasFooter(int)}方法返回false就可以去掉组尾了。
 */
public class NoFooterAdapter extends GroupListAdapter {

    public NoFooterAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context, groups);
    }

    /**
     * 返回false表示没有组尾
     *
     * @param groupPosition
     * @return
     */
    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    /**
     * 当hasFooter返回false时，这个方法不会被调用。
     *
     * @return
     */
    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    /**
     * 当hasFooter返回false时，这个方法不会被调用。
     *
     * @param holder
     * @param groupPosition
     */
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

}
