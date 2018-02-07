package com.jingke.rxjavamvp.bean;

/**
 * Created by jingke
 */

public class PhoneMsgBean {

    /**
     * city : 太原市
     * cityCode : 0351
     * mobileNumber : 1823403
     * operator : 移动182卡
     * province : 山西
     * zipCode : 030000
     */

    private String city;
    private String cityCode;
    private String mobileNumber;
    private String operator;
    private String province;
    private String zipCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
