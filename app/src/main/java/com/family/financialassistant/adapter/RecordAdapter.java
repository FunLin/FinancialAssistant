package com.family.financialassistant.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.family.financialassistant.R;
import com.family.financialassistant.bean.HistoryBean;
import com.family.financialassistant.bean.Record;

import java.util.List;

/**
 * Created by lsg on 2020-12-09
 */
public class RecordAdapter extends BaseItemDraggableAdapter<Record, BaseViewHolder> {
    public RecordAdapter(List<Record> data) {
        super(R.layout.rcv_item_record,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record item) {
        initViewVisible(helper);
        if(item.getType() == 0){
            helper.setText(R.id.tv_money,String.valueOf(item.getExpenses()));
           // itemType 衣食住行0123 4全选 5衣食住 6衣食 7衣住 8衣行 9食住行 10食住 11食行 12住行
            switch (item.getItemType()){
                case 0:
                    helper.setVisible(R.id.ll_y,true);
                    break;
                case 1:
                    helper.setVisible(R.id.ll_s,true);
                    break;
                case 2:
                    helper.setVisible(R.id.ll_z,true);
                    break;
                case 3:
                    helper.setVisible(R.id.ll_x,true);
                    break;
                case 4:
                    helper.setVisible(R.id.ll_yszx,true);
                    break;
                case 5:
                    helper.setVisible(R.id.ll_ysz,true);
                    break;
                case 6:
                    helper.setVisible(R.id.ll_ys,true);
                    break;
                case 7:
                    helper.setVisible(R.id.ll_yz,true);
                    break;
                case 8:
                    helper.setVisible(R.id.ll_yx,true);
                    break;
                case 9:
                    helper.setVisible(R.id.ll_szx,true);
                    break;
                case 10:
                    helper.setVisible(R.id.ll_sz,true);
                    break;
                case 11:
                    helper.setVisible(R.id.ll_sx,true);
                    break;
                case 12:
                    helper.setVisible(R.id.ll_zx,true);
                    break;
                default:
                    break;
            }
        }else {
            helper.setText(R.id.tv_money,String.valueOf(item.getIncome()));
            switch (item.getItemType()){
                case 0:
                    helper.setVisible(R.id.ll_gz,true);
                    break;
                case 1:
                    helper.setVisible(R.id.ll_hb,true);
                    break;
                case 4:
                    helper.setVisible(R.id.ll_gz_hb,true);
                    break;
                default:
                    break;
            }
        }
        helper.setText(R.id.tv_desc,item.getDesc());
        helper.setText(R.id.tv_time,item.getTime());
    }

    /**
     * 初始都设置为隐藏 防止页面回收导致错乱
     * @param helper
     */
    private void initViewVisible(BaseViewHolder helper) {
        helper.setGone(R.id.ll_y,false);
        helper.setGone(R.id.ll_s,false);
        helper.setGone(R.id.ll_z,false);
        helper.setGone(R.id.ll_x,false);
        helper.setGone(R.id.ll_yszx,false);
        helper.setGone(R.id.ll_ysz,false);
        helper.setGone(R.id.ll_ys,false);
        helper.setGone(R.id.ll_yz,false);
        helper.setGone(R.id.ll_yx,false);
        helper.setGone(R.id.ll_szx,false);
        helper.setGone(R.id.ll_sx,false);
        helper.setGone(R.id.ll_sz,false);
        helper.setGone(R.id.ll_sx,false);
        helper.setGone(R.id.ll_gz_hb,false);
        helper.setGone(R.id.ll_gz,false);
        helper.setGone(R.id.ll_hb,false);
    }


}
