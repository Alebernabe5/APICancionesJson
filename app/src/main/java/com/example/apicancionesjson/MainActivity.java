package com.example.apicancionesjson;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    protected TextView texto1;
    protected ListView lista1;
    protected String rutaJson = "https://raw.githubusercontent.com/Alebernabe5/listadocanciones/refs/heads/main/listadocanciones.json";
    protected Thread tareaOnline;
    protected String infoOnline;
    protected  String contenidoStream = "";

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
            texto1.setText(buffer.toString());



        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return "";
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
            //Mostrar en la interfaz gráfica
            }
        };

        tareaOnline.start(); //Con esto lanzo el thread





    }
}