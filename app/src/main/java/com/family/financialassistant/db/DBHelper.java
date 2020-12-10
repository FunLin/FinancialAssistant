/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.family.financialassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建工具
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_START_SQL = "CREATE TABLE IF NOT EXISTS ";
    private static final String CREATE_TABLE_PRIMARY_SQL = " integer primary key autoincrement,";

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "database.db";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;

    /**
     * 用户表
     */
    public static final String TABLE_RECORDS = "records";

    /**
     * 识别记录表
     */
    public static final String TABLE_MONTH_BUDGET = "budget";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTH_BUDGET);
            onCreate(db);
        }
    }

    public synchronized void createTables(SQLiteDatabase db) {
        if (db == null || db.isReadOnly()) {
            db = getWritableDatabase();
        }

        // 创建Record的SQL语句
        StringBuffer recordSql = new StringBuffer();
        recordSql.append(CREATE_TABLE_START_SQL).append(TABLE_RECORDS).append(" ( ");
        recordSql.append(" _id").append(CREATE_TABLE_PRIMARY_SQL);
        recordSql.append(" income").append(" varchar(32) default \"\"   ,");//收入
        recordSql.append(" expenses").append(" varchar(32) default \"\"   ,");//支出
        recordSql.append(" desc").append(" varchar(128) default \"\"   ,");//备注
        recordSql.append(" type").append(" varchar(32) default \"\"   ,");//支出 0 r 收入1
        recordSql.append(" name").append(" varchar(32) default \"\"   ,");//支出or收入的具体项名称
        recordSql.append(" itemType").append(" varchar(32) default \"\"   ,");//支出or收入的具体项type
        // 衣食住行0123 4全选 5衣食住 6衣食 7衣住 8衣行 9食住行 10食住 11食行 12住行
        // 工资红包 01 4全选
        recordSql.append(" ctime").append(" varchar(32) default \"\"   ,");//时间戳
        recordSql.append(" time").append(" varchar(32) default \"\"   )");//记录时间

        // 创建月预算的SQL语句
        StringBuffer budgetSql = new StringBuffer();
        budgetSql.append(CREATE_TABLE_START_SQL).append(TABLE_MONTH_BUDGET).append(" ( ");
        budgetSql.append(" _id").append(CREATE_TABLE_PRIMARY_SQL);
        budgetSql.append(" budget").append(" varchar(32) default \"\"   ,");//预算
        budgetSql.append(" bank").append(" varchar(32) default \"\"   ,");//银行
        budgetSql.append(" ctime").append(" varchar(32) default \"\"   ,");//时间戳
        budgetSql.append(" time").append(" varchar(32) default \"\"   )");//记录时间

        try {
            db.execSQL(recordSql.toString());
            db.execSQL(budgetSql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
