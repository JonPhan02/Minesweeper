package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int seconds = 0;
    private int totalSeconds = 0;
    private int flagCount = 4;

    // false tool = pickaxe
    // true tool = flag
    private boolean toolTracker = false;
    private boolean running = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
        TextView flagCounter = (TextView) findViewById(R.id.flagCounter);
        flagCounter.setText(String.valueOf(flagCount));

        ImageButton tool = (ImageButton) findViewById(R.id.tool);
        tool.setBackgroundResource(R.drawable.pickaxe);
        tool.setOnClickListener(this::changeTool);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                TextView tv = new TextView(this);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);
                runTimer();
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(5, 5, 5, 5);

                lp.rowSpec = GridLayout.spec(i, 1f);

                lp.columnSpec = GridLayout.spec(j, 1f);

                grid.addView(tv, lp);
            }
        }
    }

    private void onClickTV(View view) {
        onClickStartTimer();
        if (toolTracker) {
            flagCount--;
            if (flagCount >= 0) {
                TextView flagCounter = (TextView) findViewById(R.id.flagCounter);
                flagCounter.setText(String.valueOf(flagCount));
            }
        }
    }

    public void onClickStartTimer() {
        running = true;
    }

    public void onClickStopTimer(View view) {
        running = false;
    }

    public void runTimer() {
        TextView timeView = (TextView) findViewById(R.id.time);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                totalSeconds = seconds / 60;
                int secs = totalSeconds % 60;
                int minutes = totalSeconds / 60;
                int hours = totalSeconds / 3600;
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void changeTool(View view) {
        ImageButton tool = (ImageButton) findViewById(R.id.tool);
        toolTracker = !toolTracker;
        if (toolTracker) {
            tool.setBackgroundResource(R.drawable.red_flag);
        } else {
            tool.setBackgroundResource(R.drawable.pickaxe);
        }
    }
}