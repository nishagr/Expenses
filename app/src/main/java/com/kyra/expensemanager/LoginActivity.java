package com.kyra.expensemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    String username,password;
    DBManager dbManager;
    TextView tvSignUp,tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                intent.putExtra("type","user");
                startActivity(intent);
                finish();
            }
        });

    }



    void initView()
    {
        etUsername = findViewById(R.id.etLogin);

        etPassword = findViewById(R.id.etLoginPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgetPassword = findViewById(R.id.tvForgotPassword);
        dbManager = new DBManager(this);
        if(!checkIfAlreadyHavePermission()){
            requestForSpecificPermission();
        }
        etUsername.setText(dbManager.getUsername());
    }

    public void buLogin(View view) {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString();
        if(!username.isEmpty() && !password.isEmpty()){
            Cursor cursor = dbManager.getLoginUser(username);
//            Log.d("Cursor Empty", String.valueOf(!cursor.moveToFirst()));
            if (cursor.moveToFirst()){
                if (password.equals(cursor.getString(cursor.getColumnIndex(DBManager.colPassword)))){
                    Toast.makeText(this,"Logging In",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.putExtra("UserID",cursor.getString(cursor.getColumnIndex(DBManager.colID)));
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this,"Password Incorrect",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this,"No such user exists",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"One or more required fields are empty",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkIfAlreadyHavePermission() {
        int resultRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultWrite = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return resultRead == PackageManager.PERMISSION_GRANTED && resultWrite == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}