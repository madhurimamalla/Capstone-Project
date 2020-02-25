package mmalla.android.com.whatnext;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mmalla.android.com.whatnext.login.EmailPasswordActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, EmailPasswordActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
