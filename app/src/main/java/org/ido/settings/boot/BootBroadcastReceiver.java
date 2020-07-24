package org.ido.settings.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.ido.settings.ui.data.Switch;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.LogUtils;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public static String SWITCH = "Switch";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AiService.class);

        Switch aSwitch = (Switch) CacheDiskStaticUtils.getSerializable(SWITCH, null , CacheDiskUtils.getInstance());
        if (aSwitch != null && aSwitch.isSwitchGoable()) {
            LogUtils.d("Boot", "-------->start AiService");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service);
            } else {
                context.startService(service);
            }
        } else {
            LogUtils.d("Boot", "-------->not start AiService");
        }
    }
}