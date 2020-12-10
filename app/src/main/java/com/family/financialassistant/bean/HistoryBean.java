package com.family.financialassistant.bean;

/**
 * Created by lsg on 2020-12-09
 */
public class HistoryBean {
    private int type;//记账操作类比 支出0/收入1
    private int operateType;//操作类比 预算0/记账1
    private String time;
    private double money;
    private String desc;
    private String cTime;

    public HistoryBean(int type, int operateType, String time, double money, String desc,String cTime) {
        this.type = type;
        this.operateType = operateType;
        this.time = time;
        this.money = money;
        this.desc = desc;
        this.cTime = cTime;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDesc() {
        return desc == null ? "" : desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? "" : desc;
    }
}
