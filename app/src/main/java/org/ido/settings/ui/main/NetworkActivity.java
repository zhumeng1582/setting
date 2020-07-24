package org.ido.settings.ui.main;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;
import org.ido.settings.ui.data.NetworkPar;
import org.ido.settings.ui.data.WifiPar;

import java.util.ArrayList;
import java.util.List;

public class NetworkActivity extends BaseActivity {
    private static String NetworkPar = "NetworkPar";
    private static String WifiPar = "WifiPar";
    private SwitchCompat netSwitch;
    private Spinner spinnerStatic;
    private Spinner spinnerEth;
    private EditText ipAddr;
    private EditText netmask;
    private EditText gateway;
    private EditText dns1;
    private EditText dns2;
    private EditText mac;
    private EditText wifiName;
    private EditText wifiPsw;
    private Button btnEthernet;
    private Button btnWol;
    private Button btnCloseWol;
    private Button btnOpenWifi;
    private Button btnCloseWifi;
    private NetworkPar networkPar;
    private WifiPar wifiPar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_network;
    }

    @Override
    public void initView() {
        netSwitch = findViewById(R.id.netSwitch);
        spinnerStatic = findViewById(R.id.spinnerStatic);
        spinnerEth = findViewById(R.id.spinnerEth);
        ipAddr = findViewById(R.id.ipAddr);
        netmask = findViewById(R.id.netmask);
        gateway = findViewById(R.id.gateway);
        dns1 = findViewById(R.id.dns1);
        dns2 = findViewById(R.id.dns2);
        btnEthernet = findViewById(R.id.btnEthernet);
        btnWol = findViewById(R.id.btnWol);
        btnCloseWol = findViewById(R.id.btnCloseWol);
        mac = findViewById(R.id.mac);
        btnOpenWifi = findViewById(R.id.btnOpenWifi);
        btnCloseWifi = findViewById(R.id.btnCloseWifi);
        wifiName = findViewById(R.id.wifiName);
        wifiPsw = findViewById(R.id.wifiPsw);

    }

    @Override
    public void initEvent() {
        netSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    btnEthernet.setEnabled(true);
                    btnWol.setEnabled(true);
                    btnCloseWol.setEnabled(true);
                    netConect("true");
                } else {
                    netSwitch.setText("已断开");
                    btnEthernet.setEnabled(false);
                    btnWol.setEnabled(false);
                    btnCloseWol.setEnabled(false);
                    netConect("false");
                }
            }
        });

        btnCloseWol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent("android.ido.intent.action.wol");
                mIntent.putExtra("enable", "false");
                sendBroadcast(mIntent);
            }
        });


        btnCloseWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent1 = new Intent("android.ido.intent.action.wifiap");
                mIntent1.putExtra("enable", false);//true:开启AP，false:关闭
                sendBroadcast(mIntent1);
            }
        });

        btnEthernet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerStatic.getSelectedItemPosition() ==0) {
                    setIPStatic();
                }else{
                    CacheDiskStaticUtils.remove(NetworkPar);
                    Intent mIntent1 = new Intent("android.ido.intent.action.ethernet");
                    mIntent1.putExtra("mode", "DHCP");
                    sendBroadcast(mIntent1);
                }
            }
        });
        btnWol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String macAddr = mac.getText().toString();
                if (StringUtils.isEmpty(macAddr)) {
                    ToastUtils.showShort("请输入正确的mac地址");
                    return;
                }
                Intent mIntent = new Intent("android.ido.intent.action.wol");
                mIntent.putExtra("enable", "true");
                mIntent.putExtra("mac", macAddr);
                sendBroadcast(mIntent);
            }
        });
        btnOpenWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiPar wifiPar = new WifiPar();
                wifiPar.setName(wifiName.getText().toString());
                wifiPar.setPsw(wifiPsw.getText().toString());
                if (StringUtils.length(wifiPar.getPsw()) < 8) {
                    ToastUtils.showShort("Wifi 密码不能小于8位");
                    return;
                }
                Intent mIntent1 = new Intent("android.ido.intent.action.wifiap");
                mIntent1.putExtra("enable", true);//true:开启AP，false:关闭
                mIntent1.putExtra("name", wifiPar.getName());//无此参数热点默认名称为:wifiAp
                mIntent1.putExtra("password", wifiPar.getPsw());
                sendBroadcast(mIntent1);

                NetworkActivity.this.wifiPar = wifiPar;
            }
        });
    }

    private void setIPStatic() {
        NetworkPar networkPar = new NetworkPar();

        networkPar.setIpAddr(ipAddr.getText().toString());
        networkPar.setNetmask(netmask.getText().toString());
        networkPar.setGeteway(gateway.getText().toString());
        networkPar.setDns1(dns1.getText().toString());
        networkPar.setDns2(dns2.getText().toString());
        if (!RegexUtils.isIP(networkPar.getIpAddr())) {
            ToastUtils.showShort("请输入正确的ip地址");
            return;
        }
        if (!RegexUtils.isIP(networkPar.getNetmask())) {
            ToastUtils.showShort("请输入正确的子网掩码");
            return;
        }
        if (!RegexUtils.isIP(networkPar.getGeteway())) {
            ToastUtils.showShort("请输入正确的网关");
            return;
        }
        if (!RegexUtils.isIP(networkPar.getDns1())) {
            ToastUtils.showShort("请输入正确的dns1");
            return;
        }
        if (!RegexUtils.isIP(networkPar.getDns1())) {
            networkPar.setDns2("");
        }
        Intent mIntent1 = new Intent("android.ido.intent.action.ethernet");
        mIntent1.putExtra("mode", "Static");
        mIntent1.putExtra("ipAddr", networkPar.getIpAddr());
        mIntent1.putExtra("netmask", networkPar.getNetmask());
        mIntent1.putExtra("gateway", networkPar.getGeteway());
        mIntent1.putExtra("dns1", networkPar.getDns1());
        mIntent1.putExtra("dns2", networkPar.getDns1()); //可以没有
        sendBroadcast(mIntent1);
        NetworkActivity.this.networkPar = networkPar;
        CacheDiskStaticUtils.put(NetworkPar, networkPar);
    }

    private void netConect(String conect) {
        if (StringUtils.equals("true", conect)) {
            netSwitch.setText("已连接");
        }else{
            netSwitch.setText("已关闭");
        }

        Intent mIntent1 = new Intent("android.ido.intent.action.ethernet");
        mIntent1.putExtra("enable", conect); //false:断开，true:重新连接
        sendBroadcast(mIntent1);
    }

    @Override
    public void initData() {
        List<String> serials = new ArrayList<>();
        serials.add("Static");
        serials.add("DHCP");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, serials);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);

        spinnerStatic.setAdapter(adapter);
        spinnerStatic.setSelection(0);

        spinnerStatic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> eth = new ArrayList<>();
        eth.add("eth0");
        eth.add("eth1");
        ArrayAdapter adapterEth = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, eth);
        adapterEth.setDropDownViewResource(R.layout.simple_spinner_item);

        spinnerEth.setAdapter(adapterEth);
        spinnerEth.setSelection(0);

        netSwitch.setChecked(true);
        netConect("true");

        networkPar = (org.ido.settings.ui.data.NetworkPar) CacheDiskStaticUtils.getSerializable(NetworkPar);
        if (networkPar != null) {
            ipAddr.setText(networkPar.getIpAddr());
            netmask.setText(networkPar.getNetmask());
            gateway.setText(networkPar.getGeteway());
            dns1.setText(networkPar.getDns1());
            dns2.setText(networkPar.getDns2());
        }
        wifiPar = (org.ido.settings.ui.data.WifiPar) CacheDiskStaticUtils.getSerializable(WifiPar);
        if (wifiPar != null) {
            wifiName.setText(wifiPar.getName());
            wifiPsw.setText(wifiPar.getPsw());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
