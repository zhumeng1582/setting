package org.ido.settings.boot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.ThreadUtils;

import org.ido.settings.ui.data.Switch;
import org.ido.settings.ui.data.WeekdayBean2;
import org.ido.settings.ui.main.SetOffAndOnActivity;
import org.ido.settings.utils.DeviceUtil;

import java.util.concurrent.TimeUnit;

public class AiService extends Service {
    static final String TAG = "AiService";
    boolean isStart = false;
    static final int NOTIFICATION_CHANNEL_ID = 1;
    static final int TIME_CHECK_INTERVAL = 30;//单位秒

    private void rtcOnOffProcess() {
        WeekdayBean2 weekdayBean2 = (WeekdayBean2) CacheDiskStaticUtils.getSerializable(SetOffAndOnActivity.WeekdayBean2,null , CacheDiskUtils.getInstance());
        Switch aSwitch = (Switch) CacheDiskStaticUtils.getSerializable(SetOffAndOnActivity.SWITCH, null , CacheDiskUtils.getInstance());
        if (aSwitch != null && weekdayBean2 != null) {
            DeviceUtil.setBoot(AiService.this, aSwitch, weekdayBean2);
        }
    }

    ThreadUtils.Task task = new ThreadUtils.Task<Object>() {

        @Override
        public Object doInBackground() throws Throwable {
            return null;
        }

        @Override
        public void onSuccess(Object result) {
            //LogUtils.d(TAG, "-------->task run");
            rtcOnOffProcess();
        }

        @Override
        public void onCancel() {}

        @Override
        public void onFail(Throwable t) {}
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 获取系统 通知管理 服务
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 构建 Notification
            Notification.Builder builder = new Notification.Builder(this,"" + NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle("定时开关机服务")
                    .setSmallIcon(android.R.drawable.presence_online)
                    .setContentText("正在运行，请勿停止！");

            // 兼容  API 26，Android 8.0
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
                NotificationChannel notificationChannel = new NotificationChannel("IdoAppNotificationId", "IdoAppNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
                // 注册通道，注册后除非卸载再安装否则不改变
                notificationManager.createNotificationChannel(notificationChannel);
                builder.setChannelId("IdoAppNotificationId");
            }

            Notification notification;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            } else {
                //noinspection deprecation
                notification = builder.getNotification();
            }
            startForeground(NOTIFICATION_CHANNEL_ID, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //LogUtils.d(TAG, "-------->Process:"+ ProcessUtils.getCurrentProcessName());
        LogUtils.d(TAG, "-------->onStartCommand");
        if (!isStart) {
            ThreadUtils.executeByIoAtFixRate(task, 0, TIME_CHECK_INTERVAL, TimeUnit.SECONDS);
            isStart = true;
        } else {
            rtcOnOffProcess();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.d(TAG, "-------->onDestroy");
        stopForeground(true);
    }
}