package ib.edu.Abtester.threadObject;

import android.util.Log;

import ib.edu.Abtester.PaintingActivity;

public class TimerThread extends Thread {

    private int seconds;
    private PaintingActivity paintingActivity;

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setPaintingActivity(PaintingActivity paintingActivity) {
        this.paintingActivity = paintingActivity;
    }

    @Override
    public void run(){
        for(int i = seconds; i >= 0;i--){
            Log.i("TimerThread", String.valueOf(i));
            paintingActivity.setTimer(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        paintingActivity.changeSetup();
    }
}
