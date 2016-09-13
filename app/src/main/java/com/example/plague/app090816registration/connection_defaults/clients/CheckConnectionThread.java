package com.example.plague.app090816registration.connection_defaults.clients;

import android.util.Log;

import com.example.plague.app090816registration.LogIn.activities.LogInActivity;

import java.io.IOException;

public class CheckConnectionThread extends ClientThread {
    public static final String TAG = "CheckConnectionThread";
    public boolean currentState = false;
    private Thread refreshConnectionState;

    public CheckConnectionThread(final LogInActivity activity){
        refreshConnectionState = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    if (currentState) {
                        activity.setConnectionON();
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "No Connection");
                        activity.setConnectionOFF();
                    }
                }
            }
        });
        refreshConnectionState.start();
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            super.run();
        }
        if(isInterrupted()) {
            refreshConnectionState.interrupt();
        }
    }

    @Override
    protected void write() throws IOException, ClassNotFoundException {
        output.flush();
        output.writeObject(true);
        output.flush();
    }

    @Override
    protected void read() throws IOException, ClassNotFoundException {
        currentState = false;
        input.readObject();
        currentState = true;
    }
}
