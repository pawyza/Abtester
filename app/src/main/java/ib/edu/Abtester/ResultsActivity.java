package ib.edu.Abtester;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ib.edu.Abtester.R;

public class ResultsActivity extends Activity {

    private ArrayList<String> drawingsFilePath;
    private ArrayList<String> images;
    private ArrayList<float[]> xPositionsList;
    private ArrayList<float[]> yPositionsList;
    private ArrayList<long[]> timeList;
    private ArrayList<boolean[]> stateList;
    private ArrayList<TextView>  linesCountTextViewArray;
    private ArrayList<TextView>  linesSpeedTextViewArray;
    private ArrayList<TextView>  singleLineTextViewArray;
    private ArrayList<TextView>  betweenTimeTextViewArray;
    private ArrayList<TextView>  totalTimeTextViewArray;
    private ArrayList<TextView>  similarityTextViewArray;
    private String profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        findTextViews();
        loadData();
        calculateAndSetup();
    }

    private void loadData(){
        profileName = getIntent().getStringExtra("profilName");
        drawingsFilePath = getIntent().getStringArrayListExtra("drawingsFilePath");
        images = getIntent().getStringArrayListExtra("imagesNameList");
        xPositionsList = new ArrayList<>();
        yPositionsList = new ArrayList<>();
        timeList = new ArrayList<>();
        stateList = new ArrayList<>();

        int t = drawingsFilePath.size();

        for(int i = 0; i<t;i++){
            xPositionsList.add(getIntent().getFloatArrayExtra("xPositions"+String.valueOf(i)));
            yPositionsList.add(getIntent().getFloatArrayExtra("yPositions"+String.valueOf(i)));
            timeList.add(getIntent().getLongArrayExtra("time"+String.valueOf(i)));
            stateList.add(getIntent().getBooleanArrayExtra("states"+String.valueOf(i)));
        }
    }

    private void calculateAndSetup(){
        int t = drawingsFilePath.size();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        StringBuilder dataToSave = new StringBuilder("Test " + profileName + " " + formattedDate + "\n");

        int totalLinesCount = 0;
        float totalTime = 0;
        float totalLinesTime = 0;
        float totalSpaceTime = 0;
        float averageSpeed = 0;
        for(int i = 0; i<t;i++) {
            int linesCount = calcLinesCount(stateList.get(i));
            float testTime = calcTotalTime(timeList.get(i)); //calkowity czas rysowania
            float lineTime = calcLineTime(testTime,linesCount);  //czas calkowity podzielony przez ilosc linii
            float spaceTime = calcSpaceTime(timeList.get(i),linesCount); //czas między liniami
            float averageTime = calcAverageSpaceTime(spaceTime,linesCount); //czas przerw na kazda linie
            float handSpeed = calcHandSpeed(xPositionsList.get(i), yPositionsList.get(i), testTime, spaceTime); // predkosc dloni

            totalSpaceTime += spaceTime;
            totalLinesCount += linesCount;
            averageSpeed += handSpeed;
            totalTime += lineTime;

            dataToSave.append("Test: "  + images.get(i) + "\n");
            dataToSave.append("LinesCount: " + Integer.toString(linesCount) + "\n");
            dataToSave.append("HandSpeed: " + String.format("%.5g%n", handSpeed) + "mm/ms");
            dataToSave.append("TimeForSingleLine: " + String.format("%.5g%n", lineTime) + "ms");
            dataToSave.append("TimeSpaceBetweenLines: " + String.format("%.5g%n", averageTime) + "ms");
            dataToSave.append("TotalTime: " + String.format("%.5g%n", testTime) + "ms");

            linesCountTextViewArray.get(i).setText(Integer.toString(linesCount));
            linesSpeedTextViewArray.get(i).setText(String.format("%.5g%n", handSpeed) + "\nmm/ms");
            singleLineTextViewArray.get(i).setText(String.format("%.5g%n", lineTime) + "ms");
            betweenTimeTextViewArray.get(i).setText(String.format("%.5g%n", averageTime) + "ms");
            totalTimeTextViewArray.get(i).setText(String.format("%.5g%n", testTime) + "ms");
            //similarityTextViewArray.get(i).setText(Float.toString(lineTime) + "ms");
        }
        totalSpaceTime = totalSpaceTime/t;
        totalLinesTime = calcLineTime(totalTime,totalLinesCount);
        averageSpeed = averageSpeed/t;

        dataToSave.append("Tests average:\n");
        dataToSave.append("LinesCount: " + Integer.toString(totalLinesCount) + "\n");
        dataToSave.append("HandSpeed: " + String.format("%.5g%n", averageSpeed) + "mm/ms");
        dataToSave.append("TimeForSingleLine: " + String.format("%.5g%n", totalLinesTime) + "ms");
        dataToSave.append("TimeSpaceBetweenLines: " + String.format("%.5g%n", totalSpaceTime) + "ms");
        dataToSave.append("TotalTime: " + String.format("%.5g%n", totalTime) + "ms");

        linesCountTextViewArray.get(t).setText(Integer.toString(totalLinesCount));
        linesSpeedTextViewArray.get(t).setText(String.format("%.5g%n", averageSpeed) + "\nmm/ms");
        singleLineTextViewArray.get(t).setText(String.format("%.5g%n", totalLinesTime) + "ms");
        betweenTimeTextViewArray.get(t).setText(String.format("%.5g%n", totalSpaceTime) + "ms");
        totalTimeTextViewArray.get(t).setText(String.format("%.5g%n", totalTime) + "ms");

        saveToFile(dataToSave.toString(),profileName + formattedDate);
    }

    private void findTextViews(){
        linesCountTextViewArray = new ArrayList<>();
        linesSpeedTextViewArray = new ArrayList<>();
        singleLineTextViewArray = new ArrayList<>();
        betweenTimeTextViewArray = new ArrayList<>();
        totalTimeTextViewArray = new ArrayList<>();
        similarityTextViewArray = new ArrayList<>();

        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT1));
        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT2));
        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT3));
        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT4));
        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT5));
        linesCountTextViewArray.add((TextView) findViewById(R.id.linesCountT6));

        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT1));
        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT2));
        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT3));
        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT4));
        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT5));
        linesSpeedTextViewArray.add((TextView) findViewById(R.id.linesSpeedT6));

        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT1));
        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT2));
        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT3));
        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT4));
        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT5));
        singleLineTextViewArray.add((TextView) findViewById(R.id.singleLineTimeT6));

        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT1));
        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT2));
        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT3));
        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT4));
        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT5));
        betweenTimeTextViewArray.add((TextView) findViewById(R.id.betweenTimeT6));

        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT1));
        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT2));
        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT3));
        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT4));
        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT5));
        totalTimeTextViewArray.add((TextView) findViewById(R.id.totalTimeT6));

        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT1));
        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT2));
        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT3));
        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT4));
        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT5));
        similarityTextViewArray.add((TextView) findViewById(R.id.similarityT6));
    }

    private float calcSpaceTime(long[] time, int linesCount) {
        if(linesCount != 0) {
            float spaceTime = 0;
            for (int i = 0; i < linesCount - 1; i++) {
                spaceTime = +(float) TimeUnit.NANOSECONDS.toMillis(time[i * 2 + 2] - time[i * 2 + 1]);
            }
            return spaceTime;
        } else {
            return 0;
        }
    }

    private float calcHandSpeed(float[] xPositions, float[] yPositions, float totalTime, float spaceTime) {
        if(xPositions.length != 0 && spaceTime != 0) {
            final float drawingTime = totalTime - spaceTime;
            final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

            float pathLength = 0;
            for (int i = 0; i < xPositions.length - 1; i++) {
                pathLength += Math.pow((Math.pow((xPositions[i + 1] - xPositions[i]), 2) + Math.pow((yPositions[i + 1] - yPositions[i]), 2)), 0.5);
            }
            Log.i("Path px", Float.toString(pathLength));
            pathLength = pathLength / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, displayMetrics);
            Log.i("Path mm", Float.toString(pathLength));
            return pathLength / drawingTime;
        } else{
            return 0;
        }
    }

    private int calcLinesCount(boolean[] states) {
        int lines = 0;
        for (boolean bool : states){
            if(states[0] != bool)
                lines++;
        }
    return lines;
    }

    private float calcTotalTime(long[] time) {
        if(time.length != 0) {
            long totalTimeNano = time[time.length - 1] - time[0];
            return (float) TimeUnit.NANOSECONDS.toMillis(totalTimeNano);
        } else
            return 0;
    }

    private float calcLineTime(float totalTime, int linesCount) {
        if(linesCount != 0) {
            return (float) totalTime / linesCount;
        } else{
            return 0;
        }
    }

    private float calcAverageSpaceTime(float spaceTime, int linesCount) {
        if(linesCount != 0) {
            return (float) spaceTime / linesCount;
        } else{
            return 0;
        }
    }

    public void onButtonShowGraph(View view) {
    }

    private void saveToFile(String data, String fileName){
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/"), fileName + ".txt");
        Log.i("PathInfo", "Location: " + file.getAbsolutePath());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file,true)){
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
        } catch (IOException |Resources.NotFoundException e) {
            e.printStackTrace();
        }
        drawingsFilePath.add(file.getAbsolutePath());
    }
}
