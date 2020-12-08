package com.family.financialassistant.activity;

import android.content.Intent;
import android.os.Bundle;

import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.databinding.ActivitySplashBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import me.wangyuwei.particleview.ParticleView;

/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 闪屏页
 **/
public class SplashActivity extends FragmentActivity {

    private ActivitySplashBinding mSplashBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mSplashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        mSplashBinding.pvLoading.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
                finish();
            }
        });
        mSplashBinding.pvLoading.startAnim();
    }

}
