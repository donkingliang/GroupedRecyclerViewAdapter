package com.donkingliang.groupedadapter.layoutmanger;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;


/**
 * 为分组列表提供的GridLayoutManager。
 * 因为分组列表如果要使用GridLayoutManager实现网格布局。要保证组的头部和尾部是要单独占用一行的。
 * 否则组的头、尾可能会跟子项混着一起，造成布局混乱。
 */
public class GroupedGridLayoutManager extends GridLayoutManager {

    private GroupedRecyclerViewAdapter mAdapter;

    public GroupedGridLayoutManager(Context context, int spanCount,
                                    GroupedRecyclerViewAdapter adapter) {
        super(context, spanCount);
        mAdapter = adapter;
        setSpanSizeLookup();
    }

    public GroupedGridLayoutManager(Context context, int spanCount, int orientation,
                                    boolean reverseLayout, GroupedRecyclerViewAdapter adapter) {
        super(context, spanCount, orientation, reverseLayout);
        this.mAdapter = adapter;
        setSpanSizeLookup();
    }

    public GroupedGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                    int defStyleRes, GroupedRecyclerViewAdapter adapter) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mAdapter = adapter;
        setSpanSizeLookup();
    }

    private void setSpanSizeLookup() {
        super.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int count = getSpanCount();
                if (mAdapter != null) {
                    int type = mAdapter.judgeType(position);
                    //只对子项做Grid效果
                    if (type == GroupedRecyclerViewAdapter.TYPE_CHILD) {
                        int groupPosition = mAdapter.getGroupPositionForPosition(position);
                        int childPosition =
                                mAdapter.getChildPositionForPosition(groupPosition, position);
                        return getChildSpanSize(groupPosition, childPosition);
                    }
                }

                return count;
            }
        });
    }

    /**
     * 提供这个方法可以使外部改变子项的SpanSize。
     * 这个方法的作用跟{@link SpanSizeLookup#getSpanSize(int)}一样。
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public int getChildSpanSize(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {

    }
}