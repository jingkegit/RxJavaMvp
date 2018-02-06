package com.jingke.rxjavamvp.http.exception;

import java.io.IOException;

/**
 * Api Exception
 * Created by wangli on 2016/6/20.
 */
public class ApiException extends IOException {

    private int code;
    private String message;
    private Object data;
    public ApiException(int code, String msg, Object data){
        super("code:"+code+"\r\n"+"message:"+msg+"\r\n"+"data:"+data);
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }
}
