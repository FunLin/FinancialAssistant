package com.family.financialassistant.activity;

import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.adapter.MyFragmentPageAdapter;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.databinding.ActivityMainBinding;
import com.family.financialassistant.db.DBManager;
import com.family.financialassistant.db.RecordApi;
import com.family.financialassistant.listener.MyPageListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 首页
 **/
public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
        bindView.navView.setNavigationItemSelectedListener(this);
        initViewPager();
    }

    private void initViewPager() {
        MyFragmentPageAdapter fragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        bindView.vp.setAdapter(fragmentPageAdapter);
        //设置最大缓存页面
        bindView.vp.setOffscreenPageLimit(1);
        bindView.vp.setOnPageChangeListener(new MyPageListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setCurrentItem(0);
                        break;
                    case 1:
                        setCurrentItem(1);
                        break;
                    case 2:
                        setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
        setCurrentItem(0);
    }


    private void setCurrentItem(int position) {
        boolean isHome = false;
        boolean isHistory = false;
        boolean isMine = false;
        switch (position) {
            case 0:
                isHome = true;
                break;
            case 1:
                isHistory = true;
                break;
            case 2:
                isMine = true;
                break;
            default:
                isHome = true;
                break;
        }
        bindView.tvHome.setSelected(isHome);
        bindView.tvHistory.setSelected(isHistory);
        bindView.tvMine.setSelected(isMine);
    }

    /**
     * 供Fragment打开菜单
     */
    public void openDrawer(){
        bindView.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home:
                bindView.vp.setCurrentItem(0);
                break;
            case R.id.tv_history:
                bindView.vp.setCurrentItem(1);
                break;
            case R.id.tv_mine:
                bindView.vp.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_music:
                Record record = new Record();
                record.setTime("2020 - 12 - 08");
                boolean recordAdd = RecordApi.getInstance().recordAdd(record);
                LogUtils.e("记录 " + recordAdd);
                List<Record> allRecordList = RecordApi.getInstance().getAllRecordList();
                if(allRecordList != null && allRecordList.size() > 0){
                    ToastUtils.showShort(allRecordList.size());
                }
                break;
            case R.id.nav_clear_budget:
                DBManager.getInstance().clearMonthBudgetTable();
                ToastUtils.showShort("清除成功");
                break;
            case R.id.nav_clear_record:
                DBManager.getInstance().clearRecordTable();
                ToastUtils.showShort("清除成功");
                break;
            case R.id.nav_about:
                ActivityUtils.startActivity(AboutActivity.class);
                break;
            case R.id.nav_exit:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}