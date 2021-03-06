package com.example.bigblackbox;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bigblackbox.adapter.UserAdapter;
import com.example.bigblackbox.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuessNumChoose extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_num_choose);
        Button one, two, three, diy;
        one = findViewById(R.id.easy);
        two = findViewById(R.id.common);
        three = findViewById(R.id.hard);
        diy = findViewById(R.id.DIY);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuessNumChoose.this, GuessNum.class);
                intent.putExtra("numRange",100);
                startActivity(intent);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuessNumChoose.this, GuessNum.class);
                intent.putExtra("numRange",1000);
                startActivity(intent);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuessNumChoose.this, GuessNum.class);
                intent.putExtra("numRange",10000);
                startActivity(intent);
            }
        });

        diy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(GuessNumChoose.this);
                final LinearLayout linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.dialogname,null);
                final EditText albumName = linearLayout.findViewById(R.id.eUser);
                albumName.setHint("XXX??????????????????????????????");
                builder.setTitle("???????????????????????????????????????1~XXX???");
                builder.setView(linearLayout);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(String.valueOf(albumName.getText()).trim().equals("")){
                            Toast.makeText(GuessNumChoose.this, "??????????????????????????????^^", Toast.LENGTH_SHORT).show();
                        }else {
                            Pattern p = Pattern.compile("[0-9]*");
                            Matcher m = p.matcher(String.valueOf(albumName.getText()).trim());
                            if (m.matches()) {
                                int a = Integer.parseInt(String.valueOf(albumName.getText()).trim());
                                if(a <= 1){
                                    Toast.makeText(GuessNumChoose.this, "?????????????????????????????????2^^", Toast.LENGTH_SHORT).show();
                                }else {
                                    Intent intent = new Intent(GuessNumChoose.this, GuessNum.class);
                                    intent.putExtra("numRange",a);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(GuessNumChoose.this, "???????????????????????????^^", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("??????",null);
                builder.create().show();
            }
        });
    }

}
