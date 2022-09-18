package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.*;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int[] bombLocations = new int[8];
    private Set<String> visited = new HashSet<>();
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

        Random rand = new Random();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
        TextView flagCounter = (TextView) findViewById(R.id.flagCounter);
        flagCounter.setText(String.valueOf(flagCount));

        ImageButton tool = (ImageButton) findViewById(R.id.tool);
        tool.setBackgroundResource(R.drawable.pickaxe);
        tool.setOnClickListener(this::changeTool);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                TextView tv = new TextView(this);
                tv.setText(i + " " + j);
                tv.setTextColor(Color.GREEN);
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

        for (int i = 0; i < 4; i++) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(8);
            if (i == 0) {
                bombLocations[0] = x;
                bombLocations[1] = y;
            } else if (i == 1) {
                bombLocations[2] = x;
                bombLocations[3] = y;
            } else if (i == 2) {
                bombLocations[4] = x;
                bombLocations[5] = y;
            } else if (i == 3) {
                bombLocations[6] = x;
                bombLocations[7] = y;
            }
            TextView tv = new TextView(this);
            tv.setOnClickListener(this::clickBomb);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(5, 5, 5, 5);

            lp.rowSpec = GridLayout.spec(x, 1f);

            lp.columnSpec = GridLayout.spec(y, 1f);

            grid.addView(tv, lp);
        }
    }

    public void clickBomb(View view) {
        TextView temp = (TextView) view;
        temp.setText("💣");
        temp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setBackgroundColor(Color.CYAN);
        onClickStopTimer();
        //add end game method
    }

    public void onClickTV(View view) {
        TextView temp = (TextView) view;
        TextView flagCounter = (TextView) findViewById(R.id.flagCounter);
        onClickStartTimer();
        if (!toolTracker) {
            view.setBackgroundColor(Color.GRAY);
            String coordinates = (String) ((TextView) view).getText();
            ((TextView) view).setText("");
            clearMine(coordinates.charAt(0), coordinates.charAt(2));
        } else if (toolTracker && temp.getText().equals("🚩")) {
            temp.setText(" ");
            flagCounter.setText(String.valueOf(++flagCount));
        } else {
            flagCount--;
            if (flagCount >= 0) {
                temp.setText("🚩");
                temp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                flagCounter.setText(String.valueOf(flagCount));
            }
        }
    }

    public int clearMine(int x, int y) {
        if (x >= 10 && y >= 8) {
            x -= 48;
            y -= 48;
        }
        Log.d(" " + x, "x");
        Log.d(" " + y, "y");
        int bombCounter = 0;

        if ((bombLocations[0] == x - 1 && bombLocations[1] == y - 1) || (bombLocations[2] == x - 1 && bombLocations[3] == y - 1)
                || (bombLocations[4] == x - 1 && bombLocations[5] == y - 1) || (bombLocations[6] == x - 1 && bombLocations[7] == y - 1)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x && bombLocations[1] == y - 1) || (bombLocations[2] == x && bombLocations[3] == y - 1)
                || (bombLocations[4] == x && bombLocations[5] == y - 1) || (bombLocations[6] == x && bombLocations[7] == y - 1)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x + 1 && bombLocations[1] == y - 1) || (bombLocations[2] == x + 1 && bombLocations[3] == y - 1)
                || (bombLocations[4] == x + 1 && bombLocations[5] == y - 1) || (bombLocations[6] == x + 1 && bombLocations[7] == y - 1)) {
            bombCounter++;
        }
        if ((bombLocations[0] == x - 1 && bombLocations[1] == y) || (bombLocations[2] == x - 1 && bombLocations[3] == y)
                || (bombLocations[4] == x - 1 && bombLocations[5] == y) || (bombLocations[6] == x - 1 && bombLocations[7] == y)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x - 1 && bombLocations[1] == y + 1) || (bombLocations[2] == x - 1 && bombLocations[3] == y + 1)
                || (bombLocations[4] == x - 1 && bombLocations[5] == y + 1) || (bombLocations[6] == x - 1 && bombLocations[7] == y + 1)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x + 1 && bombLocations[1] == y + 1) || (bombLocations[2] == x + 1 && bombLocations[3] == y + 1)
                || (bombLocations[4] == x + 1 && bombLocations[5] == y + 1) || (bombLocations[6] == x + 1 && bombLocations[7] == y + 1)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x + 1 && bombLocations[1] == y) || (bombLocations[2] == x + 1 && bombLocations[3] == y)
                || (bombLocations[4] == x + 1 && bombLocations[5] == y) || (bombLocations[6] == x + 1 && bombLocations[7] == y)) {
            bombCounter++;
        }

        if ((bombLocations[0] == x && bombLocations[1] == y + 1) || (bombLocations[2] == x && bombLocations[3] == y + 1)
                || (bombLocations[4] == x && bombLocations[5] == y + 1) || (bombLocations[6] == x && bombLocations[7] == y + 1)) {
            bombCounter++;
        }

        if (bombCounter != 0) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(bombCounter));
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setBackgroundColor(Color.GRAY);
            tv.setTextColor(Color.RED);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(5, 5, 5, 5);

            lp.rowSpec = GridLayout.spec(x, 1f);

            lp.columnSpec = GridLayout.spec(y, 1f);

            GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);

            grid.addView(tv, lp);
            visited.add(String.valueOf(x) + String.valueOf(y));
        } else {
            visited.add(String.valueOf(x) + String.valueOf(y));
            TextView tv = new TextView(this);
            tv.setBackgroundColor(Color.GRAY);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(5, 5, 5, 5);

            lp.rowSpec = GridLayout.spec(x, 1f);

            lp.columnSpec = GridLayout.spec(y, 1f);

            GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);

            grid.addView(tv, lp);

            while (!visited.contains(String.valueOf(x + 1) + String.valueOf(y + 1)) && x + 1 < 10 && y + 1 < 8 && clearMine(x + 1, y + 1) == 0){
            }

            while (!visited.contains(String.valueOf(x + 1) + String.valueOf(y)) && x + 1 < 10 && y < 8 && clearMine(x + 1, y) == 0){
            }

            while (!visited.contains(String.valueOf(x + 1) + String.valueOf(y - 1)) && x + 1 < 10 && y - 1 >= 0 && clearMine(x + 1, y - 1) == 0){
            }

            while (!visited.contains(String.valueOf(x - 1) + String.valueOf(y - 1)) && x - 1 >= 0 && y - 1 >= 0 && clearMine(x - 1, y - 1) == 0){
            }

            while (!visited.contains(String.valueOf(x - 1) + String.valueOf(y + 1)) && x - 1 >= 0 && y + 1 < 8 && clearMine(x - 1, y + 1) == 0){
            }

            while (!visited.contains(String.valueOf(x - 1) + String.valueOf(y)) && x - 1 >= 0 && y < 8 && clearMine(x - 1, y) == 0){
            }

            while (!visited.contains(String.valueOf(x) + String.valueOf(y + 1)) && x < 10 && y + 1 < 8 && clearMine(x, y + 1) == 0){
            }

            while (!visited.contains(String.valueOf(x) + String.valueOf(y - 1)) && x < 10 && y - 1 >= 0 && clearMine(x, y - 1) == 0){
            }
        }

        return bombCounter;
    }

    public void onClickStartTimer() {
        running = true;
    }

    public void onClickStopTimer() {
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