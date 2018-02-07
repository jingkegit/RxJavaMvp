package com.jingke.rxjavamvp.api;

import com.jingke.rxjavamvp.http.retrofit.RetrofitUtils;

/**
 * 接口工具类
 * Created by jingke
 */

public class ApiUtils {

    private static PhoneApi phoneApi;

    public static PhoneApi getPhoneApi() {
        if (phoneApi == null) {
            phoneApi = RetrofitUtils.get().retrofit().create(PhoneApi.class);
        }
        return phoneApi;
    }
}
