package com.family.financialassistant.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.family.financialassistant.R;
import com.family.financialassistant.base.BaseActivity;
import com.family.financialassistant.common.Constant;
import com.family.financialassistant.databinding.ActivityNetDataBinding;

import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lsg on 2020-12-10
 */
public class NetDataActivity extends BaseActivity<ActivityNetDataBinding> {
    private static final String TAG = "NetDataActivity";
    private Handler mHandler;
    @Override
    public int getLayoutId() {
        return R.layout.activity_net_data;
    }

    @Override
    public void processLogic() {
        bindView.titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new DataThread().start();
        mHandler = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                bindView.tvResult.setText((String)msg.obj);
            }
        };
    }

    private void getNetData(){
        Message message = Message.obtain();
        RequestBody body = new FormBody.Builder() //创建表单请求
                .add("k","记账")
                .build();
        Request request = new Request.Builder()
                .url(Constant.Url)
                .post(body)
                .build();
        try (Response response = new OkHttpClient().newCall(request).execute()) {
            String result = response.body().string();
            if (response.isSuccessful()) {
                Log.e(TAG, "response code ==> " + response.code());
                Log.e(TAG, "response message ==> " + response.message());
                Log.e(TAG, "response res ==> " + result);
                message.obj = result;
                mHandler.sendMessage(message);
            } else {
                Log.e(TAG, "response fail");
                message.obj = "response fail";
                mHandler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DataThread extends Thread{
        @Override
        public void run() {
            super.run();
            getNetData();
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null){
            mHandler = null;
        }
        super.onDestroy();
    }
}
