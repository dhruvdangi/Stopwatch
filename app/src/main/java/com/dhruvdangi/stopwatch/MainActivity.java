package com.dhruvdangi.stopwatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final int ONE_MINUTE_IN_MS = 60 * 1000;

    private int mElapsedTime = 0;
    private boolean running = false;
    private final Handler handler = new Handler();

    private Context mContext;
    private LinearLayout mLayout;
    private TextView mClock;
    private FloatingActionButton mFab, mReset;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = MainActivity.this;
        mLayout = (LinearLayout) findViewById(R.id.layout);

        mClock = (TextView) findViewById(R.id.clock);
        mReset = (FloatingActionButton) findViewById(R.id.reset);
        mFab = (FloatingActionButton) findViewById(R.id.play);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Regular.ttf");
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        mClock.setText(String.format(getString(R.string.time_format), "00", "00"));
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.play));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) stop();
                else start();
            }
        });
        mClock.setTypeface(mTypeface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_credits:
                Snackbar.make(mLayout, "Made by Dhruv, Bharat, Ankit, Kirti", Snackbar.LENGTH_LONG)
                        .setAction("DISMISS", null).
                        show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void start() {
        running = true;
        mReset.setVisibility(View.VISIBLE);
        final Runnable runnable = new Runnable() {
            public void run() {
                mElapsedTime += 1000;
                setTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pause));
    }
    private void stop() {
        running = false;
        handler.removeCallbacksAndMessages(null);
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.play));
    }

    private void reset() {
        stop();
        startRipple();
    }

    private void startRipple() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
            Drawable d = coordinatorLayout.getBackground();
            final RippleDrawable rippleDrawable = (RippleDrawable) d;
            rippleDrawable.setHotspot(mReset.getX() + mReset.getWidth() / 2, mReset.getY() + mReset.getHeight() / 2);
            rippleDrawable.setState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled});
            Runnable stopRipple = new Runnable() {
                @Override
                public void run() {
                    coordinatorLayout.getBackground().setState(new int[]{});
                    mReset.setVisibility(View.GONE);
                    mElapsedTime = 0;
                    setTime();
                }
            };
            Handler h = new Handler();
            h.postDelayed(stopRipple, 900);
        } else mReset.setVisibility(View.GONE);
    }
    private void setTime() {
        String one, two;
        if (mElapsedTime < ONE_MINUTE_IN_MS) {
            one = String.valueOf((mElapsedTime % 1000) / 1000);
            two = String.valueOf(mElapsedTime / 1000);
        } else {
            one = String.valueOf(mElapsedTime / ONE_MINUTE_IN_MS);
            two = String.valueOf((mElapsedTime % ONE_MINUTE_IN_MS) / 1000);
        }

        one = (one.length() == 1 ? "0" : "") + one;
        two = (two.length() == 1 ? "0" : "") + two;

        mClock.setText(String.format(getString(R.string.time_format), one, two));
    }
}
