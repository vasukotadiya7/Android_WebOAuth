package com.vasukotadiya.exampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    TextView tvUid;
    Button btnLogout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        tvUid=findViewById(R.id.tv_uid);
        btnLogout=findViewById(R.id.btn_logout);
        SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null) {
            // Process the deep link data, if needed
//            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
            String userid1=sp.getString("userid","");
            tvUid.setText(userid1);
        }
        else{
//            Toast.makeText(this, "NOT NULL", Toast.LENGTH_SHORT).show();
            String[] accesstoken =(data.getQueryParameter("accesstoken")).split("@");
            tvUid.setText(accesstoken[0]);
            editor.putString("userid",accesstoken[0]);
            editor.putBoolean("isLoggedIn",true);
            editor.apply();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("isLoggedIn");
                editor.remove("userid");
                editor.apply();
                startActivity(new Intent(NewActivity.this,MainActivity.class));
            }
        });
    }
}