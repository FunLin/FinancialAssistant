package com.family.financialassistant.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.databinding.ActivityAboutBinding;

/**
 * Created by lsg on 2020-12-08
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void processLogic() {
        bindView.titleBar.setLeftImageId(R.drawable.icon_back);
        bindView.titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bindView.tvVersionCode.setText("当前版本号v:" + getLocalVersion(this));
    }

    /**
     * 获取版本号
     * @param ctx
     * @return
     */
    public static String getLocalVersion(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext ()
                    .getPackageManager ()
                    .getPackageInfo (ctx.getPackageName (), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        return localVersion;
    }
}
