package com.jingke.rxjavamvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jingke.rxjavamvp.R;
import com.jingke.rxjavamvp.base.BaseActivity;
import com.jingke.rxjavamvp.bean.PhoneMsgBean;
import com.jingke.rxjavamvp.presenter.PhoneAddressPresenter;
import com.jingke.rxjavamvp.utils.ToastUtils;
import com.jingke.rxjavamvp.view.interf.IPhoneAddressView;
import com.jingke.rxjavamvp.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class PhoneAddressActivity extends BaseActivity implements IPhoneAddressView {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    private PhoneAddressPresenter mPhonePst = new PhoneAddressPresenter(this, this);
    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        loadingDialog = new LoadingDialog(mContext,true);
    }

    @OnClick({R.id.submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
        }
    }

    private void submit() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "输入想要查询的手机号", Toast.LENGTH_SHORT).show();
        }
        mPhonePst.getInfo(phone);
    }

    @Override
    public void showResult(PhoneMsgBean bean) {
        if (bean == null) {
            return;
        }
        tvPhone.setText(bean.getMobileNumber());
        tvCity.setText(bean.getCity());
        tvProvince.setText(bean.getProvince());
        tvOperator.setText(bean.getOperator());
    }

    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        loadingDialog.cancel();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(mContext, msg);
    }
}
