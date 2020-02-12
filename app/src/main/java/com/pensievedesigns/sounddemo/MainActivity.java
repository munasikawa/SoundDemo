package com.pensievedesigns.sounddemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    // Usually made like  MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.failure);
    // So here we make the variable public, then we can use it from different methods

    MediaPlayer mPlayer;

    /** This is an Audio manager which allows us to interact with the android system and allow
     * us to change volume and things like that*/

    AudioManager audioManager;

    public void playAudio(View view) {
        //This method starts the audio

        mPlayer.start();

    }

    public void pauseAudio(View view) {
        //This method pauses the audio

        mPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We put this in the onCreate method so the file is set to the media player when the app starts

        mPlayer = MediaPlayer.create(this, R.raw.failure);

        // Establish maximum volume of the phone we are working with, and establish the current volume and we'll use those to update the seekBar
        // First we take the audio manager and set it up so we can use it  to work with the phones Audio System using getSystemService; cast to AudioManager
        // maxVolume get the max volume using getStreamMaxVolume and set it to STREAM_MUSIC and get currentVolume

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // We put the seekBar on the onCreate
        // we need to set the maximum volume of the system to be the maximum volume of our seek bar

        SeekBar volumeControl = findViewById(R.id.seekBar);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /** Returns three variables, the seekBar, the progress (the number we are interested in
                how far the user has clicked the seek bar and a boolean to check if the user is the one who changed the seekBar
                 or we are running it from our code. If we are running from the code its false */

                // When the user updates the seekbar it updates the stream volume
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // find seekBar view
        final SeekBar scrubber = findViewById(R.id.scrubber);

        // get the length of our audio file
        scrubber.setMax(mPlayer.getDuration());

        // we are creating a scheduling at a fixed rate which means we are scheduling a certain task to be done
        // at a fixed schedule. A timer task. The 0 and 1000 give us the number of seconds for the timer tun. 0 means no delay
        // this timer will run every second and will update the value of our scrubber or the progress of our seek bar and show the current position

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubber.setProgress(mPlayer.getCurrentPosition());
            }
        },0, 1000);

        scrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // this will seek to a certain position back and forth
                mPlayer.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(0);
            }
        });












    }
}
