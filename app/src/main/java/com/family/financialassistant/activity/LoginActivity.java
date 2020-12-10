package com.family.financialassistant.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityLoginBinding;
import com.family.financialassistant.util.SPUtils;
import com.family.financialassistant.util.StatusBarUtil;

/**
 * Created by lsg on 2020-12-10
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
    }

    @Override
    protected boolean isGetPermission() {
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_login){
            if(TextUtils.isEmpty(getStringByUI(bindView.etAccountNum))){
                ToastUtils.showShort("请输入帐号");
                return;
            }
            if(TextUtils.isEmpty(getStringByUI(bindView.etPsd))){
                ToastUtils.showShort("请输入密码");
                return;
            }

            if(bindView.rbRememberPsd.isChecked()){
                SPUtils.putBoolean(Constant.IS_REMEMBER,true);
            }
            ToastUtils.showShort("登录成功");

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
            finish();
        }
    }
}
