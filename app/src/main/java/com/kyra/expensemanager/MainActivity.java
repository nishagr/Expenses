package com.kyra.expensemanager;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    DBManager dbManager;
    String id;
    CommonMethodArea commonMethodArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        id = bundle.getString("UserID");
        commonMethodArea = new CommonMethodArea(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Dashboard");
        dbManager = new DBManager(this);
        loadFragment(new DashboardFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    actionBar.setTitle("Dashboard");
                    fragment = new DashboardFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_calculator:
                    actionBar.setTitle("Calculator");
                    fragment = new CalculatorFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_home:
                    actionBar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_advanced:
                    actionBar.setTitle("Search");
                    fragment = new AdvancedFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    actionBar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.wrapper,fragment);
        transaction.commit();
    }

    public void buLogout(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        Toast.makeText(getApplicationContext(),"Logging Out",Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();
    }

    public void buChangePassword(View view) {

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog changePasswordDialog = new Dialog(this);
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        Objects.requireNonNull(changePasswordDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(changePasswordDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changePasswordDialog.show();
        final EditText etCurrentPassword = changePasswordDialog.findViewById(R.id.etCurrentPassword);
        final EditText etPassword = changePasswordDialog.findViewById(R.id.etPassword);
        final EditText etConfirmPassword = changePasswordDialog.findViewById(R.id.etConfirmPassword);
        Button buChangePassword = changePasswordDialog.findViewById(R.id.buChangePassword);
        buChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbManager.getCurrentUser(id);
                cursor.moveToFirst();
                String currentPassword = etCurrentPassword.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if(currentPassword.equals(cursor.getString(cursor.getColumnIndex(DBManager.colPassword)))){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colPassword,password);
                    long returnID = dbManager.changePassword(contentValues,id);
                    if(password.equals(confirmPassword) && returnID!=0){
                        Toast.makeText(getApplicationContext(),"Password Successfully Changed",Toast.LENGTH_LONG).show();
                        changePasswordDialog.dismiss();

                    }
                    else if(password.equals(confirmPassword)){
                        Toast.makeText(getApplicationContext(),"Password Not Changed",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Passwords doesn't match",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Current Password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void buChangeBankPassword(View view) {

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog changeBankPasswordDialog = new Dialog(this);
        changeBankPasswordDialog.setContentView(R.layout.dialog_change_password);
        Objects.requireNonNull(changeBankPasswordDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(changeBankPasswordDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changeBankPasswordDialog.show();
        final EditText etCurrentPassword = changeBankPasswordDialog.findViewById(R.id.etCurrentPassword);
        final EditText etPassword = changeBankPasswordDialog.findViewById(R.id.etPassword);
        final EditText etConfirmPassword = changeBankPasswordDialog.findViewById(R.id.etConfirmPassword);
        Button buChangePassword = changeBankPasswordDialog.findViewById(R.id.buChangePassword);
        buChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbManager.getCurrentUser(id);
                cursor.moveToFirst();
                String currentPassword = etCurrentPassword.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if(currentPassword.equals(cursor.getString(cursor.getColumnIndex(DBManager.colBankPassword)))){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colBankPassword,password);
                    long returnID = dbManager.changeBankPassword(contentValues,id);
                    if(password.equals(confirmPassword) && returnID!=0){
                        Toast.makeText(getApplicationContext(),"Banking Password Successfully Changed",Toast.LENGTH_LONG).show();
                        changeBankPasswordDialog.dismiss();

                    }
                    else if(password.equals(confirmPassword)){
                        Toast.makeText(getApplicationContext(),"Banking Password Not Changed",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Passwords doesn't match",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Current Password",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void buBackup(View view) {

        if (checkIfAlreadyHavePermission()){
            commonMethodArea.exportDB();
        }
        else {
            requestForSpecificPermission();
        }


    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkIfAlreadyHavePermission() {
        int resultRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultWrite = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return resultRead == PackageManager.PERMISSION_GRANTED && resultWrite == PackageManager.PERMISSION_GRANTED;
    }

}
