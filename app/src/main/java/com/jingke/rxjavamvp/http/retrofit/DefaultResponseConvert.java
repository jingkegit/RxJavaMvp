package com.jingke.rxjavamvp.http.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.jingke.rxjavamvp.http.exception.ApiCode;
import com.jingke.rxjavamvp.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 *  Default Response
 * Created by jingke on 2017/6/20.
 */
public class DefaultResponseConvert<T> implements Converter<ResponseBody,T> {

    private final Gson gson;
    private final Type type;
    private final TypeAdapter<T> adapter;

    public DefaultResponseConvert(Gson gson, Type type, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.type = type;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("retCode");
            String message = jsonObject.getString("msg");
            String data = jsonObject.getString("result");
            return dealCode(code,message,data);
        } catch (JSONException e) {
            throw new JsonParseException("Response body parse fail,maybe response body is not json format");
        }
    }

    private T dealCode(int code, String message, String data)throws IOException {
        Boolean isBaseType = isBaseType();
        if (code == ApiCode.CODE_SUCCESS){
            if (isBaseType){
                return (T) dealBaseType(data);
            }else {
                JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes("UTF-8"))));
                return this.adapter.read(jsonReader);
            }
        }else {
            if (isBaseType){
                throw new ApiException(code,message,dealBaseType(data));
            }else {
                JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes("UTF-8"))));
                throw new ApiException(code,message,this.adapter.read(jsonReader));
            }
        }
    }

    private boolean isBaseType(){
        return this.type == String.class
            || type == Boolean.class
            || type == boolean.class
            || type == Byte.class
            || type == byte.class
            || type == Character.class
            || type == char.class
            || type == Double.class
            || type == double.class
            || type == Float.class
            || type == float.class
            || type == Integer.class
            || type == int.class
            || type == Long.class
            || type == long.class
            || type == Short.class
            || type == short.class;
    }

    private Object dealBaseType(String value)throws IOException {
        if (type == String.class) {
            return value;
        }
        if (type == Boolean.class || type == boolean.class) {
            return Boolean.valueOf(value);
        }
        if (type == Byte.class || type == byte.class) {
            return Byte.valueOf(value);
        }
        if (type == Character.class || type == char.class) {
            if (value.length() != 1) {
                throw new IOException(
                    "Expected body of length 1 for Character conversion but was " + value.length());
            }
            return value.charAt(0);
        }
        if (type == Double.class || type == double.class) {
            return Double.valueOf(value);
        }
        if (type == Float.class || type == float.class) {
            return Float.valueOf(value);
        }
        if (type == Integer.class || type == int.class) {
            return Integer.valueOf(value);
        }
        if (type == Long.class || type == long.class) {
            return Long.valueOf(value);
        }
        if (type == Short.class || type == short.class) {
            return Short.valueOf(value);
        }
        return null;
    }
}
