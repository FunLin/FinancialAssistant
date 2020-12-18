package com.family.financialassistant.bean;

/**
 * Created by ooo on 2020/12/15.
 */
public class UserBean {
    private int account;
    private int password;

    public UserBean() {

    }

    public UserBean(int account, int password) {
        this.account = account;
        this.password = password;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
