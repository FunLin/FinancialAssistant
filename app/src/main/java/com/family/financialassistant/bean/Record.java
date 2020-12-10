package com.family.financialassistant.bean;

import java.util.Date;

/**
 * Created by lsg on 2020-12-08
 */
public class Record {
    private int id;
    private double income;
    private double expenses;
    private String desc;
    private int type;
    private int itemType;
    private String name;
    private String time;
    private String cTime;

    public String getcTime() {
        return cTime == null ? "" : cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime == null ? "" : cTime;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public String getDesc() {
        return desc == null ? "" : desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? "" : desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

}