package com.family.financialassistant.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;


import com.family.financialassistant.R;
import com.family.financialassistant.util.StatusBarUtil;
import com.family.financialassistant.widget.CustomProgress;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by lsg on 2020/12/08
 */
public abstract class BaseActivity<VDB extends ViewDataBinding>
        extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    protected VDB bindView;
    private Activity activity;
    private CustomProgress dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        bindView = DataBindingUtil.setContentView(this,getLayoutId());
        bindView.setLifecycleOwner(this);
        initStatusBar();
        processLogic();
        setListener();
    }


    /**
     * 设置透明状态栏,兼容4.4
     */
    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary),0);
    }


    public void showLoading(){
        if (dialog == null) {
            dialog = CustomProgress.show(activity, "正在加载中...", true, null);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideLoading(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public Activity getAtContext() {
        return activity;
    }

    /**
     * 处理页面监听事件
     */
    protected void setListener(){

    }

    /**
     * 获取布局资源Id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 处理逻辑
     */
    public abstract void processLogic();

    /**
     * 禁止改变字体大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
