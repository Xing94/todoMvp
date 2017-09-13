package com.lucio.mvpapp.presenter;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import com.lucio.mvpapp.R;
import com.lucio.mvpapp.contract.MainContract;
import com.lucio.mvpapp.data.bean.LoginBean;
import com.lucio.mvpapp.util.LogUtil;
import com.lucio.mvpapp.util.net.NetCallBack;
import com.lucio.mvpapp.util.net.RetrofitUtil;

/**
 * 主页面
 * 为什么不用tabHost：tabHost每一次切换都是replace，影响加载速度
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void netRequest() {

        RetrofitUtil.request(mView, RetrofitUtil.getMainService().login("1311111111", "24cfeed0c1c1d9ed3414afc78d096b67"),
                new NetCallBack<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean bean) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = bean;
                        msg.what = 101;
                        mHandler.sendMessage(msg);
                        LogUtil.out(bean.toString());
                        mView.showToast("成功了");
                    }
                });
    }

    @Override
    public void selectFragment(int radioId) {
        switch (radioId) {
            case R.id.rb_one:
                mView.selectFragment(0);
                break;
            case R.id.rb_two:
                mView.selectFragment(1);
                break;
            case R.id.rb_three:
                mView.selectFragment(2);
                break;
            case R.id.rb_four:
                mView.selectFragment(3);
                break;
        }
    }

    @Override
    public Drawable getRadioDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, 50, 50);
        return drawable;
    }

    @Override
    public ViewPager.OnPageChangeListener vpChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mView.selectRadio(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    //子线程的视图操作
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    break;
            }
        }
    };
}
