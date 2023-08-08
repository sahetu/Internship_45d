package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        imageView = findViewById(R.id.splash_image);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(2500);
        //animation.setRepeatCount(4);
        imageView.startAnimation(animation);
        //imageView.setImage

        Glide.with(SplashActivity.this).asGif().load("https://cdn.pixabay.com/animation/2022/08/01/20/42/20-42-37-53_512.gif").into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString(ConstantSp.ID,"").equals("")) {
                    new CommonMethod(SplashActivity.this, MainActivity.class);
                }
                else{
                    new CommonMethod(SplashActivity.this, DashboardActivity.class);
                }
            }
        },3000);

    }
}