package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button forwardButton, backButton, playButton, pauseButton;
    TextView timeText, titleText;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;

    Handler handler = new Handler();

    double startTime = 0;
    double finalTime = 0;

    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.play_button);
        backButton = findViewById(R.id.back_button);
        forwardButton = findViewById(R.id.forward_button);
        pauseButton = findViewById(R.id.pause_button);
        titleText = findViewById(R.id.music_title);
        timeText = findViewById(R.id.time_text);
        seekBar = findViewById(R.id.seek_bar);

        titleText.setText(getResources().getIdentifier("living_life","raw",getPackageName()));
        mediaPlayer = MediaPlayer.create(this,R.raw.living_life);

        seekBar.setClickable(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int temp = (int) startTime;
               if((temp + forwardTime)<= finalTime) {
                   startTime = startTime + forwardTime;
                   mediaPlayer.seekTo((int) startTime);
               }else{
                   Toast.makeText(MainActivity.this,"Can't jump forward",Toast.LENGTH_SHORT).show();
               }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if((temp - backwardTime)>0){
                    startTime = startTime-backwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }else{
                    Toast.makeText(MainActivity.this, "Can't go back",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void playMusic(){
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        timeText.setText(
                String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long)finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))
                ));
        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime,100);
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            timeText.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
             TimeUnit.MILLISECONDS.toSeconds((long) startTime),
             - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))
             ));
            seekBar.setProgress((int) startTime);
            handler.postDelayed(this,100);
        }
    };
}