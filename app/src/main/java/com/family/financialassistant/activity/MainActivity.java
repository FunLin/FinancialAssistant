package com.family.financialassistant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.adapter.MyFragmentPageAdapter;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityMainBinding;
import com.family.financialassistant.db.DBManager;
import com.family.financialassistant.db.MonthBudgetApi;
import com.family.financialassistant.db.RecordApi;
import com.family.financialassistant.listener.MyPageListener;
import com.family.financialassistant.manager.BroadcastReceiverManager;
import com.family.financialassistant.manager.MusicServiceManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 首页
 **/
public class MainActivity extends BaseActivity<ActivityMainBinding> implements
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {



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

    /**
     * 处理Viewpager与BottomTab的联动关系
     * @param position
     */
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
                startOrStopMusicService();
                break;
            case R.id.nav_video:
                ActivityUtils.startActivity(VideoActivity.class);
                break;
            case R.id.nav_net_data:
                ActivityUtils.startActivity(NetDataActivity.class);
                break;
            case R.id.nav_clear_budget:
                //清除月预算
                showDeleteDialog(0,getMonthBudetListSize());
                break;
            case R.id.nav_clear_record:
                //清除记账记录
                showDeleteDialog(1,getRecordListSize());
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

    /**
     * 获取月预算数据库数量
     * @return
     */
    private int getMonthBudetListSize() {
        int size = 0;
        List<Monthbudget> allMonthBudgetList = MonthBudgetApi.getInstance().
                getAllMonthBudgetList();
        if(allMonthBudgetList != null && allMonthBudgetList.size() > 0){
            size = allMonthBudgetList.size();
        }
        return size;
    }

    /**
     * 获取记账记录数据库数量
     * @return
     */
    private int getRecordListSize() {
        int size = 0;
        List<Record> recordList = RecordApi.getInstance().
                getAllRecordList();
        if(recordList != null && recordList.size() > 0){
            size = recordList.size();
        }
        return size;
    }

    /**
     * 清除数据库数据
     * @param type 0清空月预算 1清空记账记录
     * @param dataSize
     */
    private void showDeleteDialog(final int type, int dataSize){
        String title;
        if(type == 0){
            title = "清空月预算";
        }else {
            title = "清空记账记录";
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("目前有" + dataSize + "条数据,确定要清空吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type == 0){
                            //数据库清除
                            DBManager.getInstance().clearMonthBudgetTable();
                            //广播发送 用于首页数据刷新
                            BroadcastReceiverManager.sendReceiver(MainActivity.this,Constant.ACTION_CLEAR_MONTH_BUDGET);
                            ToastUtils.showShort("清除成功");
                        }else {
                            DBManager.getInstance().clearRecordTable();
                            BroadcastReceiverManager.sendReceiver(MainActivity.this,Constant.ACTION_CLEAR_RECORD);
                            ToastUtils.showShort("清除成功");
                        }
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * 启动/停止音乐服务
     */
    private void startOrStopMusicService(){
        final Intent intent = new Intent(MainActivity.this, MusicServiceManager.class);
            //启动和停止MusicService
        if (MusicServiceManager.isplay == false){
            startService(intent);//启动service
        }else {
            stopService(intent);//停止service
        }
    }

}
