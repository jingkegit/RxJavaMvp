package com.jingke.rxjavamvp.http.observer;

import com.google.gson.Gson;
import com.jingke.rxjavamvp.utils.LogUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Retrofit网络请求Observable(被观察者)
 * Created by jingke
 */
public class HttpRxObservable {

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 无管理生命周期,容易导致内存溢出
     */
    public static Observable getObservable(Observable apiObservable) {
       // showLog(request);
        Observable observable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider自动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity.../RxFragment...
     */
    public static Observable getObservable(Observable apiObservable, LifecycleProvider lifecycle) {
        //showLog(request);
        Observable observable;

        if (lifecycle != null) {
            //随生命周期自动管理.eg:onCreate(start)->onStop(end)
            observable =apiObservable
                    .subscribeOn(Schedulers.io())
                    .compose(lifecycle.bindToLifecycle())//随生命周期自动管理
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
     *
     */
    public static Observable getObservable(Observable apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
       // showLog(request);
        Observable observable;
        if (lifecycle != null) {
            observable = apiObservable
                    .subscribeOn(Schedulers.io())
                    .compose(lifecycle.bindUntilEvent(event))//手动管理移除监听生命周期 特定时期移除
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<FragmentEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxFragment,RxDialogFragment
     */
    public static Observable getObservable(Observable apiObservable, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event) {
      //  showLog(request);
        Observable observable;
        if (lifecycle != null) {
            observable = apiObservable
                    .compose(lifecycle.bindUntilEvent(event))//手动管理移除监听生命周期.eg:FragmentEvent.STOP
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

    private static void showLog(Map<String, Object> request) {
        if (request == null || request.size() == 0) {
            LogUtils.e("[http request]:");
        }
        LogUtils.e("[http request]:" + new Gson().toJson(request));
    }

}
