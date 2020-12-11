/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.family.financialassistant.db;

import android.text.TextUtils;

import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;

import java.util.List;

/**
 * 月预算API类,提供查询删除等功能
 */
public class MonthBudgetApi {
    private static MonthBudgetApi instance;

    private MonthBudgetApi() {

    }

    public static synchronized MonthBudgetApi getInstance() {
        if (instance == null) {
            instance = new MonthBudgetApi();
        }
        return instance;
    }

    /**
     * 添加月预算
     */
    public boolean budgetAdd(Monthbudget monthbudget) {
        if (monthbudget == null || TextUtils.isEmpty(monthbudget.getTime())) {
            return false;
        }
        boolean ret = DBManager.getInstance().addMonthBudget(monthbudget);

        return ret;
    }

    /**
     * 查询所有月预算
     */
    public List<Monthbudget> getAllMonthBudgetList() {
        return DBManager.getInstance().queryAllMonthBudgets();
    }

    /**
     * 根据时间查询月预算
     */
    public Monthbudget getMonthBudgetByTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        return DBManager.getInstance().queryMonthBudgetByTime(time);
    }

    /**
     * 根据时间删除月预算
     */
    public boolean budgetDelete(String cTime) {
        if (TextUtils.isEmpty(cTime)) {
            return false;
        }
        boolean ret = DBManager.getInstance().deleteMonthBudget(cTime);
        return ret;
    }
}
