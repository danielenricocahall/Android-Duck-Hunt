package com.example.danie.ppd_final_project;

/**
 * Created by daniel on 12/19/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


//courtesy of StackOverflow, this class and it's corresponding listener
// are used to pause the game if the home button is pressed
public class HomeWatcher {

    private Context context;
    private IntentFilter filter;
    private OnHomePressedListener listener;
    private InnerRecevier recevier;

    public HomeWatcher(Context context) {
        this.context = context;
        filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        this.listener = listener;
        recevier = new InnerRecevier();
    }

    public void startWatch() {
        if (recevier != null) {
            context.registerReceiver(recevier, filter);
        }
    }

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (listener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            listener.onHomePressed();
                        }
                    }
                }
            }
        }
    }
}