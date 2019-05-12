package ib.edu.Abtester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setStatistics();
    }

    private void setStatistics(){

    }

    public void onButtonReturn(View view) {
        Intent intent = new Intent(view.getContext(), MenuController.class);
        startActivity(intent);
    }
}
