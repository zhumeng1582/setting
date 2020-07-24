package org.ido.settings.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheMemoryStaticUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

import org.ido.settings.ui.data.SelectBean;
import org.ido.settings.ui.data.SimpleDate;
import org.ido.settings.ui.data.Switch;
import org.ido.settings.ui.data.WeekdayBean2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class DeviceUtil {
    public static final String TAG = "DeviceUtil";
    public static final String rtcenable = "rtcEnable";
    public static final String timeon = "timeon";
    public static final String timeoff = "timeoff";
    public static final SimpleDateFormat timeformat = TimeUtils.getSafeDateFormat("HH:mm");
    public static final SimpleDateFormat dateformat = TimeUtils.getSafeDateFormat("yyyy-MM-dd ");
    public static final SimpleDateFormat datetimeformat = TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm");
    public static final int DUR = 1;
    private static boolean logSwitch = true;

    public static void open(Context mContext, SimpleDate newOff, SimpleDate newOn) {

        SimpleDate newTime = SimpleDate.getDate(TimeUtils.getNowString());
        if (newTime.equals(newOff)) {
            LogUtils.dTag(TAG,"-------->当前时间等于关机时间，不发广播");
            return;
        }

        SimpleDate lastOnTime =  CacheMemoryStaticUtils.get(timeon, new SimpleDate());
        SimpleDate lastOffTime = CacheMemoryStaticUtils.get(timeoff, new SimpleDate());

        boolean rtcOnOffEnabled = (boolean)CacheDiskStaticUtils.getSerializable(rtcenable,false);

        if (rtcOnOffEnabled && newOn.equals(lastOnTime) && newOff.equals(lastOffTime)) {
            if (logSwitch) {
                LogUtils.dTag(TAG,"-------->已经设置过相同定时开关机参数！");
                logSwitch = false;
            }
            return;
        }
        logSwitch = true;
        LogUtils.dTag(TAG,"-------->发送定时开关机广播");

        int[] mOffTime = {newOff.getYear(), newOff.getMonth(), newOff.getDay(), newOff.getHour(), newOff.getMinute()};
        int[] mOnTime = {newOn.getYear(), newOn.getMonth(), newOn.getDay(), newOn.getHour(), newOn.getMinute()};
        Intent mIntent = new Intent("android.intent.action.set.rtc.poweronoff");
        mIntent.putExtra(timeon, mOnTime); //开机时间，见上面数组定义
        mIntent.putExtra(timeoff, mOffTime); //关机时间，见上面数组定义
        mIntent.putExtra("enable", true); //true 开启,false 关闭
        mContext.sendBroadcast(mIntent);

        CacheMemoryStaticUtils.put(timeon, newOn);
        CacheMemoryStaticUtils.put(timeoff, newOff);

        CacheDiskStaticUtils.put(rtcenable,true);

    }

    public static void close(Context mContext) {
        Intent mIntent = new Intent("android.intent.action.set.rtc.poweronoff");
        mIntent.putExtra("enable", false); //true 开启,false 关闭
        mContext.sendBroadcast(mIntent);
        CacheDiskStaticUtils.put(rtcenable, false);
    }

    public static void setBoot(Context mContext, Switch aSwitch, WeekdayBean2 weekdayBean2) {
        Date current = TimeUtils.getNowDate();
        //LogUtils.d(TAG, "-------->currentTime：" + TimeUtils.date2String(current));

        if (!aSwitch.isSwitchGoable()) {
            return;
        }

        String offTime = getNextOffDateTime(weekdayBean2, aSwitch);
        String onTime = getNextOnDateTime(weekdayBean2, aSwitch);

        //关机时间大于开机时间，则关机时间无效设置为当前时间的整点(这种情况 一般是正常关机后设备被手动开机或者断电重启了，已经过了关机时间段，则需要手动关机)
        if (TimeUtils.getTimeSpan(offTime, onTime, datetimeformat, TimeConstants.MIN) > 0) {
            offTime = TimeUtils.date2String(current, datetimeformat);

            SimpleDate off = SimpleDate.getDate(offTime);
            off.setMinute(0);

            SimpleDate on = SimpleDate.getDate(onTime);

            LogUtils.d("------->currentTime：" + TimeUtils.date2String(current), "=======>offTime(invalid)：" +off.toString()  + "，onTime：" + onTime);

            DeviceUtil.open(mContext, off, on);
        } else {
            SimpleDate off = SimpleDate.getDate(offTime);
            SimpleDate on = SimpleDate.getDate(onTime);
            LogUtils.d("------->currentTime：" + TimeUtils.date2String(current), "------->offTime：" + offTime + "，onTime：" + onTime);
            DeviceUtil.open(mContext, off, on);
        }
    }

    //计算下一个有效日期相对date的天数
    private static int nextVaildDate(Switch aSwitch, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 0; i < 7; i++) {
            if (aSwitch.getWeekday()[(i + week) % 7]) {
                return i;
            }
        }
        return 0;
    }

    //获取相对当前时间的下一个关机时间
    private static String getNextOffDateTime(WeekdayBean2 weekdayBean2, Switch aSwitch) {
        Date current = TimeUtils.getNowDate();
        String currentDateTime = TimeUtils.date2String(current, datetimeformat);
        String currentDate = TimeUtils.date2String(current, dateformat);

        SelectBean select1 = weekdayBean2.getSelect1();
        SelectBean select2 = weekdayBean2.getSelect2();
        String closeTime = "";
        int offset = 0;
        if (aSwitch.isSelect1()) {
            offset = nextVaildDate(aSwitch, current);
            closeTime = currentDate + select1.getClose();
            closeTime = TimeUtils.date2String(TimeUtils.getDate(closeTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(closeTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return closeTime;
            }
        }
        if (aSwitch.isSelect2()) {
            offset = nextVaildDate(aSwitch, current);
            closeTime = currentDate + select2.getClose();
            closeTime = TimeUtils.date2String(TimeUtils.getDate(closeTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(closeTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return closeTime;
            }
        }
        if (aSwitch.isSelect1()) {
            offset = nextVaildDate(aSwitch, TimeUtils.getDate(current, 1, TimeConstants.DAY)) + 1;
            closeTime = currentDate + select1.getClose();
            closeTime = TimeUtils.date2String(TimeUtils.getDate(closeTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(closeTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return closeTime;
            }
        }
        if (aSwitch.isSelect2()) {
            offset = nextVaildDate(aSwitch, TimeUtils.getDate(current, 1, TimeConstants.DAY)) + 1;
            closeTime = currentDate + select2.getClose();
            closeTime = TimeUtils.date2String(TimeUtils.getDate(closeTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(closeTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return closeTime;
            }
        }

        return closeTime;
    }

    //获取相对当前时间的下一个开机时间
    private static String getNextOnDateTime(WeekdayBean2 weekdayBean2, Switch aSwitch) {
        Date current = TimeUtils.getNowDate();
        String currentDateTime = TimeUtils.date2String(current, datetimeformat);
        String currentDate = TimeUtils.date2String(current, dateformat);

        SelectBean select1 = weekdayBean2.getSelect1();
        SelectBean select2 = weekdayBean2.getSelect2();
        String openTime = "";
        int offset = 0;
        if (aSwitch.isSelect1()) {
            offset = nextVaildDate(aSwitch, current);
            openTime = currentDate + select1.getOpen();
            openTime = TimeUtils.date2String(TimeUtils.getDate(openTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(openTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return openTime;
            }
        }
        if (aSwitch.isSelect2()) {
            offset = nextVaildDate(aSwitch, current);
            openTime = currentDate + select2.getOpen();
            openTime = TimeUtils.date2String(TimeUtils.getDate(openTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(openTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return openTime;
            }
        }
        if (aSwitch.isSelect1()) {
            offset = nextVaildDate(aSwitch, TimeUtils.getDate(current, 1, TimeConstants.DAY)) + 1;
            openTime = currentDate + select1.getOpen();
            openTime = TimeUtils.date2String(TimeUtils.getDate(openTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(openTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return openTime;
            }
        }
        if (aSwitch.isSelect2()) {
            offset = nextVaildDate(aSwitch, TimeUtils.getDate(current, 1, TimeConstants.DAY)) + 1;
            openTime = currentDate + select2.getOpen();
            openTime = TimeUtils.date2String(TimeUtils.getDate(openTime, datetimeformat, offset, TimeConstants.DAY), datetimeformat);
            if (TimeUtils.getTimeSpan(openTime, currentDateTime, datetimeformat, TimeConstants.MIN) > 0) {
                return openTime;
            }
        }
        return openTime;
    }

    public static void setBuzzerOn(boolean on) {
        String path = "/dev/bz0";
        if (new File(path).exists()) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                if (on)
                    writer.write("ON ");
                else
                    writer.write("OFF ");
                writer.flush();
            } catch (Exception ex) {
                Log.e("setBuzzerOn", "" + ex);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Log.e("setBuzzerOn", "" + ex);
                    }
                }
            }
        } else {
            Log.d("setBuzzerOn", "buzzer node is not found!");
        }
    }

    public static void setUsbHost() {
        setOTGMode("HOST");
    }

    public static void setUsbDevice() {
        setOTGMode("DEVICE");
    }

    private static void setOTGMode(String mode) {
        String path = "/dev/otg_mode";
        if (new File(path).exists()) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                writer.write(mode + " ");
                writer.flush();
            } catch (Exception ex) {
                Log.e("setOTGMode", "" + ex);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Log.e("setOTGMode", "" + ex);
                    }
                }
            }
        } else {
            Log.d("setOTGMode", "otg mode node is not found!");
        }
    }

    //以太网的接口为：ethx x:0,,1,2..
    //WIFI的接口为：wlan0
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            Log.e(TAG, "" + ex);
        }
        return "";
    }

    //以太网的接口为：ethx x:0,,1,2..
    //WIFI的接口为：wlan0
    public static String getIpAddress(String interfaceName) {
        try {
            Enumeration<NetworkInterface> enNetworkInterface = NetworkInterface.getNetworkInterfaces(); //获取本机所有的网络接口
            while (enNetworkInterface.hasMoreElements()) {  //判断 Enumeration 对象中是否还有数据
                NetworkInterface networkInterface = enNetworkInterface.nextElement();   //获取 Enumeration 对象中的下一个数据
                if (!networkInterface.isUp()) { // 判断网口是否在使用
                    continue;
                }
                if (!interfaceName.equals(networkInterface.getDisplayName())) { // 网口名称是否和需要的相同
                    continue;
                }
                Enumeration<InetAddress> enInetAddress = networkInterface.getInetAddresses();   //getInetAddresses 方法返回绑定到该网卡的所有的 IP 地址。
                while (enInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enInetAddress.nextElement();
                    if (inetAddress instanceof Inet4Address) {  //判断是否未ipv4
                        return inetAddress.getHostAddress();
                    }
//                    判断未lo时
//                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress();
//                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }
        return "";
    }
}
