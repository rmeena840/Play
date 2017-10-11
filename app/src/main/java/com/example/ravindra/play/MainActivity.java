package com.example.ravindra.play;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int CODE = 1;
    ListView listView;
    String[] items;
    int[] duration;
    SeekBar seekBar;
    ImageButton play, next, previous;
    ArrayList<File> mysongs;
    int pos = 0;
    MediaPlayer mediaPlayer;
    ListAdapter listAdapter;
    boolean let = false;
    ImageButton previoussong = null;
    ImageView albumimage;
    TextView songtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        previous = (ImageButton) findViewById(R.id.previous);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        listView = (ListView) findViewById(R.id.listview);
        albumimage = (ImageView) findViewById(R.id.albumimage);

        songtitle = (TextView) findViewById(R.id.songtitle);
        songtitle.setText("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();

                }

                ImageButton nextsong = (ImageButton) view.findViewById(R.id.play1);
                if (!let) {
                    if (previoussong != nextsong) {
                        nextsong.setImageResource(R.mipmap.ic_pause_white_24dp);
                        if (previoussong != null)
                            previoussong.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
                        previoussong = nextsong;
                        let = true;
                    }
                } else {
                    if (previoussong != nextsong) {
                        let = false;
                        previoussong.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
                        nextsong.setImageResource(R.mipmap.ic_pause_white_24dp);
                        previoussong = nextsong;
                    }
                }

                try {
                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(mysongs.get(i).toString());
                    byte[] art = metaRetriever.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    albumimage.setImageBitmap(songImage);
                } catch (Exception e) {
                    albumimage.setImageResource(R.drawable.download);
                }

                Uri uri = Uri.parse(mysongs.get(i).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                pos = i;
                songtitle.setText(items[pos]);
                play.setImageResource(R.mipmap.ic_pause_white_24dp);
                seekBar.setMax(mediaPlayer.getDuration());
                Thread t = new Thread(new seek(mediaPlayer, seekBar));
                seekBar.setProgress(0);
                mediaPlayer.start();
                t.start();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer == null) {
                    Uri uri = Uri.parse(mysongs.get(0).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    pos = 0;
                    songtitle.setText(items[pos]);
                    play.setImageResource(R.mipmap.ic_pause_white_24dp);
                    seekBar.setMax(mediaPlayer.getDuration());
                    Thread t = new Thread(new seek(mediaPlayer, seekBar));
                    seekBar.setProgress(0);
                    mediaPlayer.start();
                    t.start();
                    return;
                }


                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.mipmap.ic_pause_white_24dp);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer == null) {
                    Uri uri = Uri.parse(mysongs.get(0).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    pos = 0;
                    songtitle.setText(items[pos]);
                    play.setImageResource(R.mipmap.ic_pause_white_24dp);
                    seekBar.setMax(mediaPlayer.getDuration());
                    Thread t = new Thread(new seek(mediaPlayer, seekBar));
                    seekBar.setProgress(0);
                    mediaPlayer.start();
                    t.start();
                    return;
                }

                mediaPlayer.stop();
                mediaPlayer.release();

                Uri uri = Uri.parse(mysongs.get(pos = (pos + 1) % mysongs.size()).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                play.setImageResource(R.mipmap.ic_pause_white_24dp);
                songtitle.setText(items[pos]);
                seekBar.setProgress(0);
                Thread t = new Thread(new seek(mediaPlayer, seekBar));
                mediaPlayer.start();
                t.start();

                try {

                    ImageButton play1;

                    View nextview = listView.getChildAt(pos - listView.getFirstVisiblePosition());
                    play1 = (ImageButton) nextview.findViewById(R.id.play1);
                    play1.setImageResource(R.mipmap.ic_pause_white_24dp);

                    listAdapter.getView(pos - listView.getFirstVisiblePosition(), nextview, null);

                    View previousview = listView.getChildAt(pos - listView.getFirstVisiblePosition() - 1);
                    play1 = (ImageButton) previousview.findViewById(R.id.play1);
                    play1.setImageResource(R.mipmap.ic_play_arrow_white_24dp);

                } catch (Exception e) {

                }


                try {
                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(mysongs.get(pos).toString());
                    byte[] art = metaRetriever.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    albumimage.setImageBitmap(songImage);
                } catch (Exception e) {
                    albumimage.setImageResource(R.drawable.download);
                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer == null) {
                    Uri uri = Uri.parse(mysongs.get(0).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    pos = 0;
                    songtitle.setText(items[pos]);
                    play.setImageResource(R.mipmap.ic_pause_white_24dp);
                    seekBar.setMax(mediaPlayer.getDuration());
                    Thread t = new Thread(new seek(mediaPlayer, seekBar));
                    seekBar.setProgress(0);
                    mediaPlayer.start();
                    t.start();
                    return;
                }

                mediaPlayer.stop();
                mediaPlayer.release();
                Uri uri = Uri.parse(mysongs.get(pos = (pos - 1 < 0) ? (mysongs.size() - 1) : pos - 1).toString());
                songtitle.setText(items[pos]);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                play.setImageResource(R.mipmap.ic_pause_white_24dp);
                seekBar.setProgress(0);
                Thread t = new Thread(new seek(mediaPlayer, seekBar));
                mediaPlayer.start();
                t.start();

                if (pos == mysongs.size() - 1) {

                } else {
                    ImageButton play1;
                    View nextview = listView.getChildAt(pos - listView.getFirstVisiblePosition());
                    play1 = (ImageButton) nextview.findViewById(R.id.play1);
                    play1.setImageResource(R.mipmap.ic_pause_white_24dp);


                    View previousview = listView.getChildAt(pos - listView.getFirstVisiblePosition() + 1);
                    play1 = (ImageButton) previousview.findViewById(R.id.play1);
                    play1.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
                }


                try {
                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(mysongs.get(pos).toString());
                    byte[] art = metaRetriever.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    albumimage.setImageBitmap(songImage);
                } catch (Exception e) {
                    albumimage.setImageResource(R.drawable.download);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    seekBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CODE);
                result();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CODE);
            }
        } else {
            result();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        result();
                    }
                }
            }
        }
    }

    public ArrayList<File> findsongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findsongs(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3")) {
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    public void result() {
        mysongs = findsongs(Environment.getExternalStorageDirectory());
        items = new String[mysongs.size()];
        duration = new int[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            items[i] = mysongs.get(i).getName().toString();

            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(mysongs.get(i).toString());
            duration[i] = Integer.parseInt(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        }
        listAdapter = new customRow(this, items, duration);
        listView.setAdapter(listAdapter);
    }


}