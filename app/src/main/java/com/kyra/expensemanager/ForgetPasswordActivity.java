package com.kyra.expensemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText etUsername,etSecurityAnswer;
    TextView tvSecurityQuestion,tvLogin;
    Button buForgetPassword,buGetUsername;

    DBManager dbManager;
    TextInputLayout tilSecurityAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        final Cursor[] cursor = new Cursor[1];
        final List<String> questions = Arrays.asList(getResources().getStringArray(R.array.questions));
        buGetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =  etUsername.getText().toString();
                cursor[0] = dbManager.forgetPassword(username);
                if (cursor[0].moveToFirst()){
                    tvSecurityQuestion.setText(questions.get(Integer.parseInt(cursor[0].getString(cursor[0].getColumnIndex(DBManager.colSecurityQuestion)))));
//                    Log.d("Security Ques",questions.get(Integer.parseInt(cursor[0].getString(cursor[0].getColumnIndex(DBManager.colSecurityQuestion)))));
                    tvSecurityQuestion.setVisibility(View.VISIBLE);
                    tilSecurityAnswer.setVisibility(View.VISIBLE);
                    buForgetPassword.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Username doesn't exist!",Toast.LENGTH_LONG).show();
                }
            }
        });
        buForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String securityAnswer = etSecurityAnswer.getText().toString();
                if (securityAnswer.equals(cursor[0].getString(cursor[0].getColumnIndex(DBManager.colSecurityAnswer)))){
                    callResetPasswordDialog(cursor[0]);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Answer",Toast.LENGTH_LONG).show();
                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView(){
        etUsername = findViewById(R.id.etUsername);
        tvSecurityQuestion = findViewById(R.id.tvSecurityQuestion);
        etSecurityAnswer = findViewById(R.id.etSecurityAnswer);
        tilSecurityAnswer = findViewById(R.id.tilSecurityAnswer);
        buGetUsername = findViewById(R.id.buGetUsername);
        buForgetPassword = findViewById(R.id.buForgetPassword);
        tvLogin = findViewById(R.id.tvLogin);
        dbManager = new DBManager(this);
    }

    private void callResetPasswordDialog(final Cursor cursor){

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final String[] type = new String[1];
        final Dialog resetPasswordDialog = new Dialog(this);
        resetPasswordDialog.setContentView(R.layout.dialog_reset_password);
        Objects.requireNonNull(resetPasswordDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(resetPasswordDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        resetPasswordDialog.show();
        cursor.moveToFirst();
        final String id = cursor.getString(cursor.getColumnIndex(DBManager.colID));
        final Spinner spinnerPasswordType = resetPasswordDialog.findViewById(R.id.spinnerPasswordType);
        final EditText etPassword = resetPasswordDialog.findViewById(R.id.etPassword);
        final EditText etConfirmPassword = resetPasswordDialog.findViewById(R.id.etConfirmPassword);
        spinnerPasswordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type[0] = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button buResetPassword = resetPasswordDialog.findViewById(R.id.buResetPassword);
        buResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                ContentValues contentValues = new ContentValues();
                long returnID;
//                Log.d("TYPE",type[0]);
                if(password.equals(confirmPassword)) {
                    if (type[0].equals("User")) {
                        contentValues.put(DBManager.colPassword,password);
                        returnID = dbManager.changePassword(contentValues, id);
                    } else if (type[0].equals("Bank")) {
                        contentValues.put(DBManager.colBankPassword,password);
                        returnID = dbManager.changeBankPassword(contentValues, id);
                    }
                    else {
                        returnID = 0;
                    }
                    if (returnID != 0) {
                        Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_LONG).show();
                        resetPasswordDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Not Changed", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Passwords doesn't match",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
