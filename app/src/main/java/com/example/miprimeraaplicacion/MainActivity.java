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
public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempval;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view->guardarAmigo());
        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view->abrirVentana());
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

        String[] datos = {"", nombre, direccion, telefono, email, dui, ""};
        db.administrar_amigos("agregar", datos);
        Toast.makeText(getApplicationContext(), "registro guardado con exito", Toast.LENGTH_SHORT).show();
        abrirVentana();
    }
}





