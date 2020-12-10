package com.family.financialassistant.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityAddMonthbudgetBinding;
import com.family.financialassistant.db.MonthBudgetApi;
import com.family.financialassistant.manager.BroadcastReceiverManager;
import com.family.financialassistant.util.AmountUtil;

import java.util.Calendar;

/**
 * @Time : 2020/12/08
 * @Author : lsg
 * @Description : 添加月预算
 **/
public class AddMonthBudgetActivity extends BaseActivity<ActivityAddMonthbudgetBinding> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_monthbudget;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
        bindView.titleBar.setRightImageId(R.drawable.ic_date);
        bindView.titleBar.setRightImageClick(this);
        bindView.titleBar.setLeftImageClick(this);
        initEditText();
        initSpinner();
    }

    /**
     * Spinner Item选中监听,输入预算后为用户计算最终预算
     */
    private void initSpinner() {
        bindView.spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String budgetText = bindView.etBudget.getText().toString();
                //预算金额输入后计算最终预算
                if(!budgetText.isEmpty()){
                    computeEndBudGet(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 预算输入框监听,不可输入多位小数,输入完毕计算最终预算
     * 不选择银行,默认使用工商银行
     */
    private void initEditText() {
        bindView.etBudget.addTextChangedListener(new TextWatcher() {
            private int selectionStart;
            private int selectionEnd;
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                temp = s;

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                selectionStart = bindView.etBudget.getSelectionStart();
                selectionEnd = bindView.etBudget.getSelectionEnd();

                if (!AmountUtil.isOnlyPointNumber(bindView.etBudget.getText().toString())){
                    ToastUtils.showShort("您输入的数字保留在小数点后两位");
                    //删除多余输入的字（不会显示出来）
                    try {
                        s.delete(selectionStart - 1, selectionEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                computeEndBudGet(0);
            }

        });
    }

    /**
     * 计算最终预算 用户未选择银行情况下默认算工商银行的
     */
    private void computeEndBudGet(int index){
        String budgetText = bindView.etBudget.getText().toString();
        if(TextUtils.isEmpty(budgetText)){
            ToastUtils.showShort("请输入金额");
            return;
        }
        Double budget = Double.valueOf(budgetText);//用户输入的预算值
        double endBudget = 0.0;//最终预算值

        if(index == 0 || index == 1 || index == 2 || index == 3 || index == 4){
            //利率都是1.3
            endBudget = budget * 1.3;
        }else if(index == 5 || index == 6 || index == 7){
            //利率1.4
            endBudget = budget * 1.4;
        }else{
            //利率1.5
            endBudget = budget * 1.5;
        }
        bindView.tvEndBudget.setText(String.valueOf(AmountUtil.formatDouble2(endBudget)));
    }



    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bindView.tvTime.setText(year + "-" + (monthOfYear + 1));
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
            case R.id.btn_submit:
                //提交前的校验
                if(getStringByUI(bindView.tvTime).isEmpty()){
                    ToastUtils.showShort("请选择时间");
                    return;
                }
                if(getStringByUI(bindView.etBudget).isEmpty()){
                    ToastUtils.showShort("请输入预算");
                    return;
                }
                double budget = Double.parseDouble(bindView.tvEndBudget.getText().toString());
                String time = bindView.tvTime.getText().toString();
                String bank = (String)bindView.spBank.getSelectedItem();
                Monthbudget monthbudget = new Monthbudget();
                monthbudget.setBudget(budget);
                monthbudget.setTime(time);
                monthbudget.setBank(bank);
                //存入数据库
                boolean budgetAdd = MonthBudgetApi.getInstance().budgetAdd(monthbudget);
                if(budgetAdd){
                    ToastUtils.showShort("提交成功");
                }else {
                    ToastUtils.showShort("提交失败");
                }
                BroadcastReceiverManager.sendReceiver(this, Constant.ACTION_ADD_MONTH_BUDGET);
                finish();
                break;
            default:
                break;
        }
    }
}
