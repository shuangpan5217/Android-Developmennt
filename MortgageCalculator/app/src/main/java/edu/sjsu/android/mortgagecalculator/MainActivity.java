package edu.sjsu.android.mortgagecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText amountBorrowed;
    private SeekBar seekBar;
    private TextView interestRateNumber;
    private CheckBox checkBox;
    private TextView monthlyPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountBorrowed = (EditText) findViewById(R.id.editTextNumberDecimal);
        interestRateNumber = (TextView) findViewById(R.id.interestRateNumber);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        setSeekBarListener();
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        monthlyPayment = (TextView) findViewById(R.id.result);
    }

    public void onClick(View view){
        if(view.getId() == R.id.button) {
            // amount borrowed
            String amount = amountBorrowed.getText().toString();
            if(amount.length() == 0){
                Toast.makeText(this, "Please enter a valid number",
                        Toast.LENGTH_LONG).show();
                monthlyPayment.setText(String.valueOf(0.00));
                return;
            }
            float borrowedAmount;
            try{
                borrowedAmount = Float.parseFloat(amount);
            }
            catch (NumberFormatException e){ //include overflow
                Toast.makeText(this, "Please enter a valid number",
                        Toast.LENGTH_LONG).show();
                monthlyPayment.setText(String.valueOf(0.0));
                return;
            }

            //interest rate
            float interest = Float.parseFloat(interestRateNumber.getText().toString());

            //loan term
            RadioButton fifteen = (RadioButton) findViewById(R.id.radioButton15);
            RadioButton twenty = (RadioButton) findViewById(R.id.radioButton20);
            RadioButton thirty = (RadioButton) findViewById(R.id.radioButton30);
            int term;
            if(fifteen.isChecked())
                term = Integer.parseInt(fifteen.getText().toString());
            else if(twenty.isChecked())
                term = Integer.parseInt(twenty.getText().toString());
            else
                term = Integer.parseInt(thirty.getText().toString());

            float mp;
            //calculate monthly payment
            if(checkBox.isChecked()){
                mp = CalculateLoanUtil.calculate(borrowedAmount, interest, term, true);
            }else{
                mp = CalculateLoanUtil.calculate(borrowedAmount, interest, term, false);
            }
            monthlyPayment.setText(String.valueOf(mp));
        }
    }

    private void setSeekBarListener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float current = (float) (i / 10.0);
                interestRateNumber.setText(String.valueOf(current));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}