package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;
import org.ido.settings.ui.data.SimpleDate;
import org.ido.settings.utils.DeviceUtil;

public class OtherActivity extends BaseActivity {
    private Button btnOff;
    private Button btnReset;
    private Button btnTestReset;
    private Button btnOpenADB;
    private Button btnCloseADB;
    private Button btnUsbHost;
    private Button btnUsbDevice;
    private Button btnVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_other;
    }

    @Override
    public void initView() {
        btnOff = findViewById(R.id.btnOff);
        btnReset = findViewById(R.id.btnReset);
        btnTestReset = findViewById(R.id.btnTestReset);
        btnOpenADB = findViewById(R.id.btnOpenADB);
        btnCloseADB = findViewById(R.id.btnCloseADB);
        btnUsbHost = findViewById(R.id.btnUsbHost);
        btnUsbDevice = findViewById(R.id.btnUsbDevice);
        btnVersion = findViewById(R.id.btnVersion);
    }

    @Override
    public void initEvent() {
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.ido.intent.action.set.shutdown");
                intent.putExtra("confirm", true);
                sendBroadcast(intent);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.ido.intent.action.set.reboot");
                intent.putExtra("confirm", true);
                sendBroadcast(intent);
            }
        });

        btnTestReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String offTime = TimeUtils.getNowString();
                String onTime = TimeUtils.date2String(TimeUtils.getDateByNow(1, TimeConstants.MIN));
                SimpleDate off = SimpleDate.getDate(offTime);
                SimpleDate on = SimpleDate.getDate(onTime);
                DeviceUtil.open(OtherActivity.this, off, on);
                ThreadUtils.runOnUiThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("约一分钟后开机");
                        Intent intent = new Intent("android.ido.intent.action.set.shutdown");
                        intent.putExtra("confirm", false);
                        sendBroadcast(intent);
                    }
                }, 1000);

            }
        });
        btnOpenADB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent("android.ido.intent.action.netadb");
                mIntent.putExtra("enable", true);  //true：开启网络adb调试，false：禁止网络adb调试
                sendBroadcast(mIntent);
                ToastUtils.showShort("开启网络adb调试");
            }
        });
        btnCloseADB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent("android.ido.intent.action.netadb");
                mIntent.putExtra("enable", false);  //true：开启网络adb调试，false：禁止网络adb调试
                sendBroadcast(mIntent);
                ToastUtils.showShort("禁止网络adb调试");
            }
        });
        btnUsbHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtil.setUsbHost();
            }
        });
        btnUsbDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtil.setUsbDevice();
            }
        });
        btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textVersion.setText(String.format("当前版本号:%s", AppUtils.getAppVersionName()));
                ToastUtils.showShort(String.format("当前版本号:%s", AppUtils.getAppVersionName()));
            }
        });
    }

    @Override
    public void initData() {

    }
}
