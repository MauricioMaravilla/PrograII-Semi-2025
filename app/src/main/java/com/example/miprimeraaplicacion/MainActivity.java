package com.example.miprimeraaplicacion;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempval;
    DB db;
    String accion = "nuevo", idAmigo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
    }
    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("amigos"));
                idAmigo = datos.getString("idAmigo");

                tempval = findViewById(R.id.txtNombre);
                tempval.setText(datos.getString("nombre"));

                tempval = findViewById(R.id.txtDireccion);
                tempval.setText(datos.getString("direccion"));

                tempval = findViewById(R.id.txtTelefono);
                tempval.setText(datos.getString("telefono"));

                tempval = findViewById(R.id.txtEmail);
                tempval.setText(datos.getString("email"));

                tempval = findViewById(R.id.txtDui);
                tempval.setText(datos.getString("dui"));
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void abrirVentana(){
        Intent intent = new Intent(this, lista_amigos.class);
        startActivity(intent);
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

        String[] datos = {idAmigo, nombre, direccion, telefono, email, dui, ""};
        db.administrar_amigos(accion, datos);
        Toast.makeText(getApplicationContext(), "registro guardado con exito", Toast.LENGTH_SHORT).show();
        abrirVentana();
    }
}





