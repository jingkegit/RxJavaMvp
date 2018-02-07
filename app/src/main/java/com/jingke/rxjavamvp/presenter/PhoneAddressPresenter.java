package com.jingke.rxjavamvp.presenter;

import com.jingke.rxjavamvp.api.ApiUtils;
import com.jingke.rxjavamvp.base.BasePresenter;
import com.jingke.rxjavamvp.bean.PhoneMsgBean;
import com.jingke.rxjavamvp.http.exception.ApiException;
import com.jingke.rxjavamvp.http.observer.HttpRxObservable;
import com.jingke.rxjavamvp.http.observer.HttpRxObserver;
import com.jingke.rxjavamvp.utils.LogUtils;
import com.jingke.rxjavamvp.view.activity.PhoneAddressActivity;
import com.jingke.rxjavamvp.view.interf.IPhoneAddressView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 手机号归属地Presenter
 *
 * Created by jingke
 */

public class PhoneAddressPresenter extends BasePresenter<IPhoneAddressView,PhoneAddressActivity> {

    private final String TAG = PhoneAddressPresenter.class.getSimpleName();

    public PhoneAddressPresenter(IPhoneAddressView view, PhoneAddressActivity activity) {
        super(view, activity);
    }

    /**
     * 获取信息
     *
     */
    public void getInfo(String phone) {
        //构建请求数据
        Map<String, Object> request = new HashMap<>();
        request.put("key","1889b37351288");//测试key 可到Mob官网申请，有很多免费接口
        request.put("phone", phone);
        HttpRxObserver httpRxObserver = new HttpRxObserver<PhoneMsgBean>(TAG + "getInfo") {

            @Override
            protected void onStart(Disposable d) {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            protected void onError(ApiException e) {
                LogUtils.w("onError code:" + e.getCode() + " msg:" + e.getMessage());
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(e.getMessage());
                }
            }

            @Override
            protected void onSuccess(PhoneMsgBean bean) {
                if (getView() != null && bean!=null) {
                    getView().closeLoading();
                    getView().showResult(bean);
                }
            }
        };

        HttpRxObservable.getObservable(ApiUtils.getPhoneApi().phoneQuery(request), getActivity()).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });

        //取消请求
//        if(!httpRxObserver.isDisposed()){
//            httpRxObserver.cancel();
//        }

    }


}
