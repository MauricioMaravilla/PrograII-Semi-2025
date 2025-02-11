package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Button btn;
    TextView tempVal;
    Spinner spnDe, spnA;
    conversores objConversores = new conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tabMonedas).setIndicator("MONEDAS", null));
        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));
        tbh.addTab(tbh.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("TIEMPO", null));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("ALMACENAMIENTO", null));
        tbh.addTab(tbh.newTabSpec("Masa").setContent(R.id.tabMasa).setIndicator("MASA", null));
        tbh.addTab(tbh.newTabSpec("Volumen").setContent(R.id.tabVolumen).setIndicator("VOLUMEN", null));
        tbh.addTab(tbh.newTabSpec("Transferencia").setContent(R.id.tabTransferenciadeDatos).setIndicator("TRANSFERENCIA", null));

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int opcion = tbh.getCurrentTab();
                int de = 0, a = 0;
                double cantidad = 0;
                String msg = "";

                tempVal = findViewById(R.id.txtCantidad);
                try {
                    cantidad = Double.parseDouble(tempVal.getText().toString());
                } catch (NumberFormatException e) {
                    tempVal = findViewById(R.id.lblRespuesta);
                    tempVal.setText("Error: Ingresa un número válido");
                    return;
                }

                switch (opcion) {
                    case 0: // Monedas
                        spnDe = findViewById(R.id.spnDeMonedas);
                        spnA = findViewById(R.id.spnAMonedas);
                        msg = "Conversión de moneda ";
                        break;
                    case 1: // Longitud
                        spnDe = findViewById(R.id.spnDeLongitud);
                        spnA = findViewById(R.id.spnALongitud);
                        msg = "Conversión de longitud ";
                        break;
                    case 2: // Tiempo
                        spnDe = findViewById(R.id.spnDeTiempo);
                        spnA = findViewById(R.id.spnATiempo);
                        msg = "Conversión de tiempo ";
                        break;
                    case 3: // Almacenamiento
                        spnDe = findViewById(R.id.spnDeAlmacenamiento);
                        spnA = findViewById(R.id.spnAAlmacenamiento);
                        msg = "Conversión de almacenamiento ";
                        break;
                    case 4: // Masa
                        spnDe = findViewById(R.id.spnDeMasa);
                        spnA = findViewById(R.id.spnAMasa);
                        msg = "Conversión de masa ";
                        break;
                    case 5: // Volumen
                        spnDe = findViewById(R.id.spnDeVolumen);
                        spnA = findViewById(R.id.spnAVolumen);
                        msg = "Conversión de volumen ";
                        break;
                    case 6: // Transferencia de Datos
                        spnDe = findViewById(R.id.spnDeTransferenciadeDatos);
                        spnA = findViewById(R.id.spnATransferenciadeDatos);
                        msg = "Conversión de transferencia de datos ";
                        break;
                    default:
                        tempVal = findViewById(R.id.lblRespuesta);
                        tempVal.setText("Error: Pestaña no reconocida");
                        return;
                }

                de = spnDe.getSelectedItemPosition();
                a = spnA.getSelectedItemPosition();

                Log.d("Conversión", "Opción: " + opcion);
                Log.d("Conversión", "De: " + de + " A: " + a);
                Log.d("Conversión", "Cantidad: " + cantidad);

                String unidadDe = spnDe.getSelectedItem().toString();
                String unidadA = spnA.getSelectedItem().toString();

                double respuesta = objConversores.convertir(opcion, de, a, cantidad);

                tempVal = findViewById(R.id.lblRespuesta);
                tempVal.setText("Respuesta: " + respuesta);
                msg += "\n" + cantidad + " " + unidadDe + " → " + respuesta + " " + unidadA;
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

            }
        });
    }
}
class conversores {
    double[][] valores = {
            // Monedas
            {1.0, 0.98, 7.73, 25.45, 36.78, 508.87, 8.74, 20.61, 3.72, 151.86},

            // Longitud
            {0.001, 0.01, 1, 1000, 0.0254, 0.3048, 0.9144, 1609.34, 0.1, 1000000},

            // Tiempo
            {0.001, 1, 60, 3600, 86400, 604800, 2.628e+6, 3.154e+7, 3.1557e+8, 3.1557e+10},

            // Almacenamiento
            {0.125, 1, 1024, 1048576, 1073741824.0, 1099511627776.0, 1125899906842624.0,
                    1.1529e+18, 1.1806e+21, 1.2089e+24},

            // Masa
            {0.000001, 0.001, 1, 1000, 1e6, 28.3495, 453.592, 6350.29, 100000, 907184.74},

            // Volumen
            {0.001, 0.01, 0.1, 1, 10, 100, 1000, 3.78541, 0.473176, 158.987},

            // Transferencia de Datos
            {1, 1000, 1e6, 1e9, 1e12, 8, 8000, 8e6, 8e9, 8e12}

    };
    public double convertir(int opcion, int de, int a, double cantidad) {
        double factorDe = valores[opcion][de];
        double factorA = valores[opcion][a];

        if (opcion == 0) {
            return cantidad * (factorA / factorDe);
        } else {
            return cantidad * (factorDe / factorA);
        }

    }

}
