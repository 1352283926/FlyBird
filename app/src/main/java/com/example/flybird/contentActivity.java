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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class contentActivity extends AppCompatActivity {

    int points;
    boolean iswaste;

    int screen_height;
    int screen_width;

    float qiu_size = 16;
    float qiu_downspeed = 3.5f;
    float qiu_upspeed = 90;
    float qiu_x = 200;
    float qiu_y;

    float zhu_height1;
    float zhu_height2;
    float zhu_width1;
    float zhu_width2;
    float zhu_x1;
    float zhu_x2;
    float zhu_y1;
    float zhu_y2;
    float zhu_xspeed = 5;

    GameView gameView;
    Timer timer;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_content);
        //绑定创建的游戏视图
        gameView = new GameView(this);
        setContentView(gameView);

        //获取窗口管理器
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //获取屏幕宽 高
        screen_height = metrics.heightPixels;
        screen_width = metrics.widthPixels;

        //开局
        play();
    }

    //游戏开局
    @SuppressLint("ClickableViewAccessibility")
    public void play() {
        iswaste = false;

        //柱子初始位置 长宽   [x,y]  x+Math.random()*y%(y-x+1)   [0,screen_height - 200]  [100,500]
        zhu_height1 = (float) (Math.random() * (screen_height - 200) % (screen_height - 199));
        zhu_width1 = 100 + (float) (Math.random() * 500 % 401);
        zhu_x1 = screen_width;
        zhu_y1 = 0;

        zhu_height2 = screen_height - zhu_height1 - 200;
        zhu_width2 = zhu_width1;
        zhu_x2 = screen_width;
        zhu_y2 = screen_height;

        //球初始y位置
        qiu_y = screen_height >> 1;

        //初始分数
        points = 0;

        //初始分数标识
        flag = true;

        //设置监听手势
        gameView.setOnTouchListener(touchListener);

        //设置标识，启动绘制初始画面
        handler.sendEmptyMessage(666);

        //之后的画面绘制
        //定时器，启动绘制
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //球与柱的位置变化
                qiu_y = qiu_y + qiu_downspeed;
                zhu_x1 = zhu_x1 - zhu_xspeed;
                zhu_x2 = zhu_x2 - zhu_xspeed;

                //球碰撞柱子
                if (qiu_x >= zhu_x1 && qiu_x <= zhu_x1 + zhu_width1) {
                    if (qiu_y < zhu_height1 || qiu_y > zhu_height1 + 200) {
                        iswaste = true;
                        timer.cancel();
                    }
                }

                //球碰撞上下边缘
                if (qiu_y >= screen_height || qiu_y <= 0) {
                    iswaste = true;
                    timer.cancel();
                }

                //积分判断
                if (flag) {
                    if (qiu_x > zhu_x1 + zhu_width1) {
                        points++;
                        flag = false;
                    }
                }

                //柱子右侧移出左屏幕时，柱子回档
                if (zhu_x1 + zhu_width1 <= 0) {
                    //柱子回档到最右边，作为下一个柱子
                    zhu_x1 = screen_width;
                    zhu_x2 = screen_width;

                    //随机下一个柱子的长 宽
                    zhu_height1 = (float) (Math.random() * (screen_height - 200) % (screen_height - 199));
                    zhu_width1 = 100 + (float) (Math.random() * 500 % 401);

                    zhu_height2 = screen_height - zhu_height1 - 200;
                    zhu_width2 = zhu_width1;

                    flag = true;

                }

                //变化后重绘
                handler.sendEmptyMessage(666);
            }
        }, 0, 15);

    }

    //手势监听
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                qiu_y = qiu_y - qiu_upspeed;
                handler.sendEmptyMessage(666);
            }
            return true;
        }
    };

    //判断标识启动绘制
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                gameView.invalidate();
            }
        }
    };

    /*//监视器
    class ButtonListen implements View.OnClickListener {
        public void onClick(View v) {
            int id = v.getId();
            switch(id) {
                case R.id.button:
                    setContentView(gameView);
                    Toast.makeText(contentActivity.this,"点了开始",Toast.LENGTH_LONG).show();
                    break;

            }

        }
    }*/

    //画板
    class GameView extends View {
        //创建画笔
        Paint paint = new Paint();

        public GameView(Context context) {
            super(context);

        }

        //画布
        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //画笔属性
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);//抗锯齿

            //绘制操作
            if (iswaste) {

                paint.setColor(Color.RED);
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("游戏结束，你获得了" + points + "分", screen_width >> 1, (screen_height >> 1) + 40, paint);

                //任意位置重新开始
                this.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Intent intent=new Intent(contentActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

            }
            else {
                //画球
                canvas.drawCircle(qiu_x, qiu_y, qiu_size, paint);

                //柱子
                paint.setColor(Color.BLUE);
                canvas.drawRect(zhu_x1, zhu_y1, zhu_width1 + zhu_x1, zhu_height1 + zhu_y1, paint);
                canvas.drawRect(zhu_x2, zhu_y2 - zhu_height2, zhu_width2 + zhu_x2, zhu_height2 + zhu_y2, paint);

                //画分数
                paint.setColor(Color.RED);
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(points + "", screen_width >> 1, 80, paint);
            }
        }
    }
}
