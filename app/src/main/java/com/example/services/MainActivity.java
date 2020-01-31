package com.example.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener{

    ServiceConnection sConn;
    Intent backgroundSoundServiceIntent;
    BackgroundSoundService backgroundSoundService;

    Button playButton, nextButton, prevButton;
    ProgressBar progressBar;
    TextView nowTime, nowSound;
    ProgressbarUpdaterTask updaterTask;

    RecyclerViewSoundsAdapter recyclerViewSoundsAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] sounds = {"acdc", "marley","acdc", "marley","acdc", "marley","acdc", "marley","acdc", "marley","acdc", "marley"};
        backgroundSoundServiceIntent = new Intent(this, BackgroundSoundService.class);
        backgroundSoundServiceIntent.putExtra("soundsName", sounds);
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                backgroundSoundService = ((BackgroundSoundService.MyBinder) binder).getService();
            }

            public void onServiceDisconnected(ComponentName name) {
            }
        };

        playButton = findViewById(R.id.play);
        nextButton = findViewById(R.id.nextSound);
        progressBar = findViewById(R.id.musicBar);
        nowTime = findViewById(R.id.tv_sound_time_now);
        nowSound = findViewById(R.id.tv_sound_name_now);
        prevButton = findViewById(R.id.prevSound);
        recyclerView = findViewById(R.id.rv_sounds);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSoundsAdapter = new RecyclerViewSoundsAdapter(sounds, this);
        recyclerView.setAdapter(recyclerViewSoundsAdapter);

        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(backgroundSoundServiceIntent);
        bindService(backgroundSoundServiceIntent, sConn, 0);
        updaterTask = new ProgressbarUpdaterTask();
        updaterTask.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                nowSound.setText(backgroundSoundService.getCurrentSoundName());
                backgroundSoundService.startPauseMusic();
                if (backgroundSoundService.player.isPlaying()) {
                    playButton.setText("pause");
                } else {
                    playButton.setText("play");
                }
                break;

            case R.id.nextSound:
                backgroundSoundService.nextSound();
                nowSound.setText(backgroundSoundService.getCurrentSoundName());
                break;

            case R.id.prevSound:
                Log.d("wtf", "wtf");
                backgroundSoundService.prevSound();
                nowSound.setText(backgroundSoundService.getCurrentSoundName());
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(int position) {
        backgroundSoundService.clickSoundFromRecycler(position);
        nowSound.setText(backgroundSoundService.getCurrentSoundName());
    }

    class ProgressbarUpdaterTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(backgroundSoundService.player.getCurrentPosition() / 1000);
            nowTime.setText(backgroundSoundService.player.getCurrentPosition() / 1000 + " s");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                publishProgress();
            }
        }

    }
}
