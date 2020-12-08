package com.family.financialassistant.adapter;

import android.text.Html;
import android.util.SparseArray;
import android.view.ViewGroup;


import com.family.financialassistant.fragment.HomeFragment;
import com.family.financialassistant.fragment.MineFragment;
import com.family.financialassistant.fragment.HistoryFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by lsg on 2020/12/7
 */
public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    public MyFragmentPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPageAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position,fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(mFragmentList != null){
            return mFragmentList.get(position);
        }else {
            switch (position){
                case 0: return HomeFragment.getInstance();
                case 1: return HistoryFragment.getInstance();
                case 2: return MineFragment.getInstance();
                default: return HomeFragment.getInstance();
            }
        }
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitleList != null && position < mTitleList.size()){
            return Html.fromHtml(mTitleList.get(position));
        }else {
            return "";
        }
    }
}
