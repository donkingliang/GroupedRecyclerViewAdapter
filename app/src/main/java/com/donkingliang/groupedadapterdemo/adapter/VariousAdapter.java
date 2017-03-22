package com.donkingliang.groupedadapterdemo.adapter;

import android.content.Context;

import com.donkingliang.groupedadapterdemo.R;
import com.donkingliang.groupedadapterdemo.entity.ChildEntity;
import com.donkingliang.groupedadapterdemo.entity.GroupEntity;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * 头、尾和子项都支持多种类型的Adapter。他跟普通的{@link GroupListAdapter}基本是一样的。
 * 只需要重写{@link GroupedRecyclerViewAdapter}里的三个方法就可以实现头、尾和子项的多种类型。
 * 使用的方式跟普通的RecyclerView实现多种type是一样的。
 * {@link GroupedRecyclerViewAdapter#getHeaderViewType(int)} 返回Header的viewType
 * {@link GroupedRecyclerViewAdapter#getFooterViewType(int)}返回Footer的viewType
 * {@link GroupedRecyclerViewAdapter#getChildViewType(int, int)}返回Child的viewType
 */
public class VariousAdapter extends GroupedRecyclerViewAdapter {

    public static final int TYPE_HEADER_1 = 1;
    public static final int TYPE_HEADER_2 = 2;
    public static final int TYPE_FOOTER_1 = 3;
    public static final int TYPE_FOOTER_2 = 4;
    public static final int TYPE_CHILD_1 = 5;
    public static final int TYPE_CHILD_2 = 6;

    private ArrayList<GroupEntity> mGroups;

    public VariousAdapter(Context context, ArrayList<GroupEntity> groups) {
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
        if (viewType == TYPE_HEADER_1) {
            return R.layout.adapter_header;
        } else {
            return R.layout.adapter_header_2;
        }
    }

    @Override
    public int getFooterLayout(int viewType) {
        if (viewType == TYPE_FOOTER_1) {
            return R.layout.adapter_footer;
        } else {
            return R.layout.adapter_footer_2;
        }
    }

    @Override
    public int getChildLayout(int viewType) {
        if (viewType == TYPE_CHILD_1) {
            return R.layout.adapter_child;
        } else {
            return R.layout.adapter_child_2;
        }
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        int viewType = getHeaderViewType(groupPosition);
        if (viewType == TYPE_HEADER_1) {
            holder.setText(R.id.tv_header, "第一种头部：" + entity.getHeader());
        } else if (viewType == TYPE_HEADER_2) {
            holder.setText(R.id.tv_header_2, "第二种头部：" + entity.getHeader());
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupEntity entity = mGroups.get(groupPosition);
        int viewType = getFooterViewType(groupPosition);
        if (viewType == TYPE_FOOTER_1) {
            holder.setText(R.id.tv_footer, "第一种尾部：" + entity.getFooter());
        } else if (viewType == TYPE_FOOTER_2) {
            holder.setText(R.id.tv_footer_2, "第二种尾部：" + entity.getFooter());
        }
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ChildEntity entity = mGroups.get(groupPosition).getChildren().get(childPosition);
        int viewType = getChildViewType(groupPosition, childPosition);
        if (viewType == TYPE_CHILD_1) {
            holder.setText(R.id.tv_child, "第一种子项：" + entity.getChild());
        } else if (viewType == TYPE_CHILD_2) {
            holder.setText(R.id.tv_child_2, "第二种子项：" + entity.getChild());
        }
    }

    @Override
    public int getHeaderViewType(int groupPosition) {
        if (groupPosition % 2 == 0) {
            return TYPE_HEADER_1;
        } else {
            return TYPE_HEADER_2;
        }
    }

    @Override
    public int getFooterViewType(int groupPosition) {
        if (groupPosition % 2 == 0) {
            return TYPE_FOOTER_1;
        } else {
            return TYPE_FOOTER_2;
        }
    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        if (groupPosition % 2 == 0) {
            return TYPE_CHILD_1;
        } else {
            return TYPE_CHILD_2;
        }
    }
}
