package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;

    TextView tempval;
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

                tempval = findViewById(R.id.lblrespuesta);
                tempval.setText("respuesta: "+ respuesta);
            }
        });
    }
}