package com.jingke.rxjavamvp.listener;

import android.os.Bundle;

/**
 * 对生命周期监听
 * Created by jingke on 2018/2/6.
 */

public interface LifeCycleListener {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}
