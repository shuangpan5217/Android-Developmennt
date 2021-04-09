package edu.sjsu.android.temperatureconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.editText);
    }

    public void onClick(View view){
        if (view.getId() == R.id.button) {
            RadioButton celsiusButton = (RadioButton) findViewById(R.id.radioButton2);
            RadioButton fahrenheitButton = (RadioButton) findViewById(R.id.radioButton3);
            if(text.getText().length() == 0){
                Toast.makeText(this, "Please enter a valid number",
                        Toast.LENGTH_LONG).show();
                return;
            }
            float inputValue;
            try{
                inputValue = Float.parseFloat(text.getText().toString());
            }catch(NumberFormatException e){
                Toast.makeText(this, "Please enter a valid number",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(fahrenheitButton.isChecked()){
                text.setText(String.valueOf(ConverterUtil.convertCelsiusToFahrenheit(inputValue)));
            }else{
                text.setText(String.valueOf(ConverterUtil.convertFahrenheitToCelsius(inputValue)));
            }
        }
    }
}