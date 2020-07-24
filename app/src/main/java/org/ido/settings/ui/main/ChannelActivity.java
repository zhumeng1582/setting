package org.ido.settings.ui.main;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;

import org.ido.settings.R;
import org.ido.settings.ui.base.BaseActivity;

import java.io.IOException;


public class ChannelActivity extends BaseActivity {
    private Button audioTestBtnLeft;
    private Button audioTestBtnRight;
    private MediaPlayer mpLeft;
    private MediaPlayer mpRight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_channel;
    }

    @Override
    public void initView() {
        audioTestBtnLeft = findViewById(R.id.audioTestBtnLeft);
        audioTestBtnRight = findViewById(R.id.audioTestBtnRight);
    }

    @Override
    public void initEvent() {
        audioTestBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLeft();
                pauseRight();
            }
        });
        audioTestBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRight();
                pauseLeft();
            }
        });
    }

    @Override
    public void initData() {

        try {
            AssetFileDescriptor fdLeft = getAssets().openFd("dbl.wav");
            mpLeft = new MediaPlayer();
            mpLeft.reset();
            mpLeft.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mpLeft.setDataSource(fdLeft.getFileDescriptor(), fdLeft.getStartOffset(), fdLeft.getLength());
            mpLeft.prepareAsync();

            AssetFileDescriptor fdRight = getAssets().openFd("dbr.wav");
            mpRight = new MediaPlayer();
            mpRight.reset();
            mpRight.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mpRight.setDataSource(fdRight.getFileDescriptor(), fdRight.getStartOffset(), fdRight.getLength());
            mpRight.prepareAsync();

        } catch (IOException e) {
            LogUtils.eTag("xxxxx",e.getMessage());
            e.printStackTrace();
        }

    }



    public void playLeft() {
        mpLeft.start();
    }
    public void playRight() {
        mpRight.start();
    }

    public void pauseLeft() {
        mpLeft.pause();
    }
    public void pauseRight() {
        mpRight.pause();
    }

    public void stopMusic() {
        mpLeft.stop();
        mpRight.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
    }
}
