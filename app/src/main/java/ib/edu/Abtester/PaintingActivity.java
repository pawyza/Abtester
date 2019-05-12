package ib.edu.Abtester;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ib.edu.Abtester.interfaces.Observer;
import ib.edu.Abtester.threadObject.TimerThread;
import ib.edu.Abtester.views.PaintingView;

public class PaintingActivity extends AppCompatActivity implements Observer {

    private PaintingView drawingScreen;
    private ImageView imageScreen;
    private TextView timer;
    private TextView timerInfo;
    private Button buttonTestEnd;
    private ArrayList<String> images;
    private ArrayList<String> drawings;
    private ArrayList<ArrayList<Float>> xArraysList;
    private ArrayList<Float> x;
    private ArrayList<ArrayList<Float>> yArraysList;
    private ArrayList<Float> y;
    private ArrayList<ArrayList<Long>> timeArraysList;
    private ArrayList<Long> time;
    private ArrayList<ArrayList<Boolean>> stateArraysList;
    private ArrayList<Boolean> state;
    private ArrayList<String> drawingsFilePath;
    private int c = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_screen);
        images = getIntent().getStringArrayListExtra("imagesNameList");
        drawings = getIntent().getStringArrayListExtra("drawingsNameList");
        drawingsFilePath = new ArrayList<>();
        xArraysList = new ArrayList<>();
        yArraysList = new ArrayList<>();
        timeArraysList = new ArrayList<>();
        stateArraysList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cycle(c);
    }

    private boolean cycle(int testsCount){
        if (testsCount < images.size()){
            setContentView(R.layout.activity_drawing_screen);
            findAll();
            drawingScreen.addObserver(this);
            x = new ArrayList<>();
            y = new ArrayList<>();
            time = new ArrayList<>();
            state = new ArrayList<>();
            prepareImageView(testsCount);
            try {
                startCountdown(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    public void onEndTestButtonClick(View view) {
        collectData(c);
        if(cycle(++c)) {
        prepareResults(view);
        }
    }

    private void collectData(int c){
        xArraysList.add(x);
        yArraysList.add(y);
        timeArraysList.add(time);
        stateArraysList.add(state);

        File file = new File(getApplicationContext().getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/"), drawings.get(c) + ".png");
        Log.i("PathInfo", "Location: " + file.getAbsolutePath());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            Bitmap bitmap = prepareBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        } catch (IOException |Resources.NotFoundException e) {
            e.printStackTrace();
        }
        drawingsFilePath.add(file.getAbsolutePath());
    }

    private void prepareResults(View view){
        Intent intent = new Intent(view.getContext(), ResultsActivity.class);
        Bundle bundle = new Bundle();
        for(int m = 0; m < xArraysList.size(); m++) {
            float[] xArray = new float[xArraysList.get(m).size()];
            float[] yArray = new float[yArraysList.get(m).size()];
            long[] timeArray = new long[timeArraysList.get(m).size()];
            boolean[] stateArray = new boolean[stateArraysList.get(m).size()];
            Log.d("Print",String.valueOf( xArraysList.get(m).size()));
            for (int i = 0; i < xArray.length; i++) {
                xArray[i] = xArraysList.get(m).get(i);
                yArray[i] = yArraysList.get(m).get(i);
            }
            for (int i = 0; i < timeArray.length; i++) {
                timeArray[i] = timeArraysList.get(m).get(i);
                stateArray[i] = stateArraysList.get(m).get(i);
            }
            bundle.putFloatArray( "xPositions"+String.valueOf(m),xArray);
            bundle.putFloatArray("yPositions"+String.valueOf(m),yArray);
            bundle.putLongArray("time"+String.valueOf(m),timeArray);
            bundle.putBooleanArray("states"+String.valueOf(m),stateArray);
        }
        bundle.putStringArrayList("imagesNameList",images);
        bundle.putStringArrayList("drawingsFilePath",drawingsFilePath);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Bitmap prepareBitmap() throws Resources.NotFoundException {
        Bitmap bitmap = drawingScreen.getBitmap();
        if (bitmap != null) {
            Log.i("Bitmap", ", found");
        }
        else {
            Log.i("Bitmap", ", not found");
            throw new Resources.NotFoundException();
        }
        return bitmap;
    }

    private void findAll(){
        imageScreen = (ImageView) findViewById(R.id.imageScreen);
        timer = (TextView) findViewById(R.id.timer);
        timerInfo = (TextView) findViewById(R.id.timerInfo);
        drawingScreen = (PaintingView) findViewById(R.id.drawingScreen);
        buttonTestEnd = (Button) findViewById(R.id.buttonTestEnd);
    }

    private void prepareImageView(int idNumber){
        Log.d("preparing",images.get(idNumber));
        int image = getResources().getIdentifier(images.get(idNumber),null,this.getPackageName());
        Log.d("preparing",String.valueOf(image));
        imageScreen.setImageResource(image);
    }

    private void startCountdown(int seconds) throws InterruptedException {
        TimerThread timerThread = new TimerThread();
        timerThread.setSeconds(seconds);
        timerThread.setPaintingActivity(this);
        timerThread.start();
    }

    public void setTimer(int second) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.setText(String.valueOf(second));
            }
        });
    }


    public void changeSetup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.setVisibility(View.INVISIBLE);
                timerInfo.setVisibility(View.INVISIBLE);
                imageScreen.setVisibility(View.INVISIBLE);
                drawingScreen.setVisibility(View.VISIBLE);
                buttonTestEnd.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void update() {
        x.add(drawingScreen.getxPosition());
        y.add(drawingScreen.getyPosition());
        if (drawingScreen.checkIfChanged()){
            state.add(drawingScreen.isInputType());
            time.add(drawingScreen.getCurrentTime());
            drawingScreen.setChanged();
        }
    }
}