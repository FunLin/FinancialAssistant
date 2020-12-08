package com.family.financialassistant.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseFragment;
import com.family.financialassistant.databinding.FragmentHistoryBinding;


/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 历史操作记录
 **/
public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {


    public static HistoryFragment getInstance(){
        return new HistoryFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void processLogic() {

    }
}
