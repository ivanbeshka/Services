package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.lang.reflect.Field;

public class BackgroundSoundService extends Service {

    private static final String TAG = null;
    public MediaPlayer player;
    public MyBinder binder = new MyBinder();
    private String[] sounds;
    private int currentSound = 0;

    class MyBinder extends Binder {
        BackgroundSoundService getService() {
            return BackgroundSoundService.this;
        }
    }

    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        sounds = intent.getStringArrayExtra("soundsName");

        assert sounds != null;
        int resId = getResId(sounds[currentSound], R.raw.class);

        player = MediaPlayer.create(this, resId);
        player.setVolume(100, 100);
        return START_STICKY;
    }

    public void startPauseMusic() {
        if (player.isPlaying())
            player.pause();
        else {
            player.start();
        }
    }

    public void nextSound() {
        player.stop();
        currentSound++;
        player = MediaPlayer.create(this, getResId(sounds[currentSound], R.raw.class));
        player.start();
    }

    public void prevSound(){
        player.stop();
        currentSound--;
        player = MediaPlayer.create(this, getResId(sounds[currentSound], R.raw.class));
        player.start();
    }

    public void clickSoundFromRecycler(int position){
        player.stop();
        currentSound = position;
        player = MediaPlayer.create(this, getResId(sounds[position], R.raw.class));
        player.start();
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }

    public String getCurrentSoundName(){
        return sounds[currentSound];
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
