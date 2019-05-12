package ib.edu.Abtester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
    }

    public void onButtonGoToLogin(View view) {
        //Noregister
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onButtonRegister(View view) {
        //Register
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }
}