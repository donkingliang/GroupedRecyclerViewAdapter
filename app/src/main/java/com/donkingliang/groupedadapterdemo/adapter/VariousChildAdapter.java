package com.donkingliang.groupedadapterdemo.adapter;

import android.content.Context;

import com.donkingliang.groupedadapterdemo.entity.GroupEntity;

import java.util.ArrayList;

/**
 * 多种不同子项的列表 它跟{@link VariousAdapter}相比只是去掉了头部和尾部。
 * 去掉头部和尾部的方式跟{@link NoHeaderAdapter}和{@link NoFooterAdapter}一样。
 * 这种列表适用于把多个不同的列表合并成一个列表。
 */
public class VariousChildAdapter extends VariousAdapter {

    public VariousChildAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context, groups);
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

}
