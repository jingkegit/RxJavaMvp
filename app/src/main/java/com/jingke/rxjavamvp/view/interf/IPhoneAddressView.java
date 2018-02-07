package com.jingke.rxjavamvp.view.interf;

import com.jingke.rxjavamvp.base.IBaseView;
import com.jingke.rxjavamvp.bean.PhoneMsgBean;

/**
 * 手机归属地页面view接口
 *
 * Created by jingke
 */

public interface IPhoneAddressView extends IBaseView {

    //显示结果
    void showResult(PhoneMsgBean bean);

}
