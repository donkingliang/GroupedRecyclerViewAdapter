package com.donkingliang.groupedadapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donkingliang.groupedadapter.R;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.structure.GroupStructure;

import java.util.ArrayList;

/**
 * 通用的分组列表Adapter。通过它可以很方便的实现列表的分组效果。
 * 这个类提供了一系列的对列表的更新、删除和插入等操作的方法。
 * 使用者要使用这些方法的列表进行操作，而不要直接使用RecyclerView.Adapter的方法。
 * 因为当分组列表发生变化时，需要及时更新分组列表的组结构{@link GroupedRecyclerViewAdapter#mStructures}
 */
public abstract class GroupedRecyclerViewAdapter
        extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int TYPE_HEADER = R.integer.type_header;
    public static final int TYPE_FOOTER = R.integer.type_footer;
    public static final int TYPE_CHILD = R.integer.type_child;

    private OnHeaderClickListener mOnHeaderClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private OnChildClickListener mOnChildClickListener;

    protected Context mContext;
    //保存分组列表的组结构
    protected ArrayList<GroupStructure> mStructures = new ArrayList<>();
    //数据是否发生变化。如果数据发生变化，要及时更新组结构。
    private boolean isDataChanged;
    private int mTempPosition;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        structureChanged();
    }

    public GroupedRecyclerViewAdapter(Context context) {
        mContext = context;
        registerAdapterDataObserver(new GroupDataObserver());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(mTempPosition, viewType), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        int type = judgeType(position);
        final int groupPosition = getGroupPositionForPosition(position);
        if (type == TYPE_HEADER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnHeaderClickListener != null) {
                        mOnHeaderClickListener.onHeaderClick(GroupedRecyclerViewAdapter.this,
                                holder, groupPosition);
                    }
                }
            });
            onBindHeaderViewHolder(holder, groupPosition);
        } else if (type == TYPE_FOOTER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFooterClickListener != null) {
                        mOnFooterClickListener.onFooterClick(GroupedRecyclerViewAdapter.this,
                                holder, groupPosition);
                    }
                }
            });
            onBindFooterViewHolder(holder, groupPosition);
        } else if (type == TYPE_CHILD) {
            final int childPosition = getChildPositionForPosition(groupPosition, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnChildClickListener != null) {
                        mOnChildClickListener.onChildClick(GroupedRecyclerViewAdapter.this,
                                holder, groupPosition, childPosition);
                    }
                }
            });
            onBindChildViewHolder(holder, groupPosition, childPosition);
        }
    }

    @Override
    public int getItemCount() {
        if (isDataChanged) {
            structureChanged();
        }
        return count();
    }

    @Override
    public int getItemViewType(int position) {
        mTempPosition = position;
        int groupPosition = getGroupPositionForPosition(position);
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderViewType(groupPosition);
        } else if (type == TYPE_FOOTER) {
            return getFooterViewType(groupPosition);
        } else if (type == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, position);
            return getChildViewType(groupPosition, childPosition);
        }
        return super.getItemViewType(position);
    }

    public int getHeaderViewType(int groupPosition) {
        return TYPE_HEADER;
    }

    public int getFooterViewType(int groupPosition) {
        return TYPE_FOOTER;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        return TYPE_CHILD;
    }

    private int getLayoutId(int position, int viewType) {
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderLayout(viewType);
        } else if (type == TYPE_FOOTER) {
            return getFooterLayout(viewType);
        } else if (type == TYPE_CHILD) {
            return getChildLayout(viewType);
        }
        return 0;
    }

    private int count() {
        return countGroupRangeItem(0, mStructures.size());
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     *
     * @param position
     * @return
     */
    public int judgeType(int position) {
        int itemCount = 0;
        int groupCount = mStructures.size();

        for (int i = 0; i < groupCount; i++) {
            GroupStructure structure = mStructures.get(i);
            if (structure.hasHeader()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_HEADER;
                }
            }

            itemCount += structure.getChildrenCount();
            if (position < itemCount) {
                return TYPE_CHILD;
            }

            if (structure.hasFooter()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_FOOTER;
                }
            }
        }

//        throw new IndexOutOfBoundsException("不能确定下标" + position + "的Item类型。" +
//                "下标" + position + "已经超出了适配器的父子类型个数的总和：" + getItemCount());
        return 0;
    }

    /**
     * 重置组结构列表
     */
    private void structureChanged() {
        mStructures.clear();
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mStructures.add(new GroupStructure(hasHeader(i), hasFooter(i), getChildrenCount(i)));
        }
        isDataChanged = false;
    }

    /**
     * 根据下标计算position所在的组（groupPosition）
     *
     * @param position 下标
     * @return 组下标 groupPosition
     */
    public int getGroupPositionForPosition(int position) {
        int count = 0;
        int groupCount = mStructures.size();
        for (int i = 0; i < groupCount; i++) {
            count += countGroupItem(i);
            if (position < count) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param groupPosition 所在的组
     * @param position      下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForPosition(int groupPosition, int position) {
        if (groupPosition < mStructures.size()) {
            int itemCount = countGroupRangeItem(0, groupPosition + 1);
            GroupStructure structure = mStructures.get(groupPosition);
            int p = structure.getChildrenCount() - (itemCount - position)
                    + (structure.hasFooter() ? 1 : 0);
            if (p >= 0) {
                return p;
            }
        }
        return -1;
    }

    /**
     * 获取一个组的组头下标 如果该组没有组头 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupHeader(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasHeader()) {
                return -1;
            }
            int itemCount = countGroupRangeItem(0, groupPosition);
            return itemCount;
        }
        return -1;
    }

    /**
     * 获取一个组的组尾下标 如果该组没有组尾 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupFooter(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasFooter()) {
                return -1;
            }
            int itemCount = countGroupRangeItem(0, groupPosition + 1) - 1;
            return itemCount;
        }
        return -1;
    }

    /**
     * 获取一个组指定的子项下标 如果没有 返回-1
     *
     * @param groupPosition 组下标
     * @param childPosition 子项的组内下标
     * @return 下标
     */
    public int getPositionForChild(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.getChildrenCount() > childPosition) {
                int itemCount = countGroupRangeItem(0, groupPosition);
                return itemCount + childPosition + (structure.hasHeader() ? 1 : 0);
            }
        }
        return -1;
    }

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     *
     * @param groupPosition
     * @return
     */
    public int countGroupItem(int groupPosition) {
        int itemCount = 0;
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                itemCount += 1;
            }
            itemCount += structure.getChildrenCount();
            if (structure.hasFooter()) {
                itemCount += 1;
            }
        }
        return itemCount;
    }

    /**
     * 计算多个组的项的总和
     *
     * @return
     */
    public int countGroupRangeItem(int start, int count) {
        int itemCount = 0;
        int size = mStructures.size();
        for (int i = start; i < size && i < start + count; i++) {
            itemCount += countGroupItem(i);
        }
        return itemCount;
    }

    //****** 刷新操作 *****//

    /**
     * 刷新数据列表
     */
    public void changeDataSet() {
        isDataChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 刷新一组数据，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void changeGroup(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * 刷新多组数据，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void changeRangeGroup(int groupPosition, int count) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = 0;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * 刷新组头
     *
     * @param groupPosition
     */
    public void changeHeader(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新组尾
     *
     * @param groupPosition
     */
    public void changeFooter(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新一组里的某个子项
     *
     * @param groupPosition
     * @param childPosition
     */
    public void changeChild(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新一组里的多个子项
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void changeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                if (structure.getChildrenCount() >= childPosition + count) {
                    notifyItemRangeChanged(index, count);
                } else {
                    notifyItemRangeChanged(index, structure.getChildrenCount() - childPosition);
                }
            }
        }
    }

    /**
     * 刷新一组里的所有子项
     *
     * @param groupPosition
     */
    public void changeChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                notifyItemRangeChanged(index, structure.getChildrenCount());
            }
        }
    }

    //****** 删除操作 *****//

    /**
     * 删除所有数据
     */
    public void removeAll() {
        notifyItemRangeRemoved(0, getItemCount());
        mStructures.clear();
    }

    /**
     * 删除一组数据，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void removeGroup(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeRemoved(index, itemCount);
            notifyItemRangeChanged(index, getItemCount() - itemCount);
            mStructures.remove(groupPosition);
        }
    }

    /**
     * 删除多组数据，包括组头,组尾和子项
     *
     * @param groupPosition
     */
    public void removeRangeGroup(int groupPosition, int count) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = 0;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeRemoved(index, itemCount);
            notifyItemRangeChanged(index, getItemCount() - itemCount);
            mStructures.remove(groupPosition);
        }
    }

    /**
     * 删除组头
     *
     * @param groupPosition
     */
    public void removeHeader(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setHasHeader(false);
        }
    }

    /**
     * 删除组尾
     *
     * @param groupPosition
     */
    public void removeFooter(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setHasFooter(false);
        }
    }

    /**
     * 删除一组里的某个子项
     *
     * @param groupPosition
     * @param childPosition
     */
    public void removeChild(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setChildrenCount(structure.getChildrenCount() - 1);
        }
    }

    /**
     * 删除一组里的多个子项
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void removeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int childCount = structure.getChildrenCount();
                int removeCount = count;
                if (childCount < childPosition + count) {
                    removeCount = childCount - childPosition;
                }
                notifyItemRangeRemoved(index, removeCount);
                notifyItemRangeChanged(index, getItemCount() - removeCount);
                structure.setChildrenCount(childCount - removeCount);
            }
        }
    }

    /**
     * 删除一组里的所有子项
     *
     * @param groupPosition
     */
    public void removeChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int itemCount = structure.getChildrenCount();
                notifyItemRangeRemoved(index, itemCount);
                notifyItemRangeChanged(index, getItemCount() - itemCount);
                structure.setChildrenCount(0);
            }
        }
    }

    //****** 插入操作 *****//

    /**
     * 插入一组数据
     *
     * @param groupPosition
     */
    public void insertGroup(int groupPosition) {
        GroupStructure structure = new GroupStructure(hasHeader(groupPosition),
                hasFooter(groupPosition), getChildrenCount(groupPosition));
        if (groupPosition < mStructures.size()) {
            mStructures.add(groupPosition, structure);
        } else {
            mStructures.add(structure);
            groupPosition = mStructures.size() - 1;
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
            notifyItemRangeChanged(index + itemCount, getItemCount() - index);
        }
    }

    /**
     * 插入一组数据
     *
     * @param groupPosition
     */
    public void insertRangeGroup(int groupPosition, int count) {

        ArrayList<GroupStructure> list = new ArrayList<>();
        for (int i = groupPosition; i < count; i++) {
            GroupStructure structure = new GroupStructure(hasHeader(i),
                    hasFooter(i), getChildrenCount(i));
            list.add(structure);
        }

        if (groupPosition < mStructures.size()) {
            mStructures.addAll(groupPosition, list);
        } else {
            mStructures.addAll(list);
            groupPosition = mStructures.size() - list.size();
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupRangeItem(groupPosition, count);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
            notifyItemRangeChanged(index + itemCount, getItemCount() - index);
        }
    }

    /**
     * 插入组头
     *
     * @param groupPosition
     */
    public void insertHeader(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupHeader(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasHeader(true);
            int index = countGroupRangeItem(0, groupPosition);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入组尾
     *
     * @param groupPosition
     */
    public void insertFooter(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupFooter(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasFooter(true);
            int index = countGroupRangeItem(0, groupPosition + 1);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入一个子项到组里
     *
     * @param groupPosition
     * @param childPosition
     */
    public void insertChild(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            int index = getPositionForChild(groupPosition, childPosition);
            if (index < 0) {
                index = countGroupRangeItem(0, groupPosition);
                index += structure.hasHeader() ? 1 : 0;
                index += structure.getChildrenCount();
            }
            structure.setChildrenCount(structure.getChildrenCount() + 1);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入一组里的多个子项
     *
     * @param groupPosition
     * @param childPosition
     * @param count
     */
    public void insertRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            if (childPosition < structure.getChildrenCount()) {
                index += childPosition;
            } else {
                index += structure.getChildrenCount();
            }
            if (count > 0) {
                structure.setChildrenCount(structure.getChildrenCount() + count);
                notifyItemRangeInserted(index, count);
                notifyItemRangeChanged(index + count, getItemCount() - index);
            }
        }
    }

    /**
     * 插入一组里的所有子项
     *
     * @param groupPosition
     */
    public void insertChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            int itemCount = getChildrenCount(groupPosition);
            if (itemCount > 0) {
                structure.setChildrenCount(itemCount);
                notifyItemRangeInserted(index, itemCount);
                notifyItemRangeChanged(index + itemCount, getItemCount() - index);
            }
        }
    }

    //****** 设置点击事件 *****//

    /**
     * 设置组头点击事件
     *
     * @param listener
     */
    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mOnHeaderClickListener = listener;
    }

    /**
     * 设置组尾点击事件
     *
     * @param listener
     */
    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    /**
     * 设置子项点击事件
     *
     * @param listener
     */
    public void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int groupPosition);

    public abstract boolean hasHeader(int groupPosition);

    public abstract boolean hasFooter(int groupPosition);

    public abstract int getHeaderLayout(int viewType);

    public abstract int getFooterLayout(int viewType);

    public abstract int getChildLayout(int viewType);

    public abstract void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition);

    public abstract void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition);

    public abstract void onBindChildViewHolder(BaseViewHolder holder,
                                               int groupPosition, int childPosition);

    class GroupDataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            isDataChanged = true;
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition);
    }

    public interface OnFooterClickListener {
        void onFooterClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition);
    }

    public interface OnChildClickListener {
        void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                          int groupPosition, int childPosition);
    }
}