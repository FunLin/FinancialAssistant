package com.family.financialassistant.manager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.family.financialassistant.R;

/**
 * Created by lsg on 2020-12-09
 * 服务管理,负责启动音乐和关闭音乐
 */
public class MusicServiceManager extends Service {
    public static boolean isplay;//记录当前的播放状态
    MediaPlayer player;
    public MusicServiceManager() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //创建MediaPlayer对象，并加载播放的音频文件
        player = MediaPlayer.create(this, R.raw.genius);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!player.isPlaying()){
            player.start();//播放音乐
            isplay = player.isPlaying();//设置当前状态为正在播放音乐
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        player.stop();//停止音乐的播放
        isplay = player.isPlaying();//设置当前状态为停止播放音乐
        player.release();//释放资源
        super.onDestroy();
    }

}
