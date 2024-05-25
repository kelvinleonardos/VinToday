package com.example.vintoday.utils;

import android.os.Handler;

public class Debouncer {
    private final Handler handler = new Handler();
    private final int delay;
    private Runnable runnable;

    public Debouncer(int delay) {
        this.delay = delay;
    }

    public void debounce(final Runnable r) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                r.run();
            }
        };
        handler.postDelayed(runnable, delay);
    }
}