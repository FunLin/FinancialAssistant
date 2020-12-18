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
import com.family.financialassistant.db.UserApi;
import com.family.financialassistant.util.SPUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by lsg on 2020-12-10
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    public static final int REQUEST_CODE = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
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
            int account = Integer.parseInt(getStringByUI(bindView.etAccountNum));
            int password = Integer.parseInt(getStringByUI(bindView.etPsd));
            boolean isLoginSuccess = false;
            //数据库获取所有用户信息 与用户输入内容进行比对
            List<UserBean> allUserList = UserApi.getInstance().getAllUserList();
            if(allUserList != null &&allUserList.size() > 0){
                for (int i = 0; i < allUserList.size(); i++) {
                    UserBean userBean = allUserList.get(i);
                    if(account == userBean.getAccount() && password == userBean.getPassword()){
                        isLoginSuccess = true;
                        break;
                    }else {
                        isLoginSuccess = false;
                    }
                }
                //比对成功跳转首页并将登陆页关闭
                if(isLoginSuccess){
                    ToastUtils.showShort("登录成功");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
                    finish();
                }else {
                    ToastUtils.showShort("请检查您输入的账号密码是否正确");
                }
            }else {
                ToastUtils.showShort("请先去注册");
            }
        }else if(id == R.id.tv_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        }
    }

    /**
     * 处理注册页面的回传数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String user_account = data != null ? data.getStringExtra("user_account") : "";
        String user_psd = data != null ? data.getStringExtra("user_psd") : "";
        bindView.etAccountNum.setText(user_account);
        bindView.etPsd.setText(user_psd);
    }
}
