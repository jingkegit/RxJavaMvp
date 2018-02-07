package com.jingke.rxjavamvp.api;

import com.jingke.rxjavamvp.bean.PhoneMsgBean;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by jingke
 */
public interface PhoneApi {

    @GET("v1/mobile/address/query")
    Observable<PhoneMsgBean> phoneQuery(@QueryMap Map<String, Object> request);

}
