/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.family.financialassistant.db;


import com.family.financialassistant.bean.UserBean;

import java.util.List;

/**
 * 用户API类,提供查询删除等功能
 */
public class UserApi {
    private static UserApi instance;

    private UserApi() {

    }

    public static synchronized UserApi getInstance() {
        if (instance == null) {
            instance = new UserApi();
        }
        return instance;
    }

    /**
     * 添加用户
     */
    public boolean UserAdd(UserBean user) {
        if (user == null || user.getAccount() == 0) {
            return false;
        }
        boolean ret = DBManager.getInstance().addUser(user);

        return ret;
    }

    /**
     * 查询所有用户
     */
    public List<UserBean> getAllUserList() {
        return DBManager.getInstance().queryAllUsers();
    }


}
