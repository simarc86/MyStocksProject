package com.simarc86.mystocks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String TAG = "MyStocks";
    private static SharedPreferences preferences;
    private ArrayList<Stock> local_stocks_list = new ArrayList<Stock>();
    private ArrayList<Stock> selected_stocks_list = new ArrayList<Stock>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        //getDDBB();             //TODO: Carga la DDBB


//        addStock(stock);           //Añade un valor a local

       // loadStocks();           //Obiene los datos de local




        formatJSON(getJSON());  // TODO: Obtiene la info del JSON

        paintTable();          //TODO: Pinta la tabla de los valores obtenidos


        //refreshLocalValues();  TODO: refresca los parametros de los valores en local

        //refreshTable();        TODO: refresca la tabla mostrada en pantalla

    }

    private void formatJSON(JSONArray jsonArray) {

        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Creamos una variable de tipo revista para almacenar los datos de esta revista
            Stock stock = new Stock();

            try {
                // Almacenamos los datos de las revistas


                stock.setName(jsonObject.getString("n"));
                stock.setPrice(jsonObject.getString("p"));
                stock.setVar(jsonObject.getString("v"));
                stock.setVar_net(jsonObject.getString("vn"));
                stock.setVolume(jsonObject.getString("vl"));
                stock.setDate(jsonObject.getString("h"));



            } catch (JSONException e) {
                e.printStackTrace();
            }

            local_stocks_list.add(stock);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private JSONArray getJSON(){
        //nombre, precio, variación neta, variación, volumen, hora

        JSONArray jsonArray = null;
        String url = "http://blooming-sierra-4986.herokuapp.com/info.json";
        String stringJSON = null; // Variable en la que almacenamos el fichero que tiene la informacion del catalogo de revistas

        stringJSON = readJSON(url);


        if (!stringJSON.equals(null)){
            try {
                jsonArray = new JSONArray(stringJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Inicializamos variable con cantidad de categorias.

        }

        return jsonArray;
    }

    /**
     * Descarga JSON
     *
     * @param urlDescarga
     *            String que contiene la url del fichero JSON donde se encuentra en la nube
     * @return String con el contenido del fichero JSON que contiene la informacion del catalogo
     */
    private String readJSON(String urlDescarga) {
        StringBuilder builder = new StringBuilder(); // Costructor de cadena de caracteres donde almacenaremos el resultado obtenido
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urlDescarga);

        try {
            // Obtenemos la respuesta del cliente al conectar con la url que le pasamos
            HttpResponse response = client.execute(httpGet);
            // Obtenemos el estado de la conexion
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            // Si la conexion se ha realizado correctamente obtenemos los datos del fichero
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                // Si no indicamos el error
            }
        } catch (ClientProtocolException e) {
            Log.w(TAG, "Error en ejecucion de obtenerInfoCatalogos (Conexion): " + e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            Log.w(TAG, "Error en ejecucion de obtenerInfoCatalogos (Fichero): " + e.getMessage());
            e.printStackTrace();
        }
        return builder.toString();

    } // Fin leerJSON()

    private void getDDBB(){
        SQLHelper baseDatos = new SQLHelper(this);
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        if (db != null) {
            db.close();
        } else {
            Log.w(TAG + ".bbdd", "bbdd no abierta");
        }
        baseDatos.close();

    }

    private void addStock(Stock stock){
        SharedPreferences.Editor editor;
        preferences = getSharedPreferences("MyStocks", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(stock.getName(),stock.getName() + ";" + stock.getPrice() + ";" + stock.getVar_net() + ";" + stock.getVar() + ";" + stock.getVolume() + ";" + stock.getDate());
        editor.commit();
    }

    private void loadStocks(){
        SharedPreferences.Editor editor;

        // inicializamos las preferencias
        preferences = getSharedPreferences("MyStocks", Context.MODE_PRIVATE);

        preferences.getAll();


        Map<String,?> keys = preferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){

            parseString((String) entry.getValue());
        }


    }

    private void parseString(String local_string) {
        String[] rows = local_string.split(";");

        Stock stock = new Stock();
        stock.setName(rows[0]);
        stock.setPrice(rows[1]);
        stock.setVar_net(rows[2]);
        stock.setVar(rows[3]);
        stock.setVolume(rows[4]);
        stock.setDate(rows[5]);

        local_stocks_list.add(stock);

    }

    private void paintTable() {
        TableLayout table = (TableLayout) findViewById(R.id.table_view);                //Tabla

        RelativeLayout relativeLayout_title = (RelativeLayout)getLayoutInflater().inflate(R.layout.row_layout, null);

        //Name
        TextView name_view_title = (TextView)relativeLayout_title.findViewById(R.id.name_field);
        name_view_title.setText("Index");


        //Price
        TextView price_view_title = (TextView)relativeLayout_title.findViewById(R.id.price_field);
        price_view_title.setText("Price");

        //Var
        TextView var_view_title = (TextView)relativeLayout_title.findViewById(R.id.var_field);
        var_view_title.setText("Var");

        //Var_net
        TextView var_net_view_title = (TextView)relativeLayout_title.findViewById(R.id.var_net_field);
        var_net_view_title.setText("Var%");

        //Var_net
        TextView date_view_title = (TextView)relativeLayout_title.findViewById(R.id.date_field);
        date_view_title.setText("Date");




        table.addView(relativeLayout_title);






        for(int i=0;i<local_stocks_list.size();i++)
        {
            Stock stock = local_stocks_list.get(i);
            RelativeLayout relativeLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.row_layout, null);




            //Name
            TextView name_view = (TextView)relativeLayout.findViewById(R.id.name_field);
            name_view.setText(stock.getName());


            //Price
            TextView price_view = (TextView)relativeLayout.findViewById(R.id.price_field);
            price_view.setText(stock.getPrice());

            //Var
            TextView var_view = (TextView)relativeLayout.findViewById(R.id.var_field);
            var_view.setText(stock.getVar());

            //Var_net
            TextView var_net_view = (TextView)relativeLayout.findViewById(R.id.var_net_field);
            var_net_view.setText(stock.getVar_net());

            //Var_net
            TextView date_view = (TextView)relativeLayout.findViewById(R.id.date_field);
            date_view.setText(stock.getDate());

            if (Double.parseDouble(stock.getVar().replace(",", "."))<0){
                relativeLayout.setBackgroundColor(Color.GREEN);
            }else if (Double.parseDouble(stock.getVar().replace(",", "."))>0){
                relativeLayout.setBackgroundColor(Color.RED);
            }

            relativeLayout.setTag(stock.getName());

            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setBackgroundColor(Color.CYAN);

                    while (local_stocks_list.iterator().hasNext()){
                        if (local_stocks_list.iterator().next().getName().equals(v.getTag())){
                            selected_stocks_list.add(local_stocks_list.iterator().next());
                            selected_stocks_list.size();
                        }
                    };

                    return true;
                }
            });


            table.addView(relativeLayout);




        }

    }



    public void valueSelected(View v){

        String tag = (String)v.getTag();
        tag.toString();
    }

}
