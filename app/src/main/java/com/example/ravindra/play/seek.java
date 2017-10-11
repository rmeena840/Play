package com.example.ravindra.play;

import android.media.MediaPlayer;
import android.widget.SeekBar;

import static java.lang.Thread.sleep;

public class seek implements Runnable {

    private MediaPlayer mediaPlayer = null;
    private SeekBar seekBar = null;

    public seek(MediaPlayer a, SeekBar b) {
        mediaPlayer = a;
        seekBar = b;
    }

    @Override
    public void run() {
        int totalduration = mediaPlayer.getDuration();
        int currentposition = 0;
        seekBar.setMax(mediaPlayer.getDuration());
        while (currentposition < totalduration) {
            try {
                sleep(500);
                currentposition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentposition);

            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
