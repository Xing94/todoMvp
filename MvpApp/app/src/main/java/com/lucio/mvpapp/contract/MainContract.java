package com.lucio.mvpapp.contract;


import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;

import com.lucio.mvpapp.base.BasePresenter;
import com.lucio.mvpapp.base.BaseView;

/**
 * 契约接口：签订view与presenter之间的关系
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void selectFragment(int position);

        void selectRadio(int position);
    }

    interface Presenter extends BasePresenter {

        void netRequest();

        void selectFragment(int radioId);

        Drawable getRadioDrawable(Drawable drawable);

        ViewPager.OnPageChangeListener vpChangeListener();
    }

}
