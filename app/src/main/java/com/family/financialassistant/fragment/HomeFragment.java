package com.family.financialassistant.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.activity.AddMonthBudgetActivity;
import com.family.financialassistant.activity.AddRecordActivity;
import com.family.financialassistant.activity.MainActivity;
import com.family.financialassistant.base.BaseFragment;
import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.databinding.FragmentHomeBinding;
import com.family.financialassistant.db.MonthBudgetApi;
import com.family.financialassistant.db.RecordApi;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 首页
 **/
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {


    public static final int REQUEST_CODE = 0;
    private AlertDialog mAlertDialog;
    private int mIndex = 0;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void processLogic() {
        initTitleBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        bindView.rcvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void initTitleBar() {
        bindView.setOnclick(this);
        bindView.titleBar.setRightImageId(R.drawable.ic_add);
        bindView.titleBar.setLeftImageId(R.drawable.titlebar_menu);
        bindView.titleBar.setRightImageClick(this);
        bindView.titleBar.setLeftImageClick(this);
        bindView.llSelectTime.setOnClickListener(this);
    }

    public void showSingleAlertDialog(){
        final String[] items = {"添加月预算", "添加记账记录"};
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


    public static Fragment getInstance() {
        return new HomeFragment();
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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bindView.tvTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                //获取数据库数据
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
