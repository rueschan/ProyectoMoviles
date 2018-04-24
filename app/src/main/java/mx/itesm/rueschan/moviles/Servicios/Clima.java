package mx.itesm.rueschan.moviles.Servicios;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Rubén Escalante on 23/04/2018.
 */

public class Clima {

    /*{
    "coord":{"lon":139,"lat":35},
    "sys":{"country":"JP","sunrise":1369769524,"sunset":1369821049},
    "weather":[{
        "id":804,"main":"clouds","description":"overcast clouds","icon":"04n"}],
    "main":{"temp":289.5,"humidity":89,"pressure":1013,"temp_min":287.04,"temp_max":292.04},
    "wind":{"speed":7.31,"deg":187.002},
    "rain":{"3h":0},
    "clouds":{"all":92},
    "dt":1369824698,
    "id":1851632,
    "name":"Shuzenji",
    "cod":200
    }*/

    private final String baseURL = "api.openweathermap.org/data/2.5/weather?lat={#lat}&lon={#lon}&APPID={#id}";
    private final String KEY = "3a95318fedaeaa351e5d73a8c0fdf7d1";

    private float clima_c;
    private float clima_f;
    private float clima_k;
    private boolean hayLluvia;
    private short porcentajeNubes;

    public Clima(double lat, double lon) {
        this.clima_c = 0;
        this.clima_f = 0;
        this.clima_k = 0;
        this.hayLluvia = false;
        this.porcentajeNubes = 0;

        String newURL = generateURL(lat, lon);

        new Clima.DescargaClima().execute(newURL);
    }

    private String generateURL(double lat, double lon) {
        String URL = baseURL.replace("#id", KEY);
        URL = URL.replace("#lat", String.valueOf(lat));
        URL = URL.replace("#lon", String.valueOf(lon));

        return URL;
    }

    public float getClima_c() {
        return clima_c;
    }

    public float getClima_f() {
        return clima_f;
    }

    public float getClima_k() {
        return clima_k;
    }

    public boolean isHayLluvia() {
        return hayLluvia;
    }

    public short getPorcentajeNubes() {
        return porcentajeNubes;
    }

    private InputStream abrirConexion(String direccionRecurso) throws IOException {
        InputStream flujoEntrada = null;
        URL url = new URL(direccionRecurso);
        // Crea el enlace de comunicación entre la app y el url
        URLConnection conexion = url.openConnection();

        if (!(conexion instanceof HttpURLConnection)) {
            throw new IOException("No es una conexión HTTP");
        }

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conexion;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); // Abre el enlace de comunicación
            int respuesta = httpConn.getResponseCode(); // Respuesta

            if (respuesta == HttpURLConnection.HTTP_OK) {
                // Exito en la conexión
                flujoEntrada = httpConn.getInputStream(); // Obtenemos el flujo para leer los datos
            }
        } catch (Exception e) {
            Log.d("Networking", e.getLocalizedMessage());
            throw new IOException("Error conectando a: " + direccionRecurso);
        }

        return flujoEntrada;    // Entrega el flujo que ya se puede leer
    }

    // Descarga un recurso de texto desde la red
    private String descargarJSON(String direccion) {
        int tamBuffer = 2000;   // Paquetes de texto
        InputStream flujoEntrada = null;
        try {
            flujoEntrada = abrirConexion(direccion);  // Estable y abre la conexión
        } catch (IOException e) {
            return "Error en la descarga de " + direccion;
        }

        // Lectura 'normal', como cualquier flujo de entrada
        InputStreamReader isr = new InputStreamReader(flujoEntrada);
        int numCharLeidos;
        StringBuffer contenido = new StringBuffer();
        char[] buffer = new char[tamBuffer];
        try {
            while ((numCharLeidos = isr.read(buffer)) > 0) {    // Mientras lee caracteres
                //convierte el arreglo de caracteres en cadena
                String cadena =
                        String.copyValueOf(buffer, 0, numCharLeidos);
                contenido.append(cadena);
                buffer = new char[tamBuffer];
            }
            flujoEntrada.close();
        } catch (IOException e) {
            return "Error leyendo los datos";
        }
        return contenido.toString();
    }

    private class DescargaClima extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return descargarJSON(urls[0]); // Descarga el contenido
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject diccionario = new JSONObject(result);

                String climaJSON = diccionario.getJSONObject("weather").getString("temp");
                clima_k = Float.parseFloat(climaJSON);

                String lluviaJSON = diccionario.getJSONObject("rain").getString("3h");
                hayLluvia = (lluviaJSON != null && !lluviaJSON.isEmpty());

                String nubesJSON = diccionario.getJSONObject("clouds").getString("all");
                if (nubesJSON != null && !nubesJSON.isEmpty()) {
                    porcentajeNubes = Short.parseShort(nubesJSON);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
