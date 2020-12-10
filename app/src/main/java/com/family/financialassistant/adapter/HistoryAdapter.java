package com.family.financialassistant.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.family.financialassistant.R;
import com.family.financialassistant.bean.HistoryBean;

import java.util.List;

/**
 * Created by lsg on 2020-12-09
 */
public class HistoryAdapter extends BaseItemDraggableAdapter<HistoryBean, BaseViewHolder> {
    public HistoryAdapter(List<HistoryBean> data) {
        super(R.layout.rcv_item_history,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryBean item) {
        if(item.getOperateType() == 1){
            if(item.getType() == 0){
                helper.setText(R.id.tv_operate,"支出");
            }else {
                helper.setText(R.id.tv_operate,"收入");
            }
            helper.setVisible(R.id.ll_desc,true);
            helper.setText(R.id.tv_desc,item.getDesc());
        }else {
            helper.setText(R.id.tv_operate,"新增预算");
            helper.setGone(R.id.ll_desc,false);
        }
        helper.setText(R.id.tv_money,String.valueOf(item.getMoney()));
        helper.setText(R.id.tv_time,item.getTime());
    }

}
