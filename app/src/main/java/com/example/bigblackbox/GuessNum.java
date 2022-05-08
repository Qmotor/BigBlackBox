package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuessNum extends AppCompatActivity {
    public static int count = 0;
    public static int guessNum;
    public static int postNum;
    public static boolean flag = true;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_num);
        final TextView tip, numCount;
        final EditText inputNum = findViewById(R.id.enterNum);;
        final Button btn,reset, cheat;

        reset = findViewById(R.id.reSet);
        btn = findViewById(R.id.enterBtn);
        cheat = findViewById(R.id.cheat);
        tip = findViewById(R.id.numTip);
        numCount = findViewById(R.id.num);

        postNum = getIntent().getIntExtra("numRange",-1);

        if(postNum == 100){
            tip.setText("您选择了“简单”难度（范围1~100）");
        }else if(postNum == 1000){
            tip.setText("您选择了“中等”难度（范围1~1000）");
        }else if(postNum == 10000){
            tip.setText("您选择了“困难”难度（范围1~10000）");
        }else {
            tip.setText("您选择了“自定义”难度（范围1~"+ postNum +"）");
        }

        if(count < 9){
            cheat.setVisibility(View.GONE);
        }

        if(flag){
            reset.setVisibility(View.GONE);
        }

        Random rand = new Random();
        guessNum = rand.nextInt(postNum) + 1;

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(count >= 9){
                    cheat.setVisibility(View.VISIBLE);
                }
                if(String.valueOf(inputNum.getText()).trim().equals("")){
                    tip.setText("什么都没有又怎么猜呢^^");
                }else {
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(String.valueOf(inputNum.getText()).trim());
                    if (m.matches()) {
                        int a = Integer.parseInt(String.valueOf(inputNum.getText()).trim());
                        if (a > guessNum) {
                            tip.setText("输入的" + a + "太大了");
                            count++;
                            numCount.setText(String.valueOf(count));
                        } else if (a < guessNum) {
                            tip.setText("输入的" + a + "太小了");
                            count++;
                            numCount.setText(String.valueOf(count));
                        } else {
                            tip.setText("恭喜你猜对啦！正确答案是" + a);
                            btn.setVisibility(View.GONE);
                            reset.setVisibility(View.VISIBLE);
                            count++;
                            numCount.setText(String.valueOf(count));
                        }
                    } else {
                        tip.setText("不是整数又怎么猜呢^^");
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                guessNum = rand.nextInt(postNum) + 1;
                inputNum.setText("");
                if(postNum == 100){
                    tip.setText("您选择了“简单”难度（范围1~100）");
                }else if(postNum == 1000){
                    tip.setText("您选择了“中等”难度（范围1~1000）");
                }else if(postNum == 10000){
                    tip.setText("您选择了“困难”难度（范围1~10000）");
                }else {
                    tip.setText("您选择了“自定义”难度（范围1~"+ postNum +"）");
                }
                numCount.setText("0");
                count = 0;
                btn.setVisibility(View.VISIBLE);
                reset.setVisibility(View.GONE);
                cheat.setVisibility(View.GONE);
            }
        });

        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip.setText("悄悄告诉你，答案是 " + guessNum);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
    }
}
