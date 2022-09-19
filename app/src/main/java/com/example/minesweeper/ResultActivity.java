package com.example.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Intent intent = getIntent();
        TextView tv = (TextView) findViewById(R.id.timeElapsed);
        tv.setText("Used " + intent.getStringExtra("time") + " seconds");

        TextView result = (TextView) findViewById(R.id.result);
        result.setText(intent.getStringExtra("result"));

        Button button = (Button) findViewById(R.id.replay);
        Intent replayIntent = new Intent(this, MainActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(replayIntent);
            }
        });
    }
}
