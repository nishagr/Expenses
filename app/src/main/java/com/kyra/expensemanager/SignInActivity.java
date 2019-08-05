package com.kyra.expensemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    EditText etUsername,etPassword,etConfirmPassword,etBankPassword,etSecurityAnswer;
    Spinner spinnerQuestions;
    DBManager dbManager;
    TextView tvLogin;
    int questionID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final List<String> questions = Arrays.asList(getResources().getStringArray(R.array.questions));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,questions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void initView()
    {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etBankPassword = findViewById(R.id.etBankPassword);
        spinnerQuestions = findViewById(R.id.spinnerQuestions);
        etSecurityAnswer = findViewById(R.id.etSecurityAnswer);
        dbManager = new DBManager(this);
        tvLogin = findViewById(R.id.tvLogin);
    }

    public void buSignUp(View view) {

        String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String bankPassword = etBankPassword.getText().toString();
        String securityAnswer = etSecurityAnswer.getText().toString();

        Cursor cursor = dbManager.getUserNames();
        ArrayList<String> usernames = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                usernames.add(cursor.getString(cursor.getColumnIndex(DBManager.colUser)));
            }while (cursor.moveToNext());
        }
        if(!username.isEmpty() && !password.isEmpty() && !securityAnswer.isEmpty() && password.equals(confirmPassword) && questionID>0){
            if (!usernames.contains(username)){
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBManager.colUser,username);
                contentValues.put(DBManager.colPassword,password);
                contentValues.put(DBManager.colSecurityQuestion,questionID);
                contentValues.put(DBManager.colSecurityAnswer,securityAnswer);
                if (!bankPassword.isEmpty()){
                    contentValues.put(DBManager.colBankPassword,bankPassword);
                }
//                Toast.makeText(getApplicationContext(), String.valueOf(questionID),Toast.LENGTH_LONG).show();
                String id = dbManager.insertUserData(contentValues);
//                Toast.makeText(this,"ID = " + id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("UserID",id);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this,"Username already taken",Toast.LENGTH_SHORT).show();
            }
        }
        else if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || !securityAnswer.isEmpty() || questionID<1){
            Toast.makeText(this,"One or More required fields Empty",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Passwords doesn't match",Toast.LENGTH_LONG).show();
        }
    }
}
