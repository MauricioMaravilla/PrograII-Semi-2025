package com.example.miprimeraaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView tempVal;
    TabHost tbh;
    Button btnMetros, btnArea;
    Spinner spnDe, spnA;
    Conversores objConversor = new Conversores();

    public class Conversores {
        public double convertir(int tipo, int de, int a, double cantidad) {
            double[] factores = {1.0, 0.698, 0.836, 1.0, 0.0001, 0.0007, 0.0001};
            double enMetrosCuadrados = cantidad * factores[de];
            return enMetrosCuadrados / factores[a];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración del TabHost
        tbh = findViewById(R.id.tbhMetros);
        tbh.setup();
        tbh.addTab(tbh.newTabSpec("Metros").setContent(R.id.tabMetros).setIndicator("Metros"));
        tbh.addTab(tbh.newTabSpec("Area").setContent(R.id.tabConversor).setIndicator("Área"));

        // Botón para calcular tarifa de agua potable
        btnMetros = findViewById(R.id.btnMetrosConvertir);
        btnMetros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtMetrosCantidad);
                double num1 = Double.parseDouble(tempVal.getText().toString());
                double Calculo = calcularTarifaAgua(num1);

                tempVal = findViewById(R.id.lblrespuesta);
                tempVal.setText("Precio: " + Calculo + "$");
            }
        });

        // Botón para conversión de área
        btnArea = findViewById(R.id.btnAreaConvertir);
        btnArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnDe = findViewById(R.id.spnAreaDe);
                spnA = findViewById(R.id.spnAreaA);
                tempVal = findViewById(R.id.txtAreaCantidad);

                int de = spnDe.getSelectedItemPosition();
                int a = spnA.getSelectedItemPosition();
                double cantidad = Double.parseDouble(tempVal.getText().toString());

                double resultado = objConversor.convertir(0, de, a, cantidad);
                Toast.makeText(getApplicationContext(), "Resultado: " + resultado, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Método para calcular el valor a pagar según el consumo de agua
    private double calcularTarifaAgua(double metrosConsumidos) {
        if (metrosConsumidos <= 18) {
            return 6.0;
        } else if (metrosConsumidos <= 28) {
            return (metrosConsumidos - 18) * 0.45 + 6.0;
        } else {
            double exceso28 = (metrosConsumidos - 28) * 0.65;
            double exceso18 = 10 * 0.45; // 10 metros entre 18 y 28
            return 6.0 + exceso18 + exceso28;
        }
    }
}

