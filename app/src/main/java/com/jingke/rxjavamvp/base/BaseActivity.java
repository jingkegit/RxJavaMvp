package com.jingke.rxjavamvp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jingke.rxjavamvp.listener.LifeCycleListener;
import com.trello.rxlifecycle2.components.RxActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Activity
 * Created by jingke on 2018/2/6.
 */

public abstract class BaseActivity extends RxActivity {

    protected Unbinder unBinder;
    protected Context mContext;
    public LifeCycleListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        setContentView(getLayoutId());
        mContext = this;
        unBinder = ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mListener != null) {
            mListener.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }

}
