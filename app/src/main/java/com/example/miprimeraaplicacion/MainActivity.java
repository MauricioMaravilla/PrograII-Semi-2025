package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView tempval;
    RadioGroup rgb;
    RadioButton opt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btncalcular);
        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                tempval = findViewById(R.id.txtnum1);
                Double num1 = Double.parseDouble(tempval.getText().toString());

                tempval = findViewById(R.id.txtnum2);
                Double num2 = Double.parseDouble(tempval.getText().toString());

                double respuesta = num1 + num2;
                double Respuesta = 0.0;
                Double num = 10.5;
                int valor = num.intValue();


                opt = findViewById(R.id.optsuma);
                if (opt.isChecked()) {
                    Respuesta = num1 + num2;
                }

                opt = findViewById(R.id.optresta);
                if (opt.isChecked()) {
                    Respuesta = num1 - num2;
                }

                opt = findViewById(R.id.optmultiplicacion);
                if (opt.isChecked()) {
                    Respuesta = num1 * num2;
                }

                opt = findViewById(R.id.optdivision);
                if (opt.isChecked()) {
                    Respuesta = num1 / num2;
                }

                opt = findViewById(R.id.optpotenciacion);
                if (opt.isChecked()) {
                    Respuesta = Math.pow(num1, num2); // exponenciacion
                }

                opt = findViewById(R.id.optporcentaje);
                if (opt.isChecked()) {
                    Respuesta = (num1 * num2) / 100; // Cálculo de porcentaje
                }

                opt = findViewById(R.id.optRaiz);
                if (opt.isChecked()) {
                    respuesta = Math.sqrt(num1);
                }

                opt = findViewById(R.id.optFactorial);
                if (opt.isChecked()) {
                    respuesta = calcularFactorial((int) num1);
                }

                tempval = findViewById(R.id.lblrespuesta);
                tempval.setText("Respuesta: "+ Respuesta);
            }
        });
    }
}
