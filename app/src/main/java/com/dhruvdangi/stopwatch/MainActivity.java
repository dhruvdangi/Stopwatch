package com.dhruvdangi.stopwatch;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mLayout;
    TextView mClock;
    int mTimestamp;
    Boolean running = false;
    final Handler handler = new Handler();
    Button mReset, mCredits;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (RelativeLayout) findViewById(R.id.layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mClock = (TextView) findViewById(R.id.clock);
        mClock.setText("0:0");
        mReset = (Button) findViewById(R.id.reset);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        mCredits = (Button) findViewById(R.id.credits);
        mCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout, "Dhruv, Bharat, Ankit, Kirti", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.play));
        fab.setRippleColor(Color.parseColor("#ffffff"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) stop(); else start();
            }
        });
    }

    private void start() {
        running = true;
        Log.d("Clock", "Started");
        final Runnable r = new Runnable() {
            public void run() {
                mTimestamp += 1;
                setTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }
    private void stop() {
        Log.d("Clock", "Stopped");
        running = false;
        handler.removeCallbacksAndMessages(null);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.play));

    }

    private void reset() {
        stop();
        mTimestamp = 0;
        setTime();
    }

    private void setTime() {
        int min = 0, sec = 0;
        while (mTimestamp > 60) {
            min++;
            mTimestamp -= 60;
        }
        sec = mTimestamp;
        mClock.setText(min + ":" + sec);
    }
}
