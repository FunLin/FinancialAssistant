package com.family.financialassistant.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.family.financialassistant.R;


/**
 * @Time : 2020-12-07
 * @Author : lsg
 * @Description : 我的
 **/
public class MineFragment extends Fragment {

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    public static MineFragment getInstance(){
        return new MineFragment();
    }

}
