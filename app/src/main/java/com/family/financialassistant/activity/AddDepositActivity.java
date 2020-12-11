package com.family.financialassistant.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.databinding.ActivityDepositBinding;
import com.family.financialassistant.util.AmountUtil;

/**
 * Created by lsg on 2020-12-11
 * 添加存款
 */
public class AddDepositActivity extends BaseActivity<ActivityDepositBinding> implements View.OnClickListener {

    /**
     * 各大银行利率 一个月 三个月 半年 一年
     * 以下皆用简写代替 工商-gs 建设-js 中国-zg 交通-jt
     */
    private double gsRateArr[] = new double[]{1.1,1.2,1.3,1.4};
    private double jsRateArr[] = new double[]{0.8,1.1,1.2,1.32};
    private double zgRateArr[] = new double[]{0.9,1.2,1.4,1.6};
    private double jtRateArr[] = new double[]{1.3,1.4,1.5,1.5};

    @Override
    public int getLayoutId() {
        return R.layout.activity_deposit;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
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
                String budgetText = bindView.etMoney.getText().toString();
                //预算金额输入后计算最终预算
                if(!budgetText.isEmpty()){
                    computeEndBudGet(i);
                }
                getPromote();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindView.spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String budgetText = bindView.etMoney.getText().toString();
                //预算金额输入后计算最终预算
                if(!budgetText.isEmpty()){
                    computeEndBudGet(i);
                }
                getPromote();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 处理用户选择内容
     */
    private void getPromote() {
        String bankName = bindView.spBank.getSelectedItem().toString();
        String period = bindView.spPeriod.getSelectedItem().toString();
        int index = 0;
        if(period.equals("一个月")){
            index = 0;
        }else if(period.equals("三个月")){
            index = 1;
        }else if(period.equals("半年")){
            index = 2;
        }else if(period.equals("一年")){
            index = 3;
        }
        if(bankName.equals("工商银行")){
             setPromoteText(String.format(getResources().getString(R.string.rate_promote),bankName,period,gsRateArr[index] + "%"));
        }else if(bankName.equals("建设银行")){
            setPromoteText(String.format(getResources().getString(R.string.rate_promote),bankName,period,jsRateArr[index] + "%"));
        }else if(bankName.equals("中国银行")){
            setPromoteText(String.format(getResources().getString(R.string.rate_promote),bankName,period,zgRateArr[index] + "%"));
        }else if(bankName.equals("交通银行")){
            setPromoteText(String.format(getResources().getString(R.string.rate_promote),bankName,period,jtRateArr[index] + "%"));
        }
    }

    private void setPromoteText(String text){
        bindView.tvPromote.setText(text);
    }

    /**
     * 预算输入框监听,不可输入多位小数,输入完毕计算最终预算
     * 不选择银行,默认使用工商银行
     */
    private void initEditText() {
        bindView.etMoney.addTextChangedListener(new TextWatcher() {
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
                selectionStart = bindView.etMoney.getSelectionStart();
                selectionEnd = bindView.etMoney.getSelectionEnd();

                if (!AmountUtil.isOnlyPointNumber(bindView.etMoney.getText().toString())){
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
        String budgetText = bindView.etMoney.getText().toString();
        if(TextUtils.isEmpty(budgetText)){
            ToastUtils.showShort("请输入金额");
            return;
        }
        Double budget = Double.valueOf(budgetText);//用户输入的预算值
        double endBudget = 0.0;//最终预算值

        switch (index){
            case 0:
                endBudget = budget * gsRateArr[index];
                break;
            case 1:
                endBudget = budget * jsRateArr[index];
                break;
            case 2:
                endBudget = budget * zgRateArr[index];
                break;
            case 3:
                endBudget = budget * jtRateArr[index];
                break;
        }
        bindView.tvEndMoney.setText(String.valueOf(AmountUtil.formatDouble2(endBudget)));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imgLeft:
                finish();
                break;
            case R.id.btn_define:
                finish();
                break;
        }
    }
}
