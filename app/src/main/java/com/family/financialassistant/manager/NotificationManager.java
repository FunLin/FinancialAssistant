package com.family.financialassistant.manager;

/**
 * Created by lsg on 2020-12-09
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;

import com.family.financialassistant.activity.AddMonthBudgetActivity;
import com.family.financialassistant.activity.MainActivity;
import com.family.financialassistant.app.APP;

import androidx.core.app.NotificationCompat;


/**
 * Created by lsg on 2020-12-09
 * 处理通知功能,兼容Android 8.0/9.0
 */
public class NotificationManager {

    private static Activity mActivity;

    public static void setActivity(Activity mActivity) {
        NotificationManager.mActivity = mActivity;
    }

    //开始通知
    @SuppressLint("WrongConstant")
    public static void startNotificationManager(final String title, final int idIco){

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context applicationContext = APP.getInstance();

                android.app.NotificationManager notificationManager = (android.app.NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent = new Intent(applicationContext, MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0);

                long [] vibrate = {0, 500, 1000, 1500};

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Notification.Builder notification = new Notification
                            .Builder(applicationContext)
                            .setContentTitle("您有一条新消息")
                            .setContentText(title)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(idIco)
                            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                            .setVibrate(vibrate)
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setChannelId(applicationContext.getPackageName())
                            .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);

                    NotificationChannel channel = new NotificationChannel(
                            applicationContext.getPackageName(),
                            "会话消息",
                            android.app.NotificationManager.IMPORTANCE_DEFAULT

                    );

                    notificationManager.createNotificationChannel(channel);

                    //创建一个启动详细页面的Intent
                    Intent intentActivity = new Intent(mActivity, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(mActivity,0,intentActivity,0);//延迟启动,当点击查看详情时再启动要跳转的activity
                    notification.setContentIntent(pi);//设置通知栏点击跳转
                    notificationManager.notify(0,notification.build()); //发送通知
                }else{

                    Notification.Builder notification = new Notification
                            .Builder(applicationContext)
                            .setContentTitle("您有一条新消息")
                            .setContentText(title)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(idIco)
                            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                            .setVibrate(vibrate)
                            .setContentIntent(pendingIntent)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
                    //创建一个启动详细页面的Intent
                    Intent intentActivity = new Intent(mActivity, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(mActivity,0,intentActivity,0);//延迟启动,当点击查看详情时再启动要跳转的activity
                    notification.setContentIntent(pi);//设置通知栏点击跳转
                    notificationManager.notify(0,notification.build()); //发送通知
                }

            }
        });


    }



}

