package com.donkingliang.groupedadapter.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;

import java.lang.reflect.Method;

/**
 * Depiction:头部吸顶布局。只要用StickyHeaderLayout包裹{@link RecyclerView},
 * 并且使用{@link GroupedRecyclerViewAdapter},就可以实现列表头部吸顶功能。
 * StickyHeaderLayout只能包裹RecyclerView，而且只能包裹一个RecyclerView。
 * <p>
 * Author:donkingliang  QQ:1043214265
 * Dat:2017/11/14
 */
public class StickyHeaderLayout extends FrameLayout {

    private Context mContext;
    private RecyclerView mRecyclerView;

    //吸顶容器，用于承载吸顶布局。
    private FrameLayout mStickyLayout;

    //保存吸顶布局的缓存池。它以列表组头的viewType为key,ViewHolder为value对吸顶布局进行保存和回收复用。
    private final SparseArray<BaseViewHolder> mStickyViews = new SparseArray<>();

    //用于在吸顶布局中保存viewType的key。
    private final int VIEW_TAG_TYPE = -101;

    //用于在吸顶布局中保存ViewHolder的key。
    private final int VIEW_TAG_HOLDER = -102;

    //记录当前吸顶的组。
    private int mCurrentStickyGroup = -1;

    //是否吸顶。
    private boolean isSticky = true;

    //是否已经注册了adapter刷新监听
    private boolean isRegisterDataObserver = false;

    public StickyHeaderLayout(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public StickyHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0 || !(child instanceof RecyclerView)) {
            //外界只能向StickyHeaderLayout添加一个RecyclerView,而且只能添加RecyclerView。
            throw new IllegalArgumentException("StickyHeaderLayout can host only one direct child --> RecyclerView");
        }
        super.addView(child, index, params);
        mRecyclerView = (RecyclerView) child;
        addOnScrollListener();
        addStickyLayout();
    }

    /**
     * 添加滚动监听
     */
    private void addOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // 在滚动的时候，需要不断的更新吸顶布局。
                if (isSticky) {
                    updateStickyView(false);
                }
            }
        });
    }

    /**
     * 添加吸顶容器
     */
    private void addStickyLayout() {
        mStickyLayout = new FrameLayout(mContext);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mStickyLayout.setLayoutParams(lp);
        super.addView(mStickyLayout, 1, lp);
    }

    /**
     * 更新吸顶布局。
     *
     * @param imperative 是否强制更新。
     */
    private void updateStickyView(boolean imperative) {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        //只有RecyclerView的adapter是GroupedRecyclerViewAdapter的时候，才会添加吸顶布局。
        if (adapter instanceof GroupedRecyclerViewAdapter) {
            GroupedRecyclerViewAdapter gAdapter = (GroupedRecyclerViewAdapter) adapter;
            registerAdapterDataObserver(gAdapter);
            //获取列表显示的第一个项。
            int firstVisibleItem = getFirstVisibleItem();
            //通过显示的第一个项的position获取它所在的组。
            int groupPosition = gAdapter.getGroupPositionForPosition(firstVisibleItem);

            //如果当前吸顶的组头不是我们要吸顶的组头，就更新吸顶布局。这样做可以避免频繁的更新吸顶布局。
            if (imperative || mCurrentStickyGroup != groupPosition) {
                mCurrentStickyGroup = groupPosition;

                //通过groupPosition获取当前组的组头position。这个组头就是我们需要吸顶的布局。
                int groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition);
                if (groupHeaderPosition != -1) {
                    //获取吸顶布局的viewType。
                    int viewType = gAdapter.getItemViewType(groupHeaderPosition);

                    //如果当前的吸顶布局的类型和我们需要的一样，就直接获取它的ViewHolder，否则就回收。
                    BaseViewHolder holder = recycleStickyView(viewType);

                    //标志holder是否是从当前吸顶布局取出来的。
                    boolean flag = holder != null;

                    if (holder == null) {
                        //从缓存池中获取吸顶布局。
                        holder = getStickyViewByType(viewType);
                    }

                    if (holder == null) {
                        //如果没有从缓存池中获取到吸顶布局，则通过GroupedRecyclerViewAdapter创建。
                        holder = (BaseViewHolder) gAdapter.onCreateViewHolder(mStickyLayout, viewType);
                        holder.itemView.setTag(VIEW_TAG_TYPE, viewType);
                        holder.itemView.setTag(VIEW_TAG_HOLDER, holder);
                    }

                    //通过GroupedRecyclerViewAdapter更新吸顶布局的数据。
                    //这样可以保证吸顶布局的显示效果跟列表中的组头保持一致。
                    gAdapter.onBindViewHolder(holder, groupHeaderPosition);

                    //如果holder不是从当前吸顶布局取出来的，就需要把吸顶布局添加到容器里。
                    if (!flag) {
                        mStickyLayout.addView(holder.itemView);
                    }
                } else {
                    //如果当前组没有组头，则不显示吸顶布局。
                    //回收旧的吸顶布局。
                    recycle();
                }
            }

            //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
            if (mStickyLayout.getChildCount() > 0 && mStickyLayout.getHeight() == 0) {
                mStickyLayout.requestLayout();
            }

            //设置mStickyLayout的Y偏移量。
            mStickyLayout.setTranslationY(calculateOffset(gAdapter, firstVisibleItem, groupPosition + 1));
        }
    }

    /**
     * 注册adapter刷新监听
     */
    private void registerAdapterDataObserver(GroupedRecyclerViewAdapter adapter) {
        if (!isRegisterDataObserver) {
            isRegisterDataObserver = true;
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    updateStickyViewDelayed();
                }

            });
        }
    }

    private void updateStickyViewDelayed() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStickyView(true);
            }
        }, 100);
    }

    /**
     * 判断是否需要先回收吸顶布局，如果要回收，则回收吸顶布局并返回null。
     * 如果不回收，则返回吸顶布局的ViewHolder。
     * 这样做可以避免频繁的添加和移除吸顶布局。
     *
     * @param viewType
     * @return
     */
    private BaseViewHolder recycleStickyView(int viewType) {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            int type = (int) view.getTag(VIEW_TAG_TYPE);
            if (type == viewType) {
                return (BaseViewHolder) view.getTag(VIEW_TAG_HOLDER);
            } else {
                recycle();
            }
        }
        return null;
    }

    /**
     * 回收并移除吸顶布局
     */
    private void recycle() {
        if (mStickyLayout.getChildCount() > 0) {
            View view = mStickyLayout.getChildAt(0);
            mStickyViews.put((int) (view.getTag(VIEW_TAG_TYPE)),
                    (BaseViewHolder) (view.getTag(VIEW_TAG_HOLDER)));
            mStickyLayout.removeAllViews();
        }
    }

    /**
     * 从缓存池中获取吸顶布局
     *
     * @param viewType 吸顶布局的viewType
     * @return
     */
    private BaseViewHolder getStickyViewByType(int viewType) {
        return mStickyViews.get(viewType);
    }

    /**
     * 计算StickyLayout的偏移量。因为如果下一个组的组头顶到了StickyLayout，
     * 就要把StickyLayout顶上去，直到下一个组的组头变成吸顶布局。否则会发生两个组头重叠的情况。
     *
     * @param gAdapter
     * @param firstVisibleItem 当前列表显示的第一个项。
     * @param groupPosition    下一个组的组下标。
     * @return 返回偏移量。
     */
    private float calculateOffset(GroupedRecyclerViewAdapter gAdapter, int firstVisibleItem, int groupPosition) {
        int groupHeaderPosition = gAdapter.getPositionForGroupHeader(groupPosition);
        if (groupHeaderPosition != -1) {
            int index = groupHeaderPosition - firstVisibleItem;
            if (mRecyclerView.getChildCount() > index) {
                //获取下一个组的组头的itemView。
                View view = mRecyclerView.getChildAt(index);
                float off = view.getY() - mStickyLayout.getHeight();
                if (off < 0) {
                    return off;
                }
            }
        }
        return 0;
    }

    /**
     * 获取当前第一个显示的item .
     */
    private int getFirstVisibleItem() {
        int firstVisibleItem = -1;
        RecyclerView.LayoutManager layout = mRecyclerView.getLayoutManager();
        if (layout != null) {
            if (layout instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = getMin(firstPositions);
            }
        }
        return firstVisibleItem;
    }

    private int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min)
                min = arr[x];
        }
        return min;
    }

    /**
     * 是否吸顶
     *
     * @return
     */
    public boolean isSticky() {
        return isSticky;
    }

    /**
     * 设置是否吸顶。
     *
     * @param sticky
     */
    public void setSticky(boolean sticky) {
        if (isSticky != sticky) {
            isSticky = sticky;
            if (mStickyLayout != null) {
                if (isSticky) {
                    mStickyLayout.setVisibility(VISIBLE);
                    updateStickyView(false);
                } else {
                    recycle();
                    mStickyLayout.setVisibility(GONE);
                }
            }
        }
    }

    @Override
    protected int computeVerticalScrollOffset() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollOffset");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollOffset();
    }


    @Override
    protected int computeVerticalScrollRange() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollRange");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollExtent");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollExtent();
    }

    @Override
    public void scrollBy(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollBy(x,y);
        } else {
            super.scrollBy(x, y);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollTo(x,y);
        } else {
            super.scrollTo(x, y);
        }
    }
}
