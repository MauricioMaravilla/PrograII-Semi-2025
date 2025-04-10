package com.example.miprimeraaplicacion;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_productos extends Activity {

    // Declaraciones
    Bundle parametros = new Bundle();
    ListView ltsProductos;
    Cursor cProductos;
    DB db;
    final ArrayList<productos> alProductos = new ArrayList<productos>();
    final ArrayList<productos> alProductosCopia = new ArrayList<productos>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    productos misProductos;
    FloatingActionButton fab;
    int posicion = 0;
    obtenerDatosServidor datosServidor;
    detectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        parametros.putString("accion", "nuevo");
        db = new DB(this);

        fab = findViewById(R.id.fabAgregarProducto);
        fab.setOnClickListener(view -> abriVentana());

        listarDatos();
        buscarproductos();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getJSONObject("value").getString("codigo"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if( item.getItemId()==R.id.mnxNuevo){
                abriVentana();
            }else if( item.getItemId()==R.id.mnxModificar){
                parametros.putString("accion", "modificar");
                parametros.putString("productos", jsonArray.getJSONObject(posicion).getJSONObject("value").toString());
                abriVentana();
            } else if (item.getItemId()==R.id.mnxEliminar) {
                eliminarProducto();
            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarProducto() {
        try {
            String codigo = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("codigo");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(codigo);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            di = new detectarInternet(this);
                            if (di.hayConexionInternet()) {
                                JSONObject datosProductos = new JSONObject();
                                String _id = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_id");
                                String _rev = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_rev");
                                String url = utilidades.url_mto + "/" + _id + "?rev=" + _rev;
                                enviarDatosServidor objEnviarDatosServidor = new enviarDatosServidor(this);

                                String respuesta = new enviarDatosServidor(this)
                                        .execute(datosProductos.toString(), "DELETE", url).get();

                                JSONObject respuestaJSON = new JSONObject(respuesta);
                                if(!respuestaJSON.getBoolean("ok")) {
                                    mostrarMsg("Error: " + respuesta);
                                }
                            }

                            String respuesta = db.administrar_productos("eliminar",
                                    new String[]{jsonArray.getJSONObject(posicion).getJSONObject("value").getString("idProducto")});

                            if (respuesta.equals("ok")) {
                                listarDatos();
                                mostrarMsg("Registro eliminado con éxito");
                            } else {
                                mostrarMsg("Error: " + respuesta);
                            }
                        } catch (Exception e) {
                            mostrarMsg("Error: " + e.getMessage());
                        }
                    });
                      confirmacion.setNegativeButton("No", (dialog, which) -> {
                         dialog.dismiss();
                    });
                      confirmacion.create().show();

            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
    }
    private void abriVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    // Decide si carga los datos online u offline
    private void listarDatos() {
        try {
            di = new detectarInternet(this);
            if (di.hayConexionInternet()) {
                datosServidor = new obtenerDatosServidor();
                String respuesta = datosServidor.execute().get();
                jsonObject = new JSONObject(respuesta);
                jsonArray = jsonObject.getJSONArray("rows");
                mostrarDatosProductos();
            } else {
                obtenerDatosProductos();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    // Carga datos locales desde la base de datos
    private void obtenerDatosProductos() {
        try {
            cProductos = db.lista_productos();
            if (cProductos.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("idProducto", cProductos.getString(0));
                    jsonObject.put("codigo", cProductos.getString(1));
                    jsonObject.put("descripcion", cProductos.getString(2));
                    jsonObject.put("marca", cProductos.getString(3));
                    jsonObject.put("presentacion", cProductos.getString(4));
                    jsonObject.put("precio", cProductos.getString(5));
                    jsonObject.put("foto", cProductos.getString(6));
                    jsonArray.put(jsonObject);
                } while (cProductos.moveToNext());
                mostrarDatosProductos();
            } else {
                mostrarMsg("No hay productos registrados.");
                abriVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    // Muestra los datos en el ListView
    private void mostrarDatosProductos() {
        try {
            if (jsonArray.length()>0){
                ltsProductos = findViewById(R.id.ltsProductos);
                alProductos.clear();
                alProductosCopia.clear();
di = new detectarInternet(this);
                for (int i=0; i<jsonArray.length(); i++) {
                    if (di.hayConexionInternet()){
                        jsonObject = jsonArray.getJSONObject(i).getJSONObject("value");

                    }else {
                        jsonObject = jsonArray.getJSONObject(i);
                    }
                    jsonObject = jsonArray.getJSONObject(i);
                    misProductos = new productos(
                            jsonObject.getString("idProducto"),
                            jsonObject.getString("codigo"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("marca"),
                            jsonObject.getString("presentacion"),
                            jsonObject.getString("precio"),
                            jsonObject.getString("foto")
                    );
                    alProductos.add(misProductos);
                }

                alProductosCopia.addAll(alProductos);
                ltsProductos.setAdapter(new AdaptadorProductos(this, alProductos));
                registerForContextMenu(ltsProductos);
            } else {
                mostrarMsg("No hay productos registrados.");
                abriVentana();
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void buscarproductos() {
        TextView tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alProductos.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alProductos.addAll(alProductosCopia);
                }else{
                    for (productos item: alProductosCopia){
                        if(item.getCodigo().toLowerCase().contains(buscar) ||
                                item.getDescripcion().toLowerCase().contains(buscar) ||
                                item.getPrecio().toLowerCase().contains(buscar)){
                            alProductos.add(item);
                        }
                    }
                }

                ltsProductos.setAdapter(new AdaptadorProductos(getApplicationContext(), alProductos));
            }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });
}
    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
