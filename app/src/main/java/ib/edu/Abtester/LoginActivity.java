package ib.edu.Abtester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void onButtonLogin(View view) {
        Intent intent = new Intent(view.getContext(), MenuController.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName","Tester");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonGoToRegister(View view) {
        Intent intent = new Intent(view.getContext(), RegisterActivity.class);
        startActivity(intent);
    }
}