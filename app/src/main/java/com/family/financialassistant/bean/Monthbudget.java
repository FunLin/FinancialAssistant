package com.family.financialassistant.bean;

import java.util.Date;

/**
 * Created by lsg on 2020-12-08
 */
public class Monthbudget {
    private int id;
    private double budget;
    private String bank;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getBank() {
        return bank == null ? "" : bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? "" : bank;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }
}