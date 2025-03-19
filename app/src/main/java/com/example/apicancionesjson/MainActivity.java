package com.example.apicancionesjson;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    protected TextView texto1;
    protected ListView lista1;
    protected String rutaJson = "https://raw.githubusercontent.com/Alebernabe5/listadocanciones/refs/heads/main/listadocanciones.json";
    protected Thread tareaOnline;
    protected String infoOnline;
    protected  String contenidoStream = "";
    protected ArrayList<String> listado = new ArrayList<String>(); //Para rellenar el listview
    protected ArrayList<cancion> canciones = new ArrayList<cancion>(); //Almacenar info
    protected ArrayAdapter<String> adaptador;


    public void parsearJson (String json)
    {

        try {
            JSONObject estructura = new JSONObject(json);
            JSONArray listaCanciones = (JSONArray) estructura.getJSONArray("data");

            for ( int i=0; i<listaCanciones.length(); i++){

                JSONObject obj = listaCanciones.getJSONObject(i); //Con esto estoy creando un objeto json
                //Accedo a cada uno de las partes del array
                String nombre = obj.getString("nombre");
                String myUrl = obj.getString("url");

                listado.add(nombre);
                cancion can = new cancion (nombre,myUrl);
                canciones.add(can);

            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public String getJsonAPIOnline()
    {
        try {
            URL url = new URL(rutaJson); //Va a definir la ruta de la conexion
            HttpURLConnection con = (HttpURLConnection) url.openConnection(); //Defino una conexion con el servidor
            con.connect(); //Me conecto al servidor
            InputStream stream = con.getInputStream();//Abro un camino de conexion
            BufferedReader br = new BufferedReader(new InputStreamReader(stream)); //Defino el stream a leer
            StringBuffer buffer = new StringBuffer(); //Buffer de lectura

            while (contenidoStream!= null)
            {
                contenidoStream = br.readLine();
                if (contenidoStream!= null)
                {
                    buffer.append(contenidoStream);
                }
            }
            br.close();
            con.disconnect();
            return buffer.toString();


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        texto1 = (TextView) findViewById(R.id.texto1_main);
        lista1 = (ListView) findViewById(R.id.lista1_main);

        //Lo que haya aqui se va a programar en paralelo al programa principal
        tareaOnline = new Thread()
        {

            public void run()
            {
            //Conseguir el json
                infoOnline = getJsonAPIOnline();

            //Parsear el json
                parsearJson (infoOnline); //Le paso el json que recibo online


            //Mostrar en la interfaz gráfica
                //Si queremos modificar dentro de thread la parte de la interfaz grafica, hacerla dentro de un runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //texto1.setText(infoOnline);
                        adaptador= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listado);
                        lista1.setAdapter(adaptador);
                    }
                });
            }
        };

        tareaOnline.start(); //Con esto lanzo el thread





    }
}