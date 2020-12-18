package com.family.financialassistant.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.bean.UserBean;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityLoginBinding;
import com.family.financialassistant.databinding.ActivityRegisterBinding;
import com.family.financialassistant.db.UserApi;
import com.family.financialassistant.util.SPUtils;

/**
 * Created by lsg on 2020-12-10
 * 注册页面
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> implements View.OnClickListener {

    public static final int RESULT_CODE = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_register){
            if(TextUtils.isEmpty(getStringByUI(bindView.etAccountNum))){
                ToastUtils.showShort("请输入帐号");
                return;
            }
            if(TextUtils.isEmpty(getStringByUI(bindView.etPsd))){
                ToastUtils.showShort("请输入密码");
                return;
            }
            int account = Integer.parseInt(getStringByUI(bindView.etAccountNum));
            int password = Integer.parseInt(getStringByUI(bindView.etPsd));
            UserBean userBean = new UserBean(account,password);
            //数据库新增用户
            boolean userAdd = UserApi.getInstance().UserAdd(userBean);
            //用户注册成功数据回传登陆页 替代用户输入
            if(userAdd){
                ToastUtils.showShort("注册成功");
                Intent intent = new Intent();
                intent.putExtra("user_account",bindView.etAccountNum.getText().toString());
                intent.putExtra("user_psd",bindView.etPsd.getText().toString());
                setResult(RESULT_CODE,intent);
                finish();
            }else {
                ToastUtils.showShort("注册失败");
            }
        }
    }
}
