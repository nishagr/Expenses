package com.kyra.expensemanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {


    public CalculatorFragment() {
        // Required empty public constructor
    }

    GridLayout gridNumber,gridCalculation;
    TextView tvCalc,tvResult;
    double total = 0;
    boolean calc_flag = false,dot_flag = false;
    char action = '=';
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        tvCalc = view.findViewById(R.id.tvCalc);
        tvResult = view.findViewById(R.id.tvResult);
        gridNumber = view.findViewById(R.id.gridNumber);
        gridCalculation = view.findViewById(R.id.gridCalculation);
        clickNumberButton(gridNumber);
        clickCalculationButton(gridCalculation);
        return view;
    }

    private void clickNumberButton(@NonNull final GridLayout gridNumber){
        for (int i = 0; i < gridNumber.getChildCount(); i++) {
            final int final_i = i;
            final Button button = (Button) gridNumber.getChildAt(final_i);
            button.setOnClickListener(new View.OnClickListener() {
                Button buNumber = (Button) gridNumber.getChildAt(final_i);
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(),"position = " + final_i,Toast.LENGTH_LONG).show();
                    if (final_i == 11){
                       /* if (action != '='){
                            total = Double.parseDouble(tvResult.getText().toString());
                            tvCalc.setText(tvResult.getText());
                        }
                        action='=';
                        calc_flag = false;
                        dot_flag = false;
                        tvResult.setText("");*/
                        if (tvCalc.getText()!=null){
                            String result = tvResult.getText().toString();
                            if(result.length()>6){
                                result = result.substring(0,6);
                            }
                            total = Double.parseDouble(result);
                            tvCalc.setText(result);
                            tvResult.setText("");
                            calc_flag = false;
                            dot_flag = false;
                        }
                    }
                    else if (final_i==9) {
                        if (!dot_flag) {
                            tvCalc.setText(String.format("%s.", tvCalc.getText()));
                            dot_flag = true;
                        }
                    }
                    else {
                        tvCalc.setText(String.format("%s%s", tvCalc.getText(), buNumber.getText()));
                        if (calc_flag) {
                            String string = tvCalc.getText().toString();
                            String number = string.substring(string.lastIndexOf(action) + 1);
                            if (tvCalc.getText().toString().length() < 12) {
                                tvResult.setText(String.valueOf(calc(total, Double.parseDouble(number), action)));
                            }
                        }
                    }
                }
            });
        }
    }

    private void clickCalculationButton(@NonNull final GridLayout gridCalculation){
        for (int i = 0; i < gridCalculation.getChildCount(); i++) {
            final int final_i = i;
            final Button button = (Button) gridCalculation.getChildAt(final_i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(),"position = " + final_i,Toast.LENGTH_LONG).show();
                    Button buCalc = (Button) gridCalculation.getChildAt(final_i);

                    if (final_i == 0){
                        String str = (String) tvCalc.getText();
                        if (str != null && str.length() > 0) {
                            if (str.charAt(str.length()-1)=='.'){
                                dot_flag = false;
                            }
                            str = str.substring(0, str.length() - 1);
                        }
                        else {
                            total = 0;
                        }
                        tvCalc.setText(str);
                        String string = tvCalc.getText().toString();
                        if (string.length() > 0) {
                            int i = string.length() - 1;
                            while (i>0 && (Character.isDigit(string.charAt(i)) || string.charAt(i) == '.')) {
//                                Log.d("Calc Character", String.valueOf(string.charAt(i)));
                                i--;
                            }
                            if (i>0){
                                action = string.charAt(i);
                            }
                            else{
                                action = '=';
                            }
//                            Log.d("Calc Action new", String.valueOf(action));
                        }
                        if (action == '='){
//                            Log.d("Enter","");
                            if(!tvCalc.getText().toString().equals(""))
                                total = Double.parseDouble(tvCalc.getText().toString());
                        }
                        tvResult.setText(String.valueOf(total));
                        /*String number = string.substring(string.lastIndexOf(action)+1);
                        if(!number.equals("")){
                            tvResult.setText(String.valueOf(calc(total,Double.parseDouble(number),action)));
                            //total = Double.parseDouble(tvResult.getText().toString());
                        }*/
                    }
                    else {
                        if(!tvCalc.getText().toString().isEmpty()){
                            dot_flag = false;
                            if (total==0 || !calc_flag){
                                total = Double.parseDouble(tvCalc.getText().toString());
                            }
                            else{
                                String string = tvCalc.getText().toString();
                                String number = string.substring(string.lastIndexOf(action)+1);
                                if (string.length()<12)
                                    total = calc(total,Double.parseDouble(number),action);
                            }
                            action = buCalc.getText().charAt(0);
                            calc_flag=true;
                            tvCalc.setText(String.format("%s%s", tvCalc.getText(), buCalc.getText()));
                        }
                    }
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (final_i == 0){
                        if (tvCalc.getText()!=null){
                            tvCalc.setText("");
                            tvResult.setText("");
                            calc_flag = false;
                            dot_flag = false;
                            total = 0;
                            action = '=';
                        }
                    }
                    return true;
                }
            });
        }
    }

    private double calc(double num1,double num2,char action){
        double result = 0;
        switch (action){
            case '/':
                result = num1/num2;
                break;
            case '*':
                result = num1*num2;
                break;
            case '+':
                result = num1+num2;
                break;
            case '-':
                result = num1-num2;
                break;
        }
        return result;
    }

}
