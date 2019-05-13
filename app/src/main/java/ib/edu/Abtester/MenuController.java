package ib.edu.Abtester;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuController extends AppCompatActivity {
        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
    }

    public void onButtonStart(View view) {
        Intent intent = new Intent(view.getContext(), PretestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonStatistics(View view) {
        Intent intent = new Intent(view.getContext(), StatisticsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonInstruction(View view) {
        Intent intent = new Intent(view.getContext(), InstructionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonLogout(View view) {
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
