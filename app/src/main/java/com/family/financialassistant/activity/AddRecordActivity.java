package com.family.financialassistant.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityAddRecordBinding;
import com.family.financialassistant.db.RecordApi;
import com.family.financialassistant.manager.BroadcastReceiverManager;
import com.family.financialassistant.util.AmountUtil;

import java.util.Calendar;

/**
 * @Time : 2020/12/08
 * @Author : lsg
 * @Description : 添加记账记录
 **/
public class AddRecordActivity extends BaseActivity<ActivityAddRecordBinding> implements View.OnClickListener {

    private int mType = 0;//0支出/1收入

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    public void processLogic() {
        bindView.setOnclick(this);
        bindView.titleBar.setRightImageId(R.drawable.ic_date);
        bindView.titleBar.setRightImageClick(this);
        bindView.titleBar.setLeftImageClick(this);
        initEditText();
        initRadioGroup();
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

                if (!AmountUtil.isOnlyPointNumber(getStringByUI(bindView.etMoney))) {
                    ToastUtils.showShort("您输入的数字保留在小数点后两位");
                    //删除多余输入的字（不会显示出来）
                    try {
                        s.delete(selectionStart - 1, selectionEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    /**
     * 管理支出或收入
     */
    private void initRadioGroup() {
        bindView.rgParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkId = radioGroup.getCheckedRadioButtonId();
                if (checkId == R.id.rb_income) {
                    //收入
                    mType = 0;
                    bindView.llExpenses.setVisibility(View.GONE);
                    bindView.llIncome.setVisibility(View.VISIBLE);
                } else {
                    //支出
                    mType = 1;
                    bindView.llExpenses.setVisibility(View.VISIBLE);
                    bindView.llIncome.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 时间选择框
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bindView.tvTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                if (KeyboardUtils.isSoftInputVisible(this)) {
                    KeyboardUtils.hideSoftInput(this);
                }
                finish();
                break;
            case R.id.btn_submit:
                Record record = new Record();
                //提交前的校验
                if (getStringByUI(bindView.tvTime).isEmpty()) {
                    ToastUtils.showShort("请选择时间");
                    return;
                }
                if (getStringByUI(bindView.etMoney).isEmpty()) {
                    ToastUtils.showShort("请输入金额");
                    return;
                }

                if (checkItemState(record)) return;
                String time = getStringByUI(bindView.tvTime);
                String desc = getStringByUI(bindView.etDesc);
                record.setDesc(desc);
                record.setTime(time);
                record.setType(mType);
                //存入数据库
                boolean recordAdd = RecordApi.getInstance().recordAdd(record);
                if (recordAdd) {
                    ToastUtils.showShort("提交成功");
                } else {
                    ToastUtils.showShort("提交失败");
                }
                BroadcastReceiverManager.sendReceiver(this, Constant.ACTION_ADD_RECORD);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 校验Checkbox的状态
     *
     * @param record
     * @return 支出 itemType 衣食住行0123 4全选 5衣食住 6衣食 7衣住 8衣行 9食住行 10食住 11食行 12住行
     * 收入 itemType 工资红包01 都选中4
     */
    private boolean checkItemState(Record record) {
        if (TextUtils.isEmpty(getStringByUI(bindView.etMoney))) {
            ToastUtils.showShort("请输入金额");
            return true;
        }
        double money = Double.parseDouble(getStringByUI(bindView.etMoney));
        if (mType == 0) {
            //支出 判断衣食住行是否被选中
            if (getCheckByUI(bindView.cbY) || getCheckByUI(bindView.cbS) ||
                    getCheckByUI(bindView.cbZ) || getCheckByUI(bindView.cbX)) {
                record.setExpenses(money);
                if (getCheckByUI(bindView.cbY) && getCheckByUI(bindView.cbS) &&
                        getCheckByUI(bindView.cbZ) && getCheckByUI(bindView.cbX)) {
                    record.setItemType(4);
                } else {
                    if (getCheckByUI(bindView.cbY) && getCheckByUI(bindView.cbS) &&
                            getCheckByUI(bindView.cbZ)) {
                        record.setItemType(5);
                    } else if (getCheckByUI(bindView.cbY) && getCheckByUI(bindView.cbS)) {
                        record.setItemType(6);
                    } else if (getCheckByUI(bindView.cbY) && getCheckByUI(bindView.cbZ)) {
                        record.setItemType(7);
                    } else if (getCheckByUI(bindView.cbY) && getCheckByUI(bindView.cbX)) {
                        record.setItemType(8);
                    } else if (getCheckByUI(bindView.cbX) && getCheckByUI(bindView.cbS) &&
                            getCheckByUI(bindView.cbZ)) {
                        record.setItemType(9);
                    } else if (getCheckByUI(bindView.cbS) && getCheckByUI(bindView.cbZ)) {
                        record.setItemType(10);
                    } else if (getCheckByUI(bindView.cbS) && getCheckByUI(bindView.cbX)) {
                        record.setItemType(11);
                    } else if (getCheckByUI(bindView.cbZ) && getCheckByUI(bindView.cbX)) {
                        record.setItemType(12);
                    }else if (getCheckByUI(bindView.cbY)) {
                        record.setItemType(0);
                    } else if (getCheckByUI(bindView.cbS)) {
                        record.setItemType(1);
                    } else if (getCheckByUI(bindView.cbZ)) {
                        record.setItemType(2);
                    } else if (getCheckByUI(bindView.cbX)) {
                        record.setItemType(3);
                    }
                }
            } else {
                ToastUtils.showShort("衣食住行至少选中一项");
                return true;
            }
        } else {
            //收入 判断工资红包是否被选中
            if (getCheckByUI(bindView.cbGz) || getCheckByUI(bindView.cbHb)) {
                record.setIncome(money);
                if (getCheckByUI(bindView.cbGz) && getCheckByUI(bindView.cbHb)) {
                    record.setItemType(4);
                } else {
                    if (getCheckByUI(bindView.cbGz)) {
                        record.setItemType(0);
                    } else {
                        record.setItemType(1);
                    }
                }
            } else {
                ToastUtils.showShort("工资红包至少选中一项");
                return true;
            }
        }
        return false;
    }

    //获取多选框选中状态
    public boolean getCheckByUI(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return true;
        } else {
            return false;
        }
    }
}
