package com.jingke.rxjavamvp.http.exception;

/**
 *
 * Created by wangli on 2016/6/21.
 */
public interface ApiCode {
    int CODE_SUCCESS = 10000;
    int CODE_PLEASE_LOGIN = 10009;//token失效
    int CODE_UNLOGIN = 10010;//未登录
    int CODE_OTHER_ERROR = 20000;//未登录
    int UN_KNOWN_ERROR = 1000;
}
