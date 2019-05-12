package ib.edu.Abtester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class InstructionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        ImageView exampleImage = (ImageView) findViewById(R.id.imageExampleTest);
        int image = getResources().getIdentifier("@drawable/test223",null,this.getPackageName());
        exampleImage.setImageResource(image);
        ImageView exampleDrawing = (ImageView) findViewById(R.id.imageExampleResult);
        int drawing = getResources().getIdentifier("@drawable/exampledrawing",null,this.getPackageName());
        exampleDrawing.setImageResource(drawing);

    }

    public void onButtonReturn(View view) {
        Intent intent = new Intent(view.getContext(), MenuController.class);
        startActivity(intent);
    }
}
