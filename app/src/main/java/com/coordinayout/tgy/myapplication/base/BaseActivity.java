package com.coordinayout.tgy.myapplication.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by tgy on 2017/2/26.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);
//        ButterKnife.bind(this);
//    }
//
//    @Override
//    public void setContentView(View view) {
//        super.setContentView(view);
//        ButterKnife.bind(this);
//    }
}
