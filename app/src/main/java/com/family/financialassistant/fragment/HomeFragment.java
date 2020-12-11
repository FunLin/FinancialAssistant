package com.family.financialassistant.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.family.financialassistant.R;
import com.family.financialassistant.activity.AddDepositActivity;
import com.family.financialassistant.activity.AddMonthBudgetActivity;
import com.family.financialassistant.activity.AddRecordActivity;
import com.family.financialassistant.activity.MainActivity;
import com.family.financialassistant.adapter.RecordAdapter;
import com.family.financialassistant.base.BaseFragment;
import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.FragmentHomeBinding;
import com.family.financialassistant.db.MonthBudgetApi;
import com.family.financialassistant.db.RecordApi;
import com.family.financialassistant.manager.BroadcastReceiverManager;
import com.family.financialassistant.manager.NotificationManager;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 首页
 **/
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    public static final int REQUEST_CODE = 0;
    private AlertDialog mAlertDialog;
    private int mIndex = 0;
    private Monthbudget mMonthBudgetByTime;//预算实体类对象
    //首页状态广播监听
    private StateReceiver mStateReceiver;
    private int mYear = 0;
    private int mMonth = 0;
    private RecordAdapter mRecordAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public static Fragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void processLogic() {
        initTitleBar();
        initRecyclerView();
        initRefresh();
        register();
    }


    private void initRecyclerView() {
        mRecordAdapter = new RecordAdapter(new ArrayList<Record>());
        bindView.rcvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mRecordAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(bindView.rcvRecord);
        // 开启滑动删除
        mRecordAdapter.enableSwipeItem();
        mRecordAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                List<Record> data = mRecordAdapter.getData();
                if(data.size() > 0){
                    String cTime = data.get(i).getcTime();
                    RecordApi.getInstance().recordDelete(cTime);
                    mRecordAdapter.getData().remove(i);
                    mRecordAdapter.notifyDataSetChanged();
                    BroadcastReceiverManager.sendReceiver(getActivity(), Constant.ACTION_CLEAR_MONTH_BUDGET);
                    ToastUtils.showShort("删除成功");
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
            }
        });
        bindView.rcvRecord.setAdapter(mRecordAdapter);
    }

    /**
     * 注册广播监听
     */
    private void register() {
        mStateReceiver = new StateReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(Constant.ACTION_ADD_MONTH_BUDGET);
        itFilter.addAction(Constant.ACTION_ADD_RECORD);
        itFilter.addAction(Constant.ACTION_CLEAR_MONTH_BUDGET);
        itFilter.addAction(Constant.ACTION_CLEAR_RECORD);
        Objects.requireNonNull(getActivity()).registerReceiver(mStateReceiver, itFilter);

    }

    /**
     * 刷新功能处理
     */
    private void initRefresh() {
        bindView.srlRefresh.setEnableLoadMore(false);
        bindView.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(!getStringByUI(bindView.tvTime).isEmpty()){
                    getRecordData();
                }else {
                    ToastUtils.showShort("请先选择时间");
                }
                bindView.srlRefresh.finishRefresh();
            }
        });
    }

    private void initTitleBar() {
        bindView.setOnclick(this);
        bindView.titleBar.setRightImageId(R.drawable.ic_add);
        bindView.titleBar.setLeftImageId(R.drawable.titlebar_menu);
        bindView.titleBar.setRightImageClick(this);
        bindView.titleBar.setLeftImageClick(this);
        bindView.llSelectTime.setOnClickListener(this);
    }


    /**
     * 获取月预算数据库数量
     * @return
     */
    private void getRecordData() {
        mRecordAdapter.getData().clear();
        boolean isHasData = false;
        double allInCome = 0.0;//总收入
        double allExpenses = 0.0;//总支出
        List<Record> recordList = RecordApi.getInstance().
                getAllRecordList();
        if(recordList != null && recordList.size() > 0){
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                if(!TextUtils.isEmpty(getStringByUI(bindView.tvTime))){
                    if(record.getTime().startsWith(getStringByUI(bindView.tvTime))){
                        mRecordAdapter.addData(record);
                        LogUtils.e(record.getItemType());
                        allInCome += record.getIncome();
                        allExpenses += record.getExpenses();
                        isHasData = true;
                    }
                }
            }
            if(!isHasData){
                bindView.tvIncome.setText("0.00");
                bindView.tvExpenses.setText("0.00");
            }else {
                bindView.tvIncome.setText(String.valueOf(allInCome));
                bindView.tvExpenses.setText(String.valueOf(allExpenses));
                //如果支出超过收入和预算的总和 那就通知用户超过预算
                if(allExpenses > (mMonthBudgetByTime.getBudget() + allInCome)){
                    sendNotification();
                }
            }
        }else {
            bindView.tvIncome.setText("0.00");
            bindView.tvExpenses.setText("0.00");
        }
        mRecordAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgRight:
                showSingleAlertDialog();
                break;
            case R.id.imgLeft:
                MainActivity mainActivity = (MainActivity)getActivity();
                if(mainActivity != null){
                    mainActivity.openDrawer();
                }
                break;
            case R.id.ll_select_time:
                showDatePickerDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 发送广播 / 支出已超预算提示用户
     */
    private void sendNotification() {
        NotificationManager.setActivity(getActivity());
        NotificationManager.startNotificationManager("本月支出已超预算！！！",R.drawable.logo);
    }

    /**
     * 选择时间 选择完毕之后获取月预算 在月预算有的前提下去获取当月记账记录
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = (monthOfYear + 1);
                bindView.tvTime.setText(year + "-" + (monthOfYear + 1));
                //获取数据库数据
                getMonthBudgetData(year,(monthOfYear + 1));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * 获取月预算数据
     * @param year
     * @param monthOfYear 月份是从0开始 所以+1
     */
    private void getMonthBudgetData(int year, int monthOfYear) {
        mMonthBudgetByTime = MonthBudgetApi.getInstance().getMonthBudgetByTime(year + "-" + monthOfYear);
        //mMonthBudgetByTime不可能为空 所以校验时间就可以
        if(!mMonthBudgetByTime.getTime().isEmpty()){
            bindView.tvBudget.setText(String.valueOf(mMonthBudgetByTime.getBudget()));
            getRecordData();
        }else {
            bindView.tvBudget.setText("0.00");
            bindView.tvIncome.setText("0.00");
            bindView.tvExpenses.setText("0.00");
            getRecordData();
        }
    }

    /**
     * 新增月预算/记账记录
     */
    public void showSingleAlertDialog(){
        mIndex = 0;
        final String[] items = {"添加月预算", "添加记账记录", "添加存款"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择添加项");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mIndex = i;
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mIndex == 0){
                    Intent intent = new Intent(getActivity(), AddMonthBudgetActivity.class);
                    ActivityUtils.startActivityForResult(Objects.requireNonNull(getActivity()),intent, REQUEST_CODE);
                }else if(mIndex == 1){
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    ActivityUtils.startActivityForResult(Objects.requireNonNull(getActivity()),intent, REQUEST_CODE);
                }else if(mIndex == 2){
                    Intent intent = new Intent(getActivity(), AddDepositActivity.class);
                    ActivityUtils.startActivityForResult(Objects.requireNonNull(getActivity()),intent, REQUEST_CODE);
                }
                mAlertDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog = alertBuilder.create();
        mAlertDialog.show();
    }

    public class StateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_CLEAR_MONTH_BUDGET)){
                //清空月预算
                refreshMonthAndRecordData();
            }else if (intent.getAction().equals(Constant.ACTION_CLEAR_RECORD)){
                //清空记账记录
                refreshMonthAndRecordData();
            }else if (intent.getAction().equals(Constant.ACTION_ADD_RECORD)){
                //添加记账记录
                refreshMonthAndRecordData();
            }else if (intent.getAction().equals(Constant.ACTION_ADD_MONTH_BUDGET)){
                //添加月预算
                refreshMonthAndRecordData();
            }
        }
    }

    /**
     * 收到广播后处理操作
     */
    public void refreshMonthAndRecordData() {
        getMonthBudgetData(mYear,mMonth);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mStateReceiver);
    }
}
