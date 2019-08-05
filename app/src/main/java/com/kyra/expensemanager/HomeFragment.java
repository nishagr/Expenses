package com.kyra.expensemanager;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    GridLayout gridLayout;
    DBManager dbManager;
    String  id;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        dbManager = new DBManager(getContext());
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        assert bundle != null;
        id = (String) bundle.get("UserID");
        gridLayout = view.findViewById(R.id.mainGrid);
        setSingleEvent(gridLayout);
        return view;
    }


    private void setSingleEvent(GridLayout mainGrid) {

        for(int i=0;i<mainGrid.getChildCount();i++){
            final int final_i=i;
            final CardView cardView=(CardView)mainGrid.getChildAt(final_i);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(),"position = " + final_i,Toast.LENGTH_LONG).show();
                    List<String> categories = Arrays.asList(getResources().getStringArray(R.array.categories));
                    if (final_i<10){
                        Intent intent = new Intent(getActivity(),ListActivity.class);
                        intent.putExtra("name", categories.get(final_i));
                        startActivity(intent);
                    }
                    else if (final_i == 10){
                        Intent intent = new Intent(getActivity(),DebtActivity.class);
                        intent.putExtra("name",categories.get(final_i));
                        startActivity(intent);
                    }
                    else {
                        final Intent[] intent = {new Intent(getActivity(), BankActivity.class)};
                        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                        Cursor cursor = dbManager.getCurrentUser(id);
                        cursor.moveToFirst();
                        final String password = cursor.getString(cursor.getColumnIndex(DBManager.colBankPassword));
                        final Dialog bankPassword = new Dialog(Objects.requireNonNull(getContext()));
                        bankPassword.setContentView(R.layout.dialog_bank_password);
                        Objects.requireNonNull(bankPassword.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Objects.requireNonNull(bankPassword.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                        bankPassword.show();
                        final EditText etBankPassword =  bankPassword.findViewById(R.id.etBankPassword);
                        Button buEnter = bankPassword.findViewById(R.id.buEnter);
                        buEnter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(etBankPassword.getText().toString().equals(password)){
                                    intent[0] = new Intent(getActivity(),BankActivity.class);
                                    intent[0].putExtra("name","Bank");
                                    startActivity(intent[0]);
                                    bankPassword.dismiss();
                                }
                                else {
                                    Toast.makeText(getActivity(),"Wrong Password",Toast.LENGTH_LONG).show();
                                    bankPassword.dismiss();
                                }
                            }
                        });

                    }
                    /*switch (final_i){
                        case 0: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Travel");
                            startActivity(intent[0]);
                            break;
                        case 1: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Food");
                            startActivity(intent[0]);
                            break;
                        case 2: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Fruits & Veg");
                            startActivity(intent[0]);
                            break;
                        case 3: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Groceries");
                            startActivity(intent[0]);
                            break;
                        case 4: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Extras");
                            startActivity(intent[0]);
                            break;
                        case 5: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Utilities");
                            startActivity(intent[0]);
                            break;
                        case 6: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Dine Out");
                            startActivity(intent[0]);
                            break;
                        case 7: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Shopping");
                            startActivity(intent[0]);
                            break;
                        case 8: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Luxury");
                            startActivity(intent[0]);
                            break;
                        case 9: intent[0] = new Intent(getActivity(),ListActivity.class);
                            intent[0].putExtra("name","Investment");
                            startActivity(intent[0]);
                            break;
                        case 10: intent[0] = new Intent(getActivity(),DebtActivity.class);
                            intent[0].putExtra("name","List");
                            startActivity(intent[0]);
                            break;
                        case 11: int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                            Cursor cursor = dbManager.getCurrentUser(id);
                            cursor.moveToFirst();
                            final String password = cursor.getString(cursor.getColumnIndex(DBManager.colBankPassword));
                            final Dialog bankPassword = new Dialog(Objects.requireNonNull(getContext()));
                            bankPassword.setContentView(R.layout.dialog_bank_password);
                            Objects.requireNonNull(bankPassword.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Objects.requireNonNull(bankPassword.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                            bankPassword.show();
                            final EditText etBankPassword =  bankPassword.findViewById(R.id.etBankPassword);
                            Button buEnter = bankPassword.findViewById(R.id.buEnter);
                            buEnter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(etBankPassword.getText().toString().equals(password)){
                                        intent[0] = new Intent(getActivity(),BankActivity.class);
                                        intent[0].putExtra("name","Bank");
                                        startActivity(intent[0]);
                                        bankPassword.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getActivity(),"Wrong Password",Toast.LENGTH_LONG).show();
                                        bankPassword.dismiss();
                                    }
                                }
                            });
                            break;
                    }*/
                }
            });
        }
    }

}
