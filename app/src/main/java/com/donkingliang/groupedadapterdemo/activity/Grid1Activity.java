package com.donkingliang.groupedadapterdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.donkingliang.groupedadapter.decoration.GroupedGridItemDecoration;
import com.donkingliang.groupedadapter.decoration.GroupedLinearItemDecoration;
import com.donkingliang.groupedadapterdemo.R;
import com.donkingliang.groupedadapterdemo.adapter.GroupedListAdapter;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.donkingliang.groupedadapterdemo.decoration.CustomGridItemDecoration;
import com.donkingliang.groupedadapterdemo.decoration.CustomLinearItemDecoration;
import com.donkingliang.groupedadapterdemo.model.GroupModel;


/**
 * 子项为Grid布局的分组列表。给RecyclerView的LayoutManager
 * 设置为{@link GroupedGridLayoutManager}即可。
 */
public class Grid1Activity extends AppCompatActivity {

    private RecyclerView rvList;
    private RecyclerView.ItemDecoration itemDecoration;
    private GroupedListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        rvList = (RecyclerView) findViewById(R.id.rv_list);

        adapter = new GroupedListAdapter(this, GroupModel.getGroups(10, 10));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition) {
                Toast.makeText(Grid1Activity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition) {
                Toast.makeText(Grid1Activity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
                Toast.makeText(Grid1Activity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        rvList.setAdapter(adapter);


        //直接使用GroupedGridLayoutManager实现子项的Grid效果
        GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(this, 2, adapter);
        rvList.setLayoutManager(gridLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_ment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 移除前面的ItemDecoration
        removeItemDecoration();

        switch (item.getItemId()) {
            case R.id.none:

                itemDecoration = null;
                return true;

            case R.id.space:

                // 空白分割线，只需要设置分割线大小，不需要设置样式
                itemDecoration = new GroupedGridItemDecoration(adapter, 20, null,
                        20, null, 20, null, 20, null);
                rvList.addItemDecoration(itemDecoration);
                return true;

            case R.id.ordinary:

                // 普通分割线，设置分割线大小和头、尾、子项的分割线样式
                itemDecoration = new GroupedGridItemDecoration(adapter,
                        20, getResources().getDrawable(R.drawable.green_divider),
                        20, getResources().getDrawable(R.drawable.purple_divider),
                        20, getResources().getDrawable(R.drawable.pink_divider),
                        20, getResources().getDrawable(R.drawable.orange_divider));
                rvList.addItemDecoration(itemDecoration);
                return true;

            case R.id.custom:

                // 自定义分割线，可以根据需要设置每个item的分割线大小和样式
                itemDecoration = new CustomGridItemDecoration(this, adapter);
                rvList.addItemDecoration(itemDecoration);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void removeItemDecoration() {
        if (itemDecoration != null) {
            rvList.removeItemDecoration(itemDecoration);
        }
    }

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, Grid1Activity.class);
        context.startActivity(intent);
    }
}
