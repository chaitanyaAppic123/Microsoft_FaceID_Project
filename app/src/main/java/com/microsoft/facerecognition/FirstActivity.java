package com.microsoft.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skyfishjy.library.RippleBackground;

public class FirstActivity extends AppCompatActivity{
TextView tvGetStarted, tvGetDatabase;
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        tvGetStarted=findViewById(R.id.tv_get_started);
        tvGetDatabase=findViewById(R.id.tv_get_data);
        rippleBackground = findViewById(R.id.ripple_effect);
        rippleBackground.startRippleAnimation();

        tvGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBackground.stopRippleAnimation();
                Intent intent =new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        tvGetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleBackground.stopRippleAnimation();
                Intent intent =new Intent(FirstActivity.this,ViewDataActivity.class);
                startActivity(intent);
            }
        });
    }



}
