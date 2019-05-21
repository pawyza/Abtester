package ib.edu.Abtester;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    DatabaseReference refTests;
    StorageReference refTestsStorage;
    private String profileName;

    private int totalLinesCount = 0;
    private float totalTime = 0;
    private float totalLinesTime = 0;
    private float totalSpaceTime = 0;
    private float averageSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        findTextViews();
        loadData();
        refTests = FirebaseDatabase.getInstance().getReference("TestResults");
        refTestsStorage = FirebaseStorage.getInstance().getReference("TestsResultsImages");
        calculateAndSetup();
    }

    private void loadData(){
        profileName = getIntent().getStringExtra("profileName");
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

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        StringBuilder dataToSave = new StringBuilder("Test " + profileName + " " + formattedDate + "\n");
        DatabaseReference refProfile = refTests.child(profileName).child(formattedDate);
        StorageReference refProfileStorage = refTestsStorage.child(profileName).child(formattedDate);

        String formatter = "%.5g";
        
        for(int i = 0; i<t;i++) {
            int linesCount = calcLinesCount(stateList.get(i));
            float testTime = calcTotalTime(timeList.get(i)); //calkowity czas rysowania
            float lineTime = calcLineTime(testTime,linesCount);  //czas calkowity podzielony przez ilosc linii
            float spaceTime = calcSpaceTime(timeList.get(i),linesCount); //czas między liniami
            float averageTime = calcAverageSpaceTime(spaceTime,linesCount); //czas przerw na kazda linie
            float handSpeed = calcHandSpeed(xPositionsList.get(i), yPositionsList.get(i), testTime, spaceTime); // predkosc dloni

            uploadResult(i,refProfile,refProfileStorage);

            totalSpaceTime += spaceTime;
            totalLinesCount += linesCount;
            averageSpeed += handSpeed;
            totalTime += lineTime;

            int indexOfLast =images.get(i).lastIndexOf("/");
            dataToSave.append("Test: "  + images.get(i).substring(indexOfLast+1, images.get(i).length()) + "\n");
            dataToSave.append("LinesCount: " + Integer.toString(linesCount) + "\n");
            dataToSave.append("HandSpeed: " + String.format(formatter, handSpeed) + " mm/ms\n");
            dataToSave.append("TimeForSingleLine: " + String.format(formatter, lineTime) + " ms\n");
            dataToSave.append("TimeSpaceBetweenLines: " + String.format(formatter, averageTime) + " ms\n");
            dataToSave.append("TotalTime: " + String.format(formatter, testTime) + " ms\n");

            linesCountTextViewArray.get(i).setText(Integer.toString(linesCount));
            linesSpeedTextViewArray.get(i).setText(String.format(formatter, handSpeed) + "\nmm/ms");
            singleLineTextViewArray.get(i).setText(String.format(formatter, lineTime) + " ms");
            betweenTimeTextViewArray.get(i).setText(String.format(formatter, averageTime) + " ms");
            totalTimeTextViewArray.get(i).setText(String.format(formatter, testTime) + " ms");
            //similarityTextViewArray.get(i).setText(Float.toString(lineTime) + "ms");
        }
        totalSpaceTime = totalSpaceTime/t;
        totalLinesTime = calcLineTime(totalTime,totalLinesCount);
        averageSpeed = averageSpeed/t;

        dataToSave.append("Tests average:\n");
        dataToSave.append("LinesCount: " + Integer.toString(totalLinesCount) + "\n");
        dataToSave.append("HandSpeed: " + String.format(formatter, averageSpeed) + " mm/ms\n");
        dataToSave.append("TimeForSingleLine: " + String.format(formatter, totalLinesTime) + " ms\n");
        dataToSave.append("TimeSpaceBetweenLines: " + String.format(formatter, totalSpaceTime) + " ms\n");
        dataToSave.append("TotalTime: " + String.format(formatter, totalTime) + " ms\n");

        linesCountTextViewArray.get(t).setText(Integer.toString(totalLinesCount));
        linesSpeedTextViewArray.get(t).setText(String.format(formatter, averageSpeed) + "\nmm/ms");
        singleLineTextViewArray.get(t).setText(String.format(formatter, totalLinesTime) + " ms");
        betweenTimeTextViewArray.get(t).setText(String.format(formatter, totalSpaceTime) + " ms");
        totalTimeTextViewArray.get(t).setText(String.format(formatter, totalTime) + " ms");

        //saveToFile(dataToSave.toString(),profileName + formattedDate);
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
        Intent intent = new Intent(view.getContext(), BarGraphActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        bundle.putFloat("totalTime",totalTime);
        bundle.putFloat("totalSpaceTime",totalLinesTime);
        bundle.putFloat("totalLinesTime",totalSpaceTime);
        bundle.putFloat("averageSpeed",averageSpeed);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /*
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
    */



    private void uploadResult(int i,DatabaseReference refProfile, StorageReference refProfileStorage) {

        Date date = Calendar.getInstance().getTime();
        TestResult tr = new TestResult(profileName,
                images.get(i).substring(images.get(i).lastIndexOf("/")+1),
                date,
                toList(xPositionsList.get(i)),
                toList(yPositionsList.get(i)),
                toList(timeList.get(i)),
                toList(stateList.get(i)));

        try{
            refProfile.child(images.get(i).substring(images.get(i).lastIndexOf("/")+1)).setValue(tr);

            Toast.makeText(ResultsActivity.this, "Test " + String.valueOf(i) + " pushed.", Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(ResultsActivity.this, "Test " + String.valueOf(i) + " pushing failed.", Toast.LENGTH_LONG).show();
        }
        
        StorageReference refImage = refProfileStorage.child(images.get(i).substring(images.get(i).lastIndexOf("/")+1) + " Date: " + date.toString());
        Uri file = Uri.fromFile(new File(drawingsFilePath.get(i)));
        
        refImage.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ResultsActivity.this,"Test image " + String.valueOf(i) + " uploaded.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(ResultsActivity.this,"Test image " + String.valueOf(i) + " uploading failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private List<Long> toList(long[] arr){
        List<Long> list = new ArrayList<>();
        for(long f : arr){
            list.add(f);
        }
        return list;
    }

    private List<Boolean> toList(boolean[] arr){
        List<Boolean> list = new ArrayList<>();
        for(boolean f : arr){
            list.add(f);
        }
        return list;
    }

    private List<Float> toList(float[] arr){
        List<Float> list = new ArrayList<>();
        for(float f : arr){
            list.add(f);
        }
        return list;
    }
}
