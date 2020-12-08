package com.family.financialassistant.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.blankj.utilcode.util.KeyboardUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.databinding.ActivityAddRecordBinding;

import java.util.Calendar;
import java.util.Objects;

/**
 * @Time : 2020/12/08
 * @Author : lsg
 * @Description : 添加记账记录
 **/
public class AddRecordActivity extends BaseActivity<ActivityAddRecordBinding> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    public void processLogic() {
        bindView.titleBar.setRightImageId(R.drawable.ic_date);
        bindView.titleBar.setRightImageClick(this);
        bindView.titleBar.setLeftImageClick(this);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                bindView.tvTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                //获取数据库数据
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgRight:
                showDatePickerDialog();
                break;
            case R.id.imgLeft:
            if (KeyboardUtils.isSoftInputVisible(this)){
                KeyboardUtils.hideSoftInput(this);
            }
                finish();
            break;
            default:
                break;
        }
    }
}
