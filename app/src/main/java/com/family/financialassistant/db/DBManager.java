package com.family.financialassistant.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.family.financialassistant.bean.Monthbudget;
import com.family.financialassistant.bean.Record;
import com.family.financialassistant.bean.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据库管理类
 */
public class DBManager {
    /**
     * The constant TAG
     */
    private static final String TAG = "DBManager";

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static DBManager instance;
    private static SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private boolean allowTransaction = true;
    private Lock writeLock = new ReentrantLock();
    private volatile boolean writeLocked = false;

    /**
     * 单例模式，初始化DBManager
     *
     * @return DBManager实例
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * 数据库初始化
     *
     * @param context 当前上下文
     */
    public void init(Context context) {
        if (context == null) {
            return;
        }

        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context.getApplicationContext());
        }
    }

    /**
     * 释放数据库
     */
    public void release() {
        if (mDBHelper != null) {
            mDBHelper.close();
            mDBHelper = null;
        }
        instance = null;
    }

    /**
     * 打开数据库
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            try {
                mDatabase = mDBHelper.getWritableDatabase();
            } catch (Exception e) {
                Log.e(TAG, "openDatabase e = " + e.getMessage());
                mDatabase = mDBHelper.getReadableDatabase();
            }
        }
        return mDatabase;
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }


    // ---------------------------------------记账记录相关 start----------------------------------------

    /**
     * 添加记录
     */
    public boolean addRecord(Record record) {
        if (mDBHelper == null) {
            return false;
        }
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            ContentValues cv = new ContentValues();
            cv.put("income", record.getIncome());
            cv.put("expenses", record.getExpenses());
            cv.put("desc", record.getDesc());
            cv.put("type", record.getType());
            cv.put("name", record.getName());
            cv.put("time", record.getTime());
            cv.put("itemType", record.getItemType());
            cv.put("ctime", String.valueOf(System.currentTimeMillis()));

            long rowId = mDatabase.insert(DBHelper.TABLE_RECORDS, null, cv);
            if (rowId < 0) {
                return false;
            }
            setTransactionSuccessful(mDatabase);
            Log.i(TAG, "insert user success:" + rowId);
        } catch (Exception e) {
            Log.e(TAG, "addUser e = " + e.getMessage());
            return false;
        } finally {
            endTransaction(mDatabase);
        }
        return true;
    }

    /**
     * 查询所有记账记录
     */
    public List<Record> queryAllRecords() {
        Cursor cursor = null;
        List<Record> records = new ArrayList<>();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_RECORDS, null, null, null, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                double income = cursor.getDouble(cursor.getColumnIndex("income"));
                double expenses = cursor.getDouble(cursor.getColumnIndex("expenses"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                int itemType = cursor.getInt(cursor.getColumnIndex("itemType"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String ctime = cursor.getString(cursor.getColumnIndex("ctime"));

                Record record = new Record();
                record.setId(dbId);
                record.setIncome(income);
                record.setExpenses(expenses);
                record.setDesc(desc);
                record.setType(type);
                record.setName(name);
                record.setTime(time);
                record.setcTime(ctime);
                record.setItemType(itemType);
                records.add(record);
            }
        } finally {
            closeCursor(cursor);
        }
        return records;
    }

    /**
     * 查询指定记账记录
     */
    public Record queryRecordByTime(String queryTime) {
        Cursor cursor = null;
        Record record = new Record();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "time = ?";
            String[] whereValue = {queryTime};
            cursor = db.query(DBHelper.TABLE_RECORDS, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                double income = cursor.getDouble(cursor.getColumnIndex("income"));
                double expenses = cursor.getDouble(cursor.getColumnIndex("expenses"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                int itemType = cursor.getInt(cursor.getColumnIndex("itemType"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String ctime = cursor.getString(cursor.getColumnIndex("ctime"));

                record.setId(dbId);
                record.setIncome(income);
                record.setExpenses(expenses);
                record.setDesc(desc);
                record.setType(type);
                record.setName(name);
                record.setTime(time);
                record.setcTime(ctime);
                record.setItemType(itemType);
            }
        } finally {
            closeCursor(cursor);
        }
        return record;
    }

    /**
     * 删除记录
     */
    public boolean deleteRecord(String cTime) {
        boolean success = false;
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (!TextUtils.isEmpty(cTime)) {
                String where = "ctime = ?";
                String[] whereValue = {cTime};

                if (mDatabase.delete(DBHelper.TABLE_RECORDS, where, whereValue) < 0) {
                    return false;
                }

                setTransactionSuccessful(mDatabase);
                success = true;
            }

        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }

    // ---------------------------------------记账记录相关 end------------------------------------------

    // ---------------------------------------月预算相关 start----------------------------------------

    /**
     * 添加
     */
    public boolean addMonthBudget(Monthbudget monthbudget) {
        if (mDBHelper == null) {
            return false;
        }
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);
            ContentValues cv = new ContentValues();
            cv.put("budget", monthbudget.getBudget());
            cv.put("bank", monthbudget.getBank());
            cv.put("time", monthbudget.getTime());
            cv.put("ctime", String.valueOf(System.currentTimeMillis()));

            long rowId = mDatabase.insert(DBHelper.TABLE_MONTH_BUDGET, null, cv);
            if (rowId < 0) {
                return false;
            }
            setTransactionSuccessful(mDatabase);
            Log.i(TAG, "insert user success:" + rowId);
        } catch (Exception e) {
            Log.e(TAG, "addUser e = " + e.getMessage());
            return false;
        } finally {
            endTransaction(mDatabase);
        }
        return true;
    }

    /**
     * 查询所有月预算（按时间降序排序）
     */
    public List<Monthbudget> queryAllMonthBudgets() {
        Cursor cursor = null;
        List<Monthbudget> monthbudgets = new ArrayList<>();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_MONTH_BUDGET, null, null, null, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                double budget = cursor.getDouble(cursor.getColumnIndex("budget"));
                String bank = cursor.getString(cursor.getColumnIndex("bank"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String ctime = cursor.getString(cursor.getColumnIndex("ctime"));

                Monthbudget monthbudget = new Monthbudget();
                monthbudget.setId(dbId);
                monthbudget.setBank(bank);
                monthbudget.setTime(time);
                monthbudget.setBudget(budget);
                monthbudget.setcTime(ctime);
                monthbudgets.add(monthbudget);
            }
        } finally {
            closeCursor(cursor);
        }
        return monthbudgets;
    }

    /**
     * 查询指定月预算
     */
    public Monthbudget queryMonthBudgetByTime(String queryTime) {
        Cursor cursor = null;
        Monthbudget monthbudget = new Monthbudget();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            String where = "time = ? ";
            String[] whereValue = {queryTime};
            cursor = db.query(DBHelper.TABLE_MONTH_BUDGET, null, where, whereValue, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                double budget = cursor.getDouble(cursor.getColumnIndex("budget"));
                String bank = cursor.getString(cursor.getColumnIndex("bank"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String ctime = cursor.getString(cursor.getColumnIndex("ctime"));

                monthbudget.setId(dbId);
                monthbudget.setBank(bank);
                monthbudget.setTime(time);
                monthbudget.setBudget(budget);
                monthbudget.setcTime(ctime);
            }
        } finally {
            closeCursor(cursor);
        }
        return monthbudget;
    }

    /**
     * 删除月预算
     */
    public boolean deleteMonthBudget(String cTime) {
        boolean success = false;
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            if (!TextUtils.isEmpty(cTime)) {
                String where = "ctime = ?";
                String[] whereValue = {cTime};

                if (mDatabase.delete(DBHelper.TABLE_MONTH_BUDGET, where, whereValue) < 0) {
                    return false;
                }

                setTransactionSuccessful(mDatabase);
                success = true;
            }

        } finally {
            endTransaction(mDatabase);
        }
        return success;
    }

    // ---------------------------------------月预算相关 end------------------------------------------

    // ---------------------------------------用户相关 start----------------------------------------

    /**
     * 添加用户
     */
    public boolean addUser(UserBean userBean) {
        if (mDBHelper == null) {
            return false;
        }
        try {
            mDatabase = mDBHelper.getWritableDatabase();
            beginTransaction(mDatabase);

            ContentValues cv = new ContentValues();
            cv.put("account", userBean.getAccount());
            cv.put("password", userBean.getPassword());

            long rowId = mDatabase.insert(DBHelper.TABLE_USER, null, cv);
            if (rowId < 0) {
                return false;
            }
            setTransactionSuccessful(mDatabase);
            Log.i(TAG, "insert user success:" + rowId);
        } catch (Exception e) {
            Log.e(TAG, "addUser e = " + e.getMessage());
            return false;
        } finally {
            endTransaction(mDatabase);
        }
        return true;
    }

    /**
     * 查询所有账号密码
     */
    public List<UserBean> queryAllUsers() {
        Cursor cursor = null;
        List<UserBean> userBeanList = new ArrayList<>();
        try {
            if (mDBHelper == null) {
                return null;
            }
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_USER, null, null, null, null, null, null);
            while (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
                int dbId = cursor.getInt(cursor.getColumnIndex("_id"));
                int account  = cursor.getInt(cursor.getColumnIndex("account"));
                int password = cursor.getInt(cursor.getColumnIndex("password"));

                UserBean userBean = new UserBean();
                userBean.setAccount(account);
                userBean.setPassword(password);
                userBeanList.add(userBean);
            }
        } finally {
            closeCursor(cursor);
        }
        return userBeanList;
    }

    // ---------------------------------------用户相关 end------------------------------------------

    private void beginTransaction(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.beginTransaction();
        } else {
            writeLock.lock();
            writeLocked = true;
        }
    }

    private void setTransactionSuccessful(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.setTransactionSuccessful();
        }
    }

    private void endTransaction(SQLiteDatabase mDatabase) {
        if (allowTransaction) {
            mDatabase.endTransaction();
        }
        if (writeLocked) {
            writeLock.unlock();
            writeLocked = false;
        }
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable e) {
                Log.e(TAG, "closeCursor e = " + e.getMessage());
            }
        }
    }

    /**
     * 清空记录表
     */
    public void clearRecordTable() {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        database.execSQL("delete from " + DBHelper.TABLE_RECORDS);
    }

    /**
     * 清空月预算表
     */
    public void clearMonthBudgetTable() {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        database.execSQL("delete from " + DBHelper.TABLE_MONTH_BUDGET);
    }


}