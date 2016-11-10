package com.example.admin123.smsams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.admin123.smsams.activity.LoginActivity;
import com.wang.avi.AVLoadingIndicatorView;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final AVLoadingIndicatorView splash_screen_anim = (AVLoadingIndicatorView) findViewById(R.id.avi);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_splash_screen);

        splash_screen_anim.startAnimation(an);

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                SplashScreen.this.startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
