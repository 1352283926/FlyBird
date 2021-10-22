package com.example.flybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(new ButtonListen());
    }



    //监视器
    class ButtonListen implements View.OnClickListener {
        public void onClick(View v) {
            int id = v.getId();
            switch(id) {
                case R.id.button:
                    Intent intent=new Intent(MainActivity.this,contentActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"点了开始",Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }




}

