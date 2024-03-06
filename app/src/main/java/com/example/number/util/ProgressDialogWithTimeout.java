package com.example.number.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;


public class ProgressDialogWithTimeout implements DefaultLifecycleObserver {
    private static final long INITIAL_DELAY = 25; // 25ms to prevent showing the loading indicator when data can be quickly loaded, for e.g. restored from cache
    private final Handler initialDelayHandler = new Handler(Looper.getMainLooper());
    private static final long TIMEOUT_FOR_AUTO_HIDE = 60 * 1000; // dismiss the loading indicator if it remain visible even after one minute
    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());

    private static final long MIN_DISPLAY_DURATION = 500; // half a sec as the min duration for which the loading indicator should be shown to prevent flashing
    private final Handler minDurationHandler = new Handler(Looper.getMainLooper());

    private static final long NOT_SHOWN_TS = -1;
    private long shownAtTS = NOT_SHOWN_TS; // timestamp at which it was last shown or attempted to be shown when already visible
    private final FragmentActivity activity;

    private ProgressBarDialog progressBarDialog;
    private boolean isShown = false;

    public ProgressDialogWithTimeout(@NonNull FragmentActivity activity) {
        this.activity = activity;
        this.activity.getLifecycle().addObserver(this);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        if (isShown) {
            // pause the loading indicator as the screen is being paused/backgrounded
            pause();
        }
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if (isShown) {
            // restore the loading indicator as the screen is being resume/foregrounded
            showImmediately();
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // Cancel all handlers
        initialDelayHandler.removeCallbacksAndMessages(null);
        timeoutHandler.removeCallbacksAndMessages(null);
        minDurationHandler.removeCallbacksAndMessages(null);
    }

    @MainThread
    public void show() {
        showAfterDelay(INITIAL_DELAY);
    }

    @MainThread
    public void showImmediately() {
        showAfterDelay(0);
    }

    @MainThread
    public void showAfterDelay(long initialDelay) {
        if (isShown) {
            // If already shown, reset timeout handler
            presentProgressDialog();
        } else {
            // present after the initial delay
            initialDelayHandler.postDelayed(this::presentProgressDialog, initialDelay);
        }
    }

    private void presentProgressDialog() {
        // update isShown so we can rely on it to restore the loading indicator on resume
        isShown = true;

        // cancel any previous timeout handler
        timeoutHandler.removeCallbacksAndMessages(null);
        // cancel any previous min duration handler
        minDurationHandler.removeCallbacksAndMessages(null);

        if (activity != null && !activity.getSupportFragmentManager().isStateSaved()) {
            // show the progressbar dialog
            if (progressBarDialog == null) {
                progressBarDialog = new ProgressBarDialog();
                progressBarDialog.show(activity.getSupportFragmentManager(), ProgressBarDialog.TAG);
            }
            // update the timestamp at which it was shown if it will be shown now, do not overwrite if already shows
            if (shownAtTS == NOT_SHOWN_TS) {
                shownAtTS = System.currentTimeMillis();
            }
            // schedule dismiss on timeout
            timeoutHandler.postDelayed(this::dismissProgressDialog, TIMEOUT_FOR_AUTO_HIDE);
        }
    }

    @MainThread
    public void dismissProgressDialog() {
        // update isShown so we know we don't need to restore the loading indicator on resume
        isShown = false;

        // cancel any previous handler that would attempt to show after a delay
        initialDelayHandler.removeCallbacksAndMessages(null);
        long currentTS = System.currentTimeMillis();
        if (shownAtTS != NOT_SHOWN_TS && currentTS - shownAtTS < MIN_DISPLAY_DURATION) {
            // Keep it running for some more time so it doesn't flash
            minDurationHandler.postDelayed(this::dismiss, MIN_DISPLAY_DURATION - (currentTS - shownAtTS));
        } else {
            // Can be dismissed completely since it has run for sufficient time to not just flash
            dismiss();
        }
    }

    @MainThread
    private void pause() {
        // cancel any previous handler that would attempt to show after a delay
        initialDelayHandler.removeCallbacksAndMessages(null);
        // dismiss the loading indicator
        dismiss();
    }

    private void dismiss() {
        if (activity != null && !activity.getSupportFragmentManager().isStateSaved()) {
            if (progressBarDialog != null) {
                progressBarDialog.dismiss();
                progressBarDialog = null;
                shownAtTS = NOT_SHOWN_TS;
            }
        }
    }
}