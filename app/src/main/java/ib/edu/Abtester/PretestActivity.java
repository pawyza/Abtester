package ib.edu.Abtester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class PretestActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);
    }

    public void onButtonContinue(View view) {
        Intent intent = new Intent(view.getContext(), PaintingActivity.class);
        ArrayList<String> imagesNameList = new ArrayList<>();
        ArrayList<String>  drawingsNameList = new ArrayList<>();
        ArrayList<Integer> imagesIdentifiers;
        Random random = new Random();
        for(int i = 1;i<6;i++){
            int id;
            imagesIdentifiers = new ArrayList<>();
            for(int j = 1;(id = getResources().getIdentifier("@drawable/test" + String.valueOf(i) + String.valueOf(j),null,this.getPackageName())) != 0;j++) {
                imagesIdentifiers.add(id);
                drawingsNameList.add("test" + String.valueOf(i) + String.valueOf(j) + "drawing");
            }
            id = imagesIdentifiers.get(random.nextInt(imagesIdentifiers.size()));
            imagesNameList.add(getResources().getResourceName(id));
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imagesNameList",imagesNameList);
        bundle.putStringArrayList("drawingsNameList",drawingsNameList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonInstruction(View view) {
        Intent intent = new Intent(view.getContext(), InstructionActivity.class);
        startActivity(intent);
    }

    public void onButtonReturn(View view) {
        Intent intent = new Intent(view.getContext(), MenuController.class);
        startActivity(intent);
    }
}