package ib.edu.Abtester.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


import ib.edu.Abtester.interfaces.Observable;
import ib.edu.Abtester.interfaces.Observer;

public class PaintingView extends View implements Observable {

    private Path path = new Path();
    private Paint brush = new Paint();
    private Bitmap bitmap;
    private Canvas canvas;
    private float xPosition;
    private float yPosition;
    private long currentTime;
    private boolean inputType;
    private boolean changed;
    private ArrayList<Observer> observersList = new ArrayList<>();

    public PaintingView(Context context) {
        super(context);
        init(null);
    }

    public PaintingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PaintingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(@Nullable AttributeSet attrs) {
        brush.setAntiAlias(true);
        brush.setColor(Color.BLACK);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.BEVEL);
        brush.setStrokeWidth(30f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("Size", " h :" + h + " w :" + w);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        createPathHistory(event.getX(),event.getY());
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY); //rozpoczecie rysowania tutaj trzeba zrobic metode ktore wysyla pomiar czasu do activity, gdzie jest podawany do jakiegos kontenera
                createEventStats(System.nanoTime(),true);
                notifyObservers();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX,pointY);
                break;
            default://puszczenie
                createEventStats(System.nanoTime(),false);
                notifyObservers();
                return false;
        }
        postInvalidate();
        notifyObservers();
        canvas.drawPath(path,brush);
        return false;
    }
    private void createPathHistory(float x, float y){
        xPosition = x;
        yPosition = y;
    }

    private void createEventStats(long l, boolean b) {
        currentTime = l;
        inputType = b;
        changed = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, brush);
    }

    @Override
    public void addObserver(Observer observer) {
        observersList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observersList.remove(observer);
    }

    @Override
    public void deleteObservers() {
        observersList.clear();
    }

    @Override
    public void setChanged() {
        changed = false;
    }

    @Override
    public boolean checkIfChanged() {
        return changed;
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observersList){
            observer.update();
        }
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public boolean isInputType() {
        return inputType;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
