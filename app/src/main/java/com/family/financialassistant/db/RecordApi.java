/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.family.financialassistant.db;

import android.text.TextUtils;

import com.family.financialassistant.bean.Record;

import java.util.List;

public class RecordApi {
    private static RecordApi instance;

    private RecordApi() {

    }

    public static synchronized RecordApi getInstance() {
        if (instance == null) {
            instance = new RecordApi();
        }
        return instance;
    }

    /**
     * 添加记录
     */
    public boolean recordAdd(Record record) {
        if (record == null || TextUtils.isEmpty(record.getTime())) {
            return false;
        }
        boolean ret = DBManager.getInstance().addRecord(record);

        return ret;
    }

    /**
     * 查询所有记录
     */
    public List<Record> getAllRecordList() {
        return DBManager.getInstance().queryAllRecords();
    }

    /**
     * 根据时间查询记录
     */
    public Record getRecordByTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        return DBManager.getInstance().queryRecordByTime(time);
    }

    /**
     * 根据时间删除记录
     */
    public boolean recordDelete(String time) {
        if (TextUtils.isEmpty(time)) {
            return false;
        }
        boolean ret = DBManager.getInstance().deleteRecord(time);
        return ret;
    }
}