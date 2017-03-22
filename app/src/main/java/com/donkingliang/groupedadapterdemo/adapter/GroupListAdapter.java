package com.donkingliang.groupedadapterdemo.adapter;

import android.content.Context;

import com.donkingliang.groupedadapterdemo.R;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapterdemo.entity.ChildEntity;
import com.donkingliang.groupedadapterdemo.entity.GroupEntity;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;

import java.util.ArrayList;

/**
 * 这是普通的分组Adapter 每一个组都有头部、尾部和子项。
 */
public class GroupListAdapter extends GroupedRecyclerViewAdapter {

    private ArrayList<GroupEntity> mGroups;

    public GroupListAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context);
        mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildEntity> children = mGroups.get(groupPosition).getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_child;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_header, entity.getHeader());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_footer, entity.getFooter());
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ChildEntity entity = mGroups.get(groupPosition).getChildren().get(childPosition);
        holder.setText(R.id.tv_child, entity.getChild());
    }
}
