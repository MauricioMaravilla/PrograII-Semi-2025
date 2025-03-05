package com.example.miprimeraaplicacion;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView tempval;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAmigo();
            }
        });
    }
    private void guardarAmigo() {
        tempval = findViewById(R.id.txtNombre);
        String nombre = tempval.getText().toString();

        tempval = findViewById(R.id.txtDireccion);
        String direccion = tempval.getText().toString();

        tempval = findViewById(R.id.txtTelefono);
        String telefono = tempval.getText().toString();

        tempval = findViewById(R.id.txtEmail);
        String email = tempval.getText().toString();

        tempval = findViewById(R.id.txtDui);
        String dui = tempval.getText().toString();

        String[] datos = {"", nombre, direccion, telefono, email, dui, ""};
        db.administrar_amigos("agregar", datos);
        Toast.makeText(getApplicationContext(), "registro guardado con exito", Toast.LENGTH_SHORT).show();
    }
}





