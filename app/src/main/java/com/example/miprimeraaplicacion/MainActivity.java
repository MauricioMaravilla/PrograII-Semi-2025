package com.example.miprimeraaplicacion;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempval;
    DB db;
    String accion = "nuevo", idAmigo = "", id="", rev="";
    ImageView img;
    String urlCompletaFoto = "";
    Intent tomarFotoIntent;
    utilidades utls;
    detectarInternet di;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
        img = findViewById(R.id.imgFotoAmigo);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
        tomarFoto();
    }
    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("amigos"));
                id = datos.getString("_id");
                rev = datos.getString("_rev");
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

                urlCompletaFoto = datos.getString("urlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }else {
                idAmigo = utls.generarUnicoId();
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoAmigo = null;
            try{
                fotoAmigo = crearImagenAmigo();
                if( fotoAmigo!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.miprimeraaplicacion.fileprovider", fotoAmigo);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("Nose pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                //Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }else{
                mostrarMsg("No se tomo la foto.");
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void abrirVentana(){
        Intent intent = new Intent(this, lista_amigos.class);
        startActivity(intent);
    }
    private void guardarAmigo() {
        try {
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

            JSONObject datosAmigos = new JSONObject();
            if (accion.equals("modificar")) {
                datosAmigos.put("_id", id);
                datosAmigos.put("_rev", rev);
            }
            datosAmigos.put("idAmigo", idAmigo);
            datosAmigos.put("nombre", nombre);
            datosAmigos.put("direccion", direccion);
            datosAmigos.put("telefono", telefono);
            datosAmigos.put("email", email);
            datosAmigos.put("dui", dui);
            datosAmigos.put("urlFoto", urlCompletaFoto);

            di = new detectarInternet(this);
            if(di.hayConexionInternet()) {//online
                //enviar los datos al servidor
                enviarDatosServidor objEnviarDatos = new enviarDatosServidor(this);
                String respuesta = objEnviarDatos.execute(datosAmigos.toString(), "POST", utilidades.url_mto).get();

                JSONObject respuestaJSON = new JSONObject(respuesta);
                if(respuestaJSON.getBoolean("ok")){
                    id = respuestaJSON.getString("id");
                    rev = respuestaJSON.getString("rev");
                }else{
                    mostrarMsg("Error: "+respuestaJSON.getString("msg"));
                }
            }
            String[] datos = {idAmigo, nombre, direccion, telefono, email, dui, urlCompletaFoto};
            db.administrar_amigos(accion, datos);
            Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
            abrirVentana();
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }


    }
}





