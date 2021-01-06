package com.example.bigblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class reg_result extends AppCompatActivity {
    private TextView name,pwd,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_result);
        name = findViewById(R.id.Regname);
        pwd = findViewById(R.id.Regpwd);
        gender = findViewById(R.id.Reggender);
        Intent intent = getIntent();
        if(intent != null){
            name.setText(intent.getStringExtra("regname"));
            pwd.setText(intent.getStringExtra("regpwd"));
            gender.setText(intent.getStringExtra("reggender"));
        }
    }
}