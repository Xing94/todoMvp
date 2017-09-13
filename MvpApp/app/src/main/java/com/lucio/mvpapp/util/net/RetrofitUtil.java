package com.lucio.mvpapp.util.net;

import com.lucio.mvpapp.AppConstant;
import com.lucio.mvpapp.R;
import com.lucio.mvpapp.MvpApp;
import com.lucio.mvpapp.base.BaseBean;
import com.lucio.mvpapp.base.BaseView;
import com.lucio.mvpapp.data.source.remote.MainService;
import com.lucio.mvpapp.util.LogUtil;
import com.lucio.mvpapp.util.retrofit.gson.GsonConverterFactory;
import com.lucio.mvpapp.util.retrofit.rxjava2.RxJava2CallAdapterFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Retrofit工具类
 */
public class RetrofitUtil {

    private static Retrofit retrofit;

    public static void setBaseUrl(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(MvpApp.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstant.getHost())
                    .client(MvpApp.getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static MainService getMainService() {
        return RetrofitUtil.getInstance().create(MainService.class);
    }

    /**
     * 请求方法：只能运行在子线程中，不会阻塞UI线程
     *
     * @param observable 观察者
     * @param callback   已封装的请求回调
     * @param <T>        数据模型
     */
    public static <T> void request(BaseView mView, Observable<T> observable, final NetCallBack<T> callback) {

        new Thread() {
            @Override
            public void run() {
                observable.subscribeOn(Schedulers.io());
                observable.unsubscribeOn(Schedulers.io());
                observable.subscribe(t -> {
                            System.out.println("onNext");
                            BaseBean bean = null;
                            if (t instanceof ResponseBody) {
                                ResponseBody responseBody = (ResponseBody) t;
                                try {
                                    bean = GsonJson(responseBody.string(), BaseBean.class);
                                    LogUtil.i("RetrofitUtil Json", responseBody.string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    bean = (BaseBean) t;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (bean != null) {
                                String code = bean.getReCode();
                                if ("0".equals(code.trim())) {
                                    callback.onSuccess(t);
                                } else {
                                    callback.onFailed(mView, bean);
                                }
                            } else {
                                mView.showToast(R.string.request_error);
                            }
                        }, throwable -> callback.onError(mView, throwable),
                        callback::onCompleted);
            }
        }.start();
    }


//    public static <T> void requestConsumer(Observable<T> observable, final NetCallBack<T> callback) {
//
//        observable.subscribeOn(Schedulers.io());
////        observable.unsubscribeOn(Schedulers.io());
//        observable.observeOn(AndroidSchedulers.mainThread());
//        observable.subscribe(t -> {
//            System.out.println("onNext");
//            BaseBean bean = null;
//            if (t instanceof ResponseBody) {
//                ResponseBody responseBody = (ResponseBody) t;
//                try {
//                    bean = GsonJson(responseBody.string(), BaseBean.class);
//                    LogUtil.i("RetrofitUtil Json", responseBody.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    bean = (BaseBean) t;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bean != null) {
//                String code = bean.getReCode();
//                if ("0".equals(code.trim())) {
//                    callback.onSuccess(t);
//                } else {
//                    callback.onFailed(bean);
//                }
//            } else {
//                ToastUtil.show(R.string.request_error);
//            }
//        }, throwable -> {
//            callback.onError(throwable);
//        }, () -> {
//            callback.onCompleted();
//        });
//    }


    //控制下拉列表的刷新事件
//    public static <T> void request(final TwinklingRefreshLayout refreshLayout,
//                                   Observable<T> observable, final CallBackIng<T> callback) {
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<T>() {
//                    @Override
//                    public void onCompleted() {
//                        LoadingUtil.dismissLoadingDialog();
//                        refreshLayout.finishRefreshing();
//                        refreshLayout.finishLoadmore();
//                        callback.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LoadingUtil.dismissLoadingDialog();
//                        refreshLayout.finishRefreshing();
//                        refreshLayout.finishLoadmore();
//                        callback.onError(e);
//                    }
//
//                    @Override
//                    public void onNext(T t) {
//                        LoadingUtil.dismissLoadingDialog();
//                        refreshLayout.finishRefreshing();
//                        refreshLayout.finishLoadmore();
//                        BaseBean bean = null;
//                        if (t instanceof ResponseBody) {
//                            ResponseBody responseBody = (ResponseBody) t;
//                            try {
//                                bean = GsonJson(responseBody.string(), BaseBean.class);
//                                LogIngUtil.i("RetrofitUtil Json", responseBody.string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            try {
//                                bean = (BaseBean) t;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (bean != null) {
//                            String code = bean.getCode();
//                            if ("0".equals(code.trim()) && bean.isIsSuccess()) {
//                                callback.onSuccess(t);
//                            } else {
//                                callback.onFailed(bean);
//                            }
//                        } else {
//                            ToastUtil.show(R.string.request_error);
//                        }
//                    }
//                });
//    }

    //转换为实体类
    public static <T> T GsonJson(String json, Class<T> type) {
        LogUtil.i("RetrofitUtil Json", json);
        try {
            Gson gson = new Gson();
            T obj = gson.fromJson(json, type);
            return obj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //创建一个新的retrofit对象
    public static Retrofit createRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        return new Retrofit.Builder().baseUrl(baseUrl).client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
