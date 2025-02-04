package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText txtNum1, txtNum2;
    TextView lblRespuesta;
    RadioGroup rgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btncalcular);
        txtNum1 = findViewById(R.id.txtnum1);
        txtNum2 = findViewById(R.id.txtnum2);
        lblRespuesta = findViewById(R.id.lblrespuesta);
        rgo = findViewById(R.id.rgoOpciones);

        // Configurar el listener del botón
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularResultado();
            }
        });

        rgo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                txtNum2.setEnabled(checkedId != R.id.optraiz && checkedId != R.id.optfactorial);
            }
        });
    }

    private void calcularResultado() {
        try {
            double num1 = Double.parseDouble(txtNum1.getText().toString());
            double num2 = 0;
            double respuesta = 0;

            int selectedId = rgo.getCheckedRadioButtonId();

            if (selectedId == R.id.optraiz || selectedId == R.id.optfactorial) {
                txtNum2.setEnabled(false);
            } else {
                txtNum2.setEnabled(true);
                num2 = Double.parseDouble(txtNum2.getText().toString());
            }

            if (selectedId == R.id.optsuma) {
                respuesta = num1 + num2;
            } else if (selectedId == R.id.optresta) {
                respuesta = num1 - num2;
            } else if (selectedId == R.id.optmultiplicacion) {
                respuesta = num1 * num2;
            } else if (selectedId == R.id.optdivision) {
                if (num2 != 0) {
                    respuesta = num1 / num2;
                } else {
                    lblRespuesta.setText("Error: División por cero");
                    return;
                }
            } else if (selectedId == R.id.optexponensacion) {
                respuesta = Math.pow(num1, num2);
            } else if (selectedId == R.id.optporcentaje) {
                respuesta = (num1 * num2) / 100;
            } else if (selectedId == R.id.optraiz) {
                respuesta = Math.sqrt(num1);
            } else if (selectedId == R.id.optfactorial) {
                respuesta = 1;
                for (int i = 1; i <= num1; i++) {
                    respuesta *= i;
                }
            } else if (selectedId == R.id.optmayorque) {
                respuesta = Math.max(num1, num2);
            } else if (selectedId == R.id.optmodulo) {
                if (num2 != 0) {
                    respuesta = num1 % num2;
                } else {
                    lblRespuesta.setText("Error: Módulo por cero");
                    return;
                }
            }

            lblRespuesta.setText("Respuesta: " + respuesta);

        } catch (NumberFormatException e) {
            lblRespuesta.setText("Error: Entrada no válida");
        }
    }
}


