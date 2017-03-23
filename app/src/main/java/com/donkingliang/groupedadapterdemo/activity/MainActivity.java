package com.donkingliang.groupedadapterdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.donkingliang.groupedadapterdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_group_list).setOnClickListener(this);
        findViewById(R.id.btn_no_header).setOnClickListener(this);
        findViewById(R.id.btn_no_footer).setOnClickListener(this);
        findViewById(R.id.btn_grid_1).setOnClickListener(this);
        findViewById(R.id.btn_grid_2).setOnClickListener(this);
        findViewById(R.id.btn_expandable).setOnClickListener(this);
        findViewById(R.id.btn_expandable).setOnClickListener(this);
        findViewById(R.id.btn_various).setOnClickListener(this);
        findViewById(R.id.btn_various_child).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_group_list:
                GroupedListActivity.openActivity(this);
                break;

            case R.id.btn_no_header:
                NoHeaderActivity.openActivity(this);
                break;

            case R.id.btn_no_footer:
                NoFooterActivity.openActivity(this);
                break;

            case R.id.btn_grid_1:
                Grid1Activity.openActivity(this);
                break;

            case R.id.btn_grid_2:
                Grid2Activity.openActivity(this);
                break;

            case R.id.btn_expandable:
                ExpandableActivity.openActivity(this);
                break;

            case R.id.btn_various:
                VariousActivity.openActivity(this);
                break;

            case R.id.btn_various_child:
                VariousChildActivity.openActivity(this);
                break;
        }
    }
}
