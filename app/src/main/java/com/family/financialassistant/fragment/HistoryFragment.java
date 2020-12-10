package com.family.financialassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.family.financialassistant.R;
import com.family.financialassistant.adapter.HistoryAdapter;
import com.family.financialassistant.base.BaseFragment;
import com.family.financialassistant.bean.HistoryBean;
import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.FragmentHistoryBinding;
import com.family.financialassistant.db.MonthBudgetApi;
import com.family.financialassistant.db.RecordApi;
import com.family.financialassistant.manager.BroadcastReceiverManager;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 历史操作记录
 **/
public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {

    private HistoryAdapter mHistoryAdapter;
    public static HistoryFragment getInstance(){
        return new HistoryFragment();
    }
    //首页状态广播监听
    private HistoryReceiver mHistoryReceiver;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void processLogic() {
        initRecyclerView();
        initData();
        initRefresh();
        register();
    }

    /**
     * 注册广播监听
     */
    private void register() {
        mHistoryReceiver = new HistoryReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(Constant.ACTION_ADD_MONTH_BUDGET);
        itFilter.addAction(Constant.ACTION_ADD_RECORD);
        itFilter.addAction(Constant.ACTION_CLEAR_MONTH_BUDGET);
        itFilter.addAction(Constant.ACTION_CLEAR_RECORD);
        itFilter.addAction(Constant.ACTION_CLEAR_RECORD_ITEM);
        Objects.requireNonNull(getActivity()).registerReceiver(mHistoryReceiver, itFilter);
    }

    private void initData() {
        getMonthBudgetListSize();
        getRecordListSize();
    }


    private void initRecyclerView() {
        mHistoryAdapter = new HistoryAdapter(new ArrayList<HistoryBean>());
        bindView.rcvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mHistoryAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(bindView.rcvRecord);
        // 开启滑动删除
        mHistoryAdapter.enableSwipeItem();
        mHistoryAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                List<HistoryBean> data = mHistoryAdapter.getData();
                if(data.size() > 0){
                    String cTime = data.get(i).getcTime();
                    if(data.get(i).getOperateType() == 0){
                        //删除预算Item
                        MonthBudgetApi.getInstance().budgetDelete(cTime);
                    }else {
                        RecordApi.getInstance().recordDelete(cTime);
                    }
                    mHistoryAdapter.getData().remove(i);
                    mHistoryAdapter.notifyDataSetChanged();
                    BroadcastReceiverManager.sendReceiver(getActivity(), Constant.ACTION_CLEAR_MONTH_BUDGET);
                    ToastUtils.showShort("删除成功");
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
            }
        });
        bindView.rcvRecord.setAdapter(mHistoryAdapter);
    }

    private void initRefresh() {
        bindView.srlRefresh.setEnableLoadMore(false);
        bindView.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHistoryAdapter.getData().clear();
                initData();
                bindView.srlRefresh.finishRefresh();
            }
        });
    }

    /**
     * 获取月预算数据库数量
     * @return
     */
    private void getMonthBudgetListSize() {
        List<Monthbudget> allMonthBudgetList = MonthBudgetApi.getInstance().
                getAllMonthBudgetList();
        if(allMonthBudgetList != null && allMonthBudgetList.size() > 0){
            for (int i = 0; i < allMonthBudgetList.size(); i++) {
                Monthbudget monthbudget = allMonthBudgetList.get(i);
                mHistoryAdapter.addData(new HistoryBean(0,0, monthbudget.getTime(),
                        monthbudget.getBudget(),"", monthbudget.getcTime()));
            }
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取月预算数据库数量
     * @return
     */
    private void getRecordListSize() {
        List<Record> recordList = RecordApi.getInstance().
                getAllRecordList();
        if(recordList != null && recordList.size() > 0){
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                if(record.getType() == 1){
                    mHistoryAdapter.addData(new HistoryBean(record.getType(),1, record.getTime(),
                            record.getIncome(), record.getDesc(), record.getcTime()));
                }else {
                    mHistoryAdapter.addData(new HistoryBean(record.getType(),1, record.getTime(),
                            record.getExpenses(), record.getDesc(), record.getcTime()));
                }
            }
        }
        mHistoryAdapter.notifyDataSetChanged();
    }

    public class HistoryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.ACTION_CLEAR_MONTH_BUDGET)){
                //清空月预算
                mHistoryAdapter.getData().clear();
                initData();
            }else if (intent.getAction().equals(Constant.ACTION_CLEAR_RECORD)){
                //清空记账记录
                mHistoryAdapter.getData().clear();
                initData();
            }else if (intent.getAction().equals(Constant.ACTION_ADD_RECORD)){
                //添加记账记录
                mHistoryAdapter.getData().clear();
                initData();
            }else if (intent.getAction().equals(Constant.ACTION_ADD_MONTH_BUDGET)){
                //添加月预算
                mHistoryAdapter.getData().clear();
                initData();
            }else if (intent.getAction().equals(Constant.ACTION_CLEAR_RECORD_ITEM)){
                //删除记账记录
                mHistoryAdapter.getData().clear();
                initData();

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mHistoryReceiver);
    }
}
