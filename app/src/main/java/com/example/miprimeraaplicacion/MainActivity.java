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
    TextView tempVal;
    DB db;
    String accion = "nuevo", idProducto = "", id = "", rev = "";
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
        img = findViewById(R.id.imgFotoProducto);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(view -> guardarProducto());

        fab = findViewById(R.id.fabListaProductos);
        fab.setOnClickListener(view -> abrirVentana());

        mostrarDatos();
        tomarFoto();
    }

    private void mostrarDatos() {
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("productos"));
                id = datos.getString("_id");
                rev = datos.getString("_rev");
                idProducto = datos.getString("idProducto");

                tempVal = findViewById(R.id.txtCodigo);
                tempVal.setText(datos.getString("codigo"));

                tempVal = findViewById(R.id.txtDescripcion);
                tempVal.setText(datos.getString("descripcion"));

                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(datos.getString("marca"));

                tempVal = findViewById(R.id.txtPresentacion);
                tempVal.setText(datos.getString("presentacion"));

                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(datos.getString("precio"));

                urlCompletaFoto = datos.getString("urlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));
            } else {
                idProducto = utls.generarUnicoId();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void tomarFoto() {
        img.setOnClickListener(view -> {
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoProducto = null;
            try {
                fotoProducto = crearImagenProducto();
                if (fotoProducto != null) {
                    Uri uriFotoProducto = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.miprimeraaplicacion.fileprovider", fotoProducto);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoProducto);
                    startActivityForResult(tomarFotoIntent, 1);
                } else {
                    mostrarMsg("No se pudo crear la imagen.");
                }
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                //Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageURI(Uri.parse(urlCompletaFoto));
            } else {
                mostrarMsg("No se tomó la foto.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private File crearImagenProducto() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_" + fechaHoraMs + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (!dirAlmacenamiento.exists()==false ) {
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, lista_productos.class);
        startActivity(intent);
    }

    private void guardarProducto() {
        try {
            tempVal = findViewById(R.id.txtCodigo);
            String codigo = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDescripcion);
            String descripcion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtMarca);
            String marca = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPresentacion);
            String presentacion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPrecio);
            String precio = tempVal.getText().toString();

            JSONObject datosProductos = new JSONObject();
            if (accion.equals("modificar")) {
                datosProductos.put("_id", id);
                datosProductos.put("_rev", rev);
            }
            datosProductos.put("idProducto", idProducto);
            datosProductos.put("codigo", codigo);
            datosProductos.put("descripcion", descripcion);
            datosProductos.put("marca", marca);
            datosProductos.put("presentacion", presentacion);
            datosProductos.put("precio", precio);
            datosProductos.put("foto", urlCompletaFoto);

            di = new detectarInternet(this);
            if (di.hayConexionInternet()) { //online
                // Enviar los datos al servidor
                enviarDatosServidor objEnviarDatos = new enviarDatosServidor(this);
                String respuesta = objEnviarDatos.execute(datosProductos.toString(), "POST", utilidades.url_mto).get();

                JSONObject respuestaJSON = new JSONObject(respuesta);
                if (respuestaJSON.getBoolean("ok")) {
                    id = respuestaJSON.getString("id");
                    rev = respuestaJSON.getString("rev");
                } else {
                    mostrarMsg("Error: " + respuestaJSON.getString("msg"));
                }
            }
            String[] datos = {idProducto, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto};
            db.administrar_productos(accion, datos);
            Toast.makeText(getApplicationContext(), "Registro guardado con éxito.", Toast.LENGTH_LONG).show();
            abrirVentana();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
}




