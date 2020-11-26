package com.donkingliang.groupedadapterdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapterdemo.R;
import com.donkingliang.groupedadapterdemo.adapter.EmptyAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 没有数据时，显示空布局。
 */
public class EmptyActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private EmptyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        rvList = (RecyclerView) findViewById(R.id.rv_list);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmptyAdapter(this, null);
        // 显示空布局。默认不显示
        adapter.showEmptyView(true);
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter groupedAdapter, BaseViewHolder holder, int groupPosition) {
                // 清空数据
                adapter.clear();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter groupedAdapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
                Toast.makeText(EmptyActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });

        rvList.setAdapter(adapter);
    }

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, EmptyActivity.class);
        context.startActivity(intent);
    }
}
