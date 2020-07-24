package org.ido.settings.ui.main;

import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.ido.settings.R;
import org.ido.settings.boot.AiService;
import org.ido.settings.ui.base.BaseActivity;
import org.ido.settings.ui.data.SelectBean;
import org.ido.settings.ui.data.Switch;
import org.ido.settings.ui.data.WeekdayBean2;
import org.ido.settings.utils.DeviceUtil;

import java.util.Date;

public class SetOffAndOnActivity extends BaseActivity {

    public static String WeekdayBean2 = "WeekdayBean2";
    public static String SWITCH = "Switch";
    private CheckBox checkSwitch;
    private CheckBox checkWeekday1;
    private CheckBox checkWeekday2;
    private CheckBox checkWeekday3;
    private CheckBox checkWeekday4;
    private CheckBox checkWeekday5;
    private CheckBox checkWeekday6;
    private CheckBox checkWeekday7;
    private CheckBox checkSelect1;
    private CheckBox checkSelect2;
    private Button textWeekday1_5;
    private Button textWeekday1_7;
    private Button textWeekdayClear;
    private TextView textOpen1;
    private TextView textOpen2;
    private TextView textClose1;
    private TextView textClose2;
    private Button btnSave;
    private TextView textTips;

    private WeekdayBean2 weekdayBean2;
    private Switch aSwitch;

    public int getLayoutId() {
        return R.layout.activity_set_off_and_on;
    }

    @Override
    public void initData() {
        aSwitch = (Switch) CacheDiskStaticUtils.getSerializable(SWITCH, null, CacheDiskUtils.getInstance());
        if (aSwitch == null) {
            aSwitch = new Switch();
            for (int i = 0; i < 7; i++) {
                aSwitch.getWeekday()[i] = true;
            }
            aSwitch.getWeekday()[0] = false;
            aSwitch.getWeekday()[6] = false;
        }

        weekdayBean2 = (WeekdayBean2) CacheDiskStaticUtils.getSerializable(WeekdayBean2, null, CacheDiskUtils.getInstance());
        if (weekdayBean2 == null) {
            weekdayBean2 = new WeekdayBean2();
            SelectBean selectBean1 = new SelectBean();
            selectBean1.setOpen("08:00");
            selectBean1.setClose("12:00");
            aSwitch.setSelect1(true);
            weekdayBean2.setSelect1(selectBean1);
            SelectBean selectBean2 = new SelectBean();
            selectBean2.setOpen("13:00");
            selectBean2.setClose("18:00");
            aSwitch.setSelect2(true);
            weekdayBean2.setSelect2(selectBean2);
        }


        checkSwitch.setChecked(aSwitch.isSwitchGoable());
        checkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setSwitchGoable(isChecked);

                if (isChecked) {
                    checkSwitch.setChecked(setBoot());
                    aSwitch.setSwitchGoable(checkSwitch.isChecked());
                } else {
                    CacheDiskStaticUtils.put(SWITCH, aSwitch);
                    DeviceUtil.close(SetOffAndOnActivity.this);
                }

            }
        });
        checkWeekday1.setChecked(aSwitch.isWeekday1());
        checkWeekday1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday1(isChecked);
            }
        });
        checkWeekday2.setChecked(aSwitch.isWeekday2());
        checkWeekday2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday2(isChecked);
            }
        });
        checkWeekday3.setChecked(aSwitch.isWeekday3());
        checkWeekday3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday3(isChecked);
            }
        });
        checkWeekday4.setChecked(aSwitch.isWeekday4());
        checkWeekday4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday4(isChecked);
            }
        });
        checkWeekday5.setChecked(aSwitch.isWeekday5());
        checkWeekday5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday5(isChecked);
            }
        });
        checkWeekday6.setChecked(aSwitch.isWeekday6());
        checkWeekday6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday6(isChecked);
            }
        });
        checkWeekday7.setChecked(aSwitch.isWeekday7());
        checkWeekday7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setWeekday7(isChecked);
            }
        });
        checkSelect1.setChecked(aSwitch.isSelect1());
        checkSelect1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setSelect1(isChecked);
            }
        });
        checkSelect2.setChecked(aSwitch.isSelect2());
        checkSelect2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aSwitch.setSelect2(isChecked);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBoot();
            }
        });

        textOpen1.setText(weekdayBean2.getSelect1().getOpen());
        textOpen2.setText(weekdayBean2.getSelect2().getOpen());
        textClose1.setText(weekdayBean2.getSelect1().getClose());
        textClose2.setText(weekdayBean2.getSelect2().getClose());

    }

    private boolean setBoot() {
        if (!aSwitch.isSwitchGoable()) {
            ToastUtils.showShort("请先开启功能");
            return false;
        }

        if (aSwitch.isWeekEmpty()) {
            ToastUtils.showShort("请至少选择一个日期");
            return false;
        }

        if (!aSwitch.isSelect2() && !aSwitch.isSelect1()) {
            ToastUtils.showShort("请至少开启一个时段");
            return false;
        }

        if (!valide()) {
            return false;
        }

        CacheDiskStaticUtils.put(WeekdayBean2, weekdayBean2);
        CacheDiskStaticUtils.put(SWITCH, aSwitch);

        Intent service = new Intent(SetOffAndOnActivity.this, AiService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SetOffAndOnActivity.this.startForegroundService(service);
        }else{
            SetOffAndOnActivity.this.startService(service);
        }

        ToastUtils.showShort("已保存");
        return true;
    }

    @Override
    public void initView() {
        checkSwitch = findViewById(R.id.checkSwitch);
        checkWeekday1 = findViewById(R.id.checkWeekday1);
        checkWeekday2 = findViewById(R.id.checkWeekday2);
        checkWeekday3 = findViewById(R.id.checkWeekday3);
        checkWeekday4 = findViewById(R.id.checkWeekday4);
        checkWeekday5 = findViewById(R.id.checkWeekday5);
        checkWeekday6 = findViewById(R.id.checkWeekday6);
        checkWeekday7 = findViewById(R.id.checkWeekday7);
        checkSelect1 = findViewById(R.id.checkSelect1);
        checkSelect2 = findViewById(R.id.checkSelect2);
        textWeekday1_5 = findViewById(R.id.textWeekday1_5);
        textWeekday1_7 = findViewById(R.id.textWeekday1_7);
        textWeekdayClear = findViewById(R.id.textWeekdayClear);
        textOpen1 = findViewById(R.id.textOpen1);
        textOpen2 = findViewById(R.id.textOpen2);
        textClose1 = findViewById(R.id.textClose1);
        textClose2 = findViewById(R.id.textClose2);
        btnSave = findViewById(R.id.btnSave);
        textTips = findViewById(R.id.textTips);
    }

    @Override
    public void initEvent() {
        textWeekday1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWeekday1.setChecked(false);
                checkWeekday2.setChecked(true);
                checkWeekday3.setChecked(true);
                checkWeekday4.setChecked(true);
                checkWeekday5.setChecked(true);
                checkWeekday6.setChecked(true);
                checkWeekday7.setChecked(false);
            }
        });
        textWeekday1_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWeekday1.setChecked(true);
                checkWeekday2.setChecked(true);
                checkWeekday3.setChecked(true);
                checkWeekday4.setChecked(true);
                checkWeekday5.setChecked(true);
                checkWeekday6.setChecked(true);
                checkWeekday7.setChecked(true);
            }
        });
        textWeekdayClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWeekday1.setChecked(false);
                checkWeekday2.setChecked(false);
                checkWeekday3.setChecked(false);
                checkWeekday4.setChecked(false);
                checkWeekday5.setChecked(false);
                checkWeekday6.setChecked(false);
                checkWeekday7.setChecked(false);
            }
        });
        textOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = TimeUtils.date2String(date, DeviceUtil.timeformat);
                        textOpen1.setText(time);
                        weekdayBean2.getSelect1().setOpen(time);
                    }
                });
            }
        });
        textOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = TimeUtils.date2String(date, DeviceUtil.timeformat);
                        textOpen2.setText(time);
                        weekdayBean2.getSelect2().setOpen(time);
                    }
                });
            }
        });
        textClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = TimeUtils.date2String(date, DeviceUtil.timeformat);
                        textClose1.setText(time);
                        weekdayBean2.getSelect1().setClose(time);
                    }
                });
            }
        });
        textClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = TimeUtils.date2String(date, DeviceUtil.timeformat);
                        textClose2.setText(time);
                        weekdayBean2.getSelect2().setClose(time);
                    }
                });
            }
        });
    }

    private void showTimePicker(View v, String time, OnTimeSelectListener listener) {


        TimePickerView pvTim = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setSubmitText("确定")
                .setCancelText("取消")
                .isDialog(ScreenUtils.isLandscape())//是否显示为对话框样式
                .build();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        if (ScreenUtils.isLandscape()) {
            params.leftMargin = 400;
            params.rightMargin = 400;
        }

        pvTim.getDialogContainerLayout().setLayoutParams(params);
        pvTim.show(v);
    }


    private boolean valide() {
        if (aSwitch.isSelect1()) {
            if (TimeUtils.getTimeSpan(weekdayBean2.getSelect1().getClose(), weekdayBean2.getSelect1().getOpen(), DeviceUtil.timeformat, TimeConstants.MIN) < DeviceUtil.DUR) {
                ToastUtils.showShort(getString(R.string.string_tips2));
                return false;
            }
        }
        if (aSwitch.isSelect2()) {
            if (TimeUtils.getTimeSpan(weekdayBean2.getSelect2().getClose(), weekdayBean2.getSelect2().getOpen(), DeviceUtil.timeformat, TimeConstants.MIN) < DeviceUtil.DUR) {
                ToastUtils.showShort(getString(R.string.string_tips2));
                return false;
            }
        }
        if (aSwitch.isSelect1() && aSwitch.isSelect2()) {
            if (TimeUtils.getTimeSpan(weekdayBean2.getSelect2().getOpen(), weekdayBean2.getSelect1().getClose(), DeviceUtil.timeformat, TimeConstants.MIN) < DeviceUtil.DUR) {
                ToastUtils.showShort(getString(R.string.string_tips2));
                return false;
            }
        }
        return true;

    }


}
