package com.vasukotadiya.exampleapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnAuth;
    static public String responseData,weboauth_url,appid,redirect_url,accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        SharedPreferences sharedPreferences ;
        sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        ProgressDialog progressDialog = new ProgressDialog(this);
        btnAuth=findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Redirecting");
                progressDialog.setMessage("Please Wait ");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://weboauthapi.onrender.com/getdata", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.setMessage("Please Wait .");
                            appid=response.getString("_id");
                            weboauth_url = response.getString("weboauth_url");
                            redirect_url=response.getString("redirect_url");
                            accessToken=response.getString("accessToken");
                            progressDialog.setMessage("Please Wait ..");
//                            Toast.makeText(MainActivity.this, accessToken, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            progressDialog.setMessage("Please Wait ...");
                            String authweburl=weboauth_url+"/"+accessToken;
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(authweburl));
                progressDialog.setMessage("Please Wait ....");
                startActivity(intent);
                progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
//                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("_id", "s2fd56wff31");
                        params.put("redirect_url", "app://login");

                        return params;
                    }};
                requestQueue.add(jsonObjectRequest);

//                progressDialog.setMessage(responseData);
//                progressDialog.setMessage("Please Wait...");
//                String authweburl="https://weboauth.vasukotadiya.site/param1=app";
//                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(authweburl));
//                progressDialog.setMessage("Please Wait....");
//                startActivity(intent);
//                progressDialog.dismiss();
            }
        });
        if (sharedPreferences.getBoolean("isLoggedIn",false))
        {
            startActivity(new Intent(this,NewActivity.class));
        }
        else
        {
            Toast.makeText(this, sharedPreferences.toString(), Toast.LENGTH_SHORT).show();

        }
    }

}