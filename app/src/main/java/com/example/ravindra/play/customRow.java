package com.example.ravindra.play;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Ravindra on 10/8/2017.
 */

public class customRow extends ArrayAdapter<String> {

    int[] duration;

    public customRow(Context context, String[] item, int[] duration) {
        super(context, R.layout.list_text_style, item);
        this.duration = duration;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater ravindrainflater = LayoutInflater.from(getContext());
        View customView = ravindrainflater.inflate(R.layout.list_text_style, parent, false);

        String songtitle = getItem(position);
        int songduration = duration[position];

        String seconds = String.valueOf((songduration % 60000) / 1000);
        String minutes = String.valueOf(songduration / 60000);

        TextView textView1 = (TextView) customView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) customView.findViewById(R.id.textView2);
        ImageButton play1 = (ImageButton) customView.findViewById(R.id.play1);

        play1.setImageResource(R.mipmap.ic_play_arrow_white_24dp);

        textView1.setText(songtitle);

        if (seconds.length() == 1) {
            textView2.setText(minutes + ":0" + seconds);
        } else {
            textView2.setText(minutes + ":" + seconds);
        }

        return customView;
    }
}
