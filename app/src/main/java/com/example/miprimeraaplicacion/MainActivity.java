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


            // Listener para deshabilitar txtNum2 cuando se selecciona factorial o raíz
        rgo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    txtNum2.setEnabled(checkedId != R.id.optraiz && checkedId != R.id.optfactorial);
                }
            });
        }
            private void calcularResultado() {
                double num1 = Double.parseDouble(txtNum1.getText().toString());
                double num2 = 0;
                double respuesta = 0;
                int selectedId = rgo.getCheckedRadioButtonId(); // Obtener el ID del RadioButton seleccionado

                // Desactivar txtNum2 si se selecciona factorial o raíz
                if (selectedId == R.id.optraiz || selectedId == R.id.optfactorial) {
                    txtNum2.setEnabled(false);
                } else {
                    txtNum2.setEnabled(true);
                    num2 = Double.parseDouble(txtNum2.getText().toString());
                }

                if (selectedId == R.id.optsuma) {
                    respuesta = num1 + num2;
                }

                else if (selectedId == R.id.optresta) {
                    respuesta = num1 - num2;
                }

                else if (selectedId == R.id.optmultiplicacion) {
                    respuesta = num1 * num2;
                }

                else if (selectedId == R.id.optdivision) {
                    respuesta = num1 / num2;
                }

                else if (selectedId == R.id.optexponensacion) {
                    respuesta = Math.pow(num1, num2);
                }

                else if (selectedId == R.id.optporcentaje) {
                    respuesta = (num1 * num2) / 100;
                }

                else if (selectedId == R.id.optraiz) {
                    respuesta = Math.sqrt(num1);
                }

                else if (selectedId == R.id.optfactorial) {
                    respuesta = 1;
                    for (int i = 1; i <= num1; i++) {
                        respuesta *= i;
                    }
                }
                lblRespuesta = findViewById(R.id.lblrespuesta);
                lblRespuesta.setText("Respuesta: "+ respuesta);
            }
        }



