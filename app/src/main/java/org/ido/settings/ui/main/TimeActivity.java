package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeActivity extends BaseActivity {
    private Button btnTimeSetting;
    private TextView textTime;
    private ThreadUtils.Task<String> task;
    @Override
    public int getLayoutId() {
        return R.layout.activity_time;
    }

    @Override
    public void initView() {
        btnTimeSetting = findViewById(R.id.btnTimeSetting);
        textTime = findViewById(R.id.textTime);
    }

    @Override
    public void initEvent() {
        btnTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(btnTimeSetting, textTime.getText().toString(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateTime = TimeUtils.date2String(date);

                        String year = dateTime.substring(0, 4);
                        String month = dateTime.substring(5,7);
                        String day = dateTime.substring(8,10);
                        String hour = dateTime.substring(11,13);
                        String minute = dateTime.substring(14,16);
                        String second = dateTime.substring(17,19);

                        int[] time = {Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute),Integer.valueOf(second)};
                        Intent mIntent1 = new Intent("android.ido.intent.action.settime");
                        mIntent1.putExtra("time",time);
                        sendBroadcast(mIntent1);
                    }
                });
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        task = new ThreadUtils.Task<String>() {

            @Override
            public String doInBackground() throws Throwable {
                return "success";
            }

            @Override
            public void onSuccess(String result) {
                textTime.setText(TimeUtils.getNowString()); //更新时间

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFail(Throwable t) {

            }

        };

        ThreadUtils.executeByIoAtFixRate(task, 1000, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ThreadUtils.cancel();
    }

    private void showTimePicker(View v, String time, OnTimeSelectListener listener) {

        TimePickerView pvTim = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setSubmitText("确定")
                .setCancelText("取消")
                .isDialog(ScreenUtils.isLandscape())//是否显示为对话框样式
                .build();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        if (ScreenUtils.isLandscape()) {
            params.leftMargin = 200;
            params.rightMargin = 200;
        }

        pvTim.getDialogContainerLayout().setLayoutParams(params);
        pvTim.show(v);
    }
}
