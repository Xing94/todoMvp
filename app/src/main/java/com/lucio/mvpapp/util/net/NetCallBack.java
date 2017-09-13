package com.lucio.mvpapp.util.net;


import com.lucio.mvpapp.R;
import com.lucio.mvpapp.base.BaseBean;
import com.lucio.mvpapp.base.BaseView;
import com.lucio.mvpapp.util.LogUtil;

/**
 * 网络请求回调函数
 */
public abstract class NetCallBack<T> {

    abstract public void onSuccess(T bean);

    public void onFailed(BaseView mView, BaseBean bean) {
        if (bean.getReMsg() != null) {
            if (!bean.getReMsg().isEmpty()) {
                mView.showToast(bean.getReMsg());
            } else {
                mView.showToast(R.string.request_fail);
            }
        } else {
            mView.showToast(R.string.request_fail);
        }
    }

    public void onError(BaseView mView, Throwable e) {
        mView.showToast(R.string.network_unavailable);
        LogUtil.out("onError");
        e.printStackTrace();

    }

    public void onCompleted() {
        LogUtil.out("onCompleted");
    }

}
