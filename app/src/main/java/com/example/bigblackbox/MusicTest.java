package com.example.bigblackbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bigblackbox.entity.Music;
import com.example.bigblackbox.tool.DbUtil;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MusicTest extends AppCompatActivity implements View.OnClickListener {
    private TextView musicName, musicArtist, musicLength, musicCur;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioManager audioManager;
    private Timer timer;
    private static int mID = -1;

    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度

    SimpleDateFormat format;
    private SQLiteDatabase mDB;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbUtil mHelper = new DbUtil(this);
        mDB = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_music_test);
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);

        format = new SimpleDateFormat("mm:ss");

        ImageView play = findViewById(R.id.play);
        ImageView pause = findViewById(R.id.pause);
        ImageView stop = findViewById(R.id.stop);

        musicName = findViewById(R.id.music_name);
        musicArtist = findViewById(R.id.music_artist);
        musicLength = findViewById(R.id.music_length);
        musicCur = findViewById(R.id.music_cur);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());

        play.setOnClickListener(MusicTest.this);
        pause.setOnClickListener(MusicTest.this);
        stop.setOnClickListener(MusicTest.this);

        if (ContextCompat.checkSelfPermission(MusicTest.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MusicTest.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer();// 初始化mediaplayer
        }
        startMusic(); // 进入页面后自动播放音频
    }

    private void initMediaPlayer() {
        try {
            mID = getIntent().getIntExtra("musicID", -1);
            Music m = null;
            try (Cursor cursor = mDB.rawQuery("select * from music where music_id = ?", new String[]{String.valueOf(mID)})) {
                if (cursor.moveToNext()) {
                    m = new Music(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                }
            }
            assert m != null;
            musicName.setText(m.getMusicName());
            musicArtist.setText(m.getMusicAuthor());

            mediaPlayer.setDataSource(m.getMusicUrl());// 指定音频文件的路径
            mediaPlayer.prepare();// 让mediaPlayer进入准备状态
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @SuppressLint("SetTextI18n")
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    musicLength.setText(format.format(mediaPlayer.getDuration()) + "");
                    musicCur.setText("00:00");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMediaPlayer();
            } else {
                Toast.makeText(MusicTest.this, "denied access", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                startMusic();
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();  // 暂停播放
                    currentPosition = mediaPlayer.getCurrentPosition();  // 记录当前音频暂停位置

                }
                break;
            case R.id.stop:
                Toast.makeText(MusicTest.this, "停止播放", Toast.LENGTH_SHORT).show();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();// 停止播放
                    initMediaPlayer();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    public void startMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // 开始播放
            mediaPlayer.seekTo(currentPosition); // 在上次暂停位置继续播放

            //监听播放时回调函数
            timer = new Timer();
            timer.schedule(new TimerTask() {

                final Runnable updateUI = new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        musicCur.setText(format.format(mediaPlayer.getCurrentPosition()) + "");
                    }
                };

                @Override
                public void run() {
                    if (!isSeekBarChanging) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        runOnUiThread(updateUI);
                    }
                }
            }, 0, 50);
        }
    }
}
