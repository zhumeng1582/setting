package org.ido.settings.ui.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.ido.settings.R;
import org.ido.iface.SerialControl;
import org.ido.settings.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PortActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "IDO_DEMO";
    private SerialControl serialControl;

    private Button uartTxBtn;
    private Button btnInit;

    private EditText uartTxEt;
    private EditText uartRxEt;
    private Spinner port;

    private RadioButton speed115200;
    private RadioButton speed921600;

    private RadioButton bit7;
    private RadioButton bit6;
    private RadioButton bit5;

    private RadioButton checkE;
    private RadioButton checkO;

    private RadioButton flowH;
    private RadioButton flowS;

    private RadioButton stop2;

    private List<String> serials;
    private String portName;
    private int speed = 9600;
    private int databits = 8;
    private int parity = 'N';
    private int stopbits = 1;
    private int flow_ctrl = 0;
    ArrayAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_port;
    }

    @Override
    public void initView() {

        btnInit = findViewById(R.id.btnInit);
        uartTxBtn = findViewById(R.id.uartTxBtn);
        uartTxEt = findViewById(R.id.uartTxEt);
        uartRxEt = findViewById(R.id.uartRxEt);
        port = findViewById(R.id.port);
        speed115200 = findViewById(R.id.speed115200);
        speed921600 = findViewById(R.id.speed921600);

        bit7 = findViewById(R.id.bit7);
        bit6 = findViewById(R.id.bit6);
        bit5 = findViewById(R.id.bit5);

        checkE = findViewById(R.id.checkE);
        checkO = findViewById(R.id.checkO);

        flowH = findViewById(R.id.flowH);
        flowS = findViewById(R.id.flowS);

        stop2 = findViewById(R.id.stop2);

    }

    @Override
    public void initEvent() {
        uartTxBtn.setOnClickListener(this);
        btnInit.setOnClickListener(this);

    }

    @Override
    public void initData() {
        serials = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String fileName = "/dev/ttyS" + i;
            if (FileUtils.isFileExists(fileName)) {
                serials.add(fileName);
            }
        }
        for (int i = 0; i < 11; i++) {
            String fileName = "/dev/ttyUSB" + i;
            if (FileUtils.isFileExists(fileName)) {
                serials.add(fileName);
            }
        }

        adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, serials);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);

        port.setAdapter(adapter);

        port.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == uartTxBtn) {
            if (serialControl != null) {
                serialControl.write(uartTxEt.getText().toString().getBytes());
            } else {
                ToastUtils.showShort("端口未定义");
            }
        } else if (v == btnInit) {
            if (StringUtils.equalsIgnoreCase(btnInit.getText().toString(),"打开")) {
                initSerial();
                btnInit.setText("关闭");
            }else{
                if (serialControl.close()) {
                    btnInit.setText("打开");
                }
            }

        }
    }

    private void initSerial() {
        if (StringUtils.isEmpty(portName)) {
            ToastUtils.showShort("请选择一个节点");
            return;
        }

        if (speed115200.isChecked()) {
            speed = 115200;
        } else if (speed921600.isChecked()) {
            speed = 921600;
        }
        if (bit7.isChecked()) {
            databits = 7;
        } else if (bit6.isChecked()) {
            databits = 6;
        } else if (bit5.isChecked()) {
            databits = 5;
        }

        if (checkE.isChecked()) {
            parity = 'E';
        } else if (checkO.isChecked()) {
            parity = 'O';
        }
        if (stop2.isChecked()) {
            stopbits = 2;
        }
        if (flowH.isChecked()) {
            flow_ctrl = 1;
        } else if (flowS.isChecked()) {
            flow_ctrl = 2;
        }

        serialControl = new SerialControl() {
            @Override
            protected void read(final byte[] buf, final int len) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uartRxEt.setText((new String(buf).substring(0, len)));
                    }
                });

            }
        };
        serialControl.init(portName, speed, databits, parity, stopbits, flow_ctrl, 10);//
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        portName = serials.get(position);
        initSerial();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

