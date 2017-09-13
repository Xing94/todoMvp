package com.lucio.mvpapp.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.lucio.mvpapp.R;
import com.lucio.mvpapp.base.BaseFragment;

public class OneFragment extends BaseFragment {


    public static OneFragment newInstance() {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int onCreateView() {
        return R.layout.fragment_one;
    }

    @Override
    public void initView(View contentView) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

}
