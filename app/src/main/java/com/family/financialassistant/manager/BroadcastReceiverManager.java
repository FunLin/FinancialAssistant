package com.family.financialassistant.manager;

import android.content.Context;
import android.content.Intent;

/**
 * Created by lsg on 2020-12-09
 */
public class BroadcastReceiverManager{

    /**
     * 发送一条广播
     * @param context
     * @param action
     */
    public static void sendReceiver(Context context,String action){
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);//发送广播
    }

}
