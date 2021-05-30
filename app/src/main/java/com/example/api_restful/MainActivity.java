package com.example.api_restful;

/**
 * @author: dcarvajals
 * */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.api_restful.interfaces.RevistaAPI;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //obejto del boton
        Button btn_consultar_retro;
        Button btn_cponsular_volley;
        btn_consultar_retro = findViewById(R.id.btn_retro);
        btn_cponsular_volley = findViewById(R.id.btn_volley);
        //evento click para el boton
        btn_consultar_retro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener el valor del input de entrada
                EditText txtCode = (EditText)findViewById(R.id.txt_code_retro);
                String code = txtCode.getText().toString();
                if(!code.equals("")) {
                    //realizar la consulta consumiendo la API Retrofit, recibiendo como parametro el id de la revista
                    consultarRevistaRetro(Integer.parseInt(code));
                } else{
                    Toast.makeText(MainActivity.this, "El parametro se encuentra vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cponsular_volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtCode = (EditText)findViewById(R.id.txt_code_volley);
                String code = txtCode.getText().toString();
                if(!code.equals("")) {
                    consultaRevistaVolley(Integer.parseInt(txtCode.getText().toString()));
                }else{
                    Toast.makeText(MainActivity.this, "El parametro se encuentra vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Metodo para cargar los datos de las revistas con la API Retrofit
    public void consultarRevistaRetro (int codigo){
        //instancia para consumir la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://revistas.uteq.edu.ec/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RevistaAPI resApi = retrofit.create(RevistaAPI.class);
        //realizar la peticion https al webservicie
        Call<List<Revista>> call = resApi.consultaRevista(codigo);
        //Objeto call para realizar la conexion asincrona
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                try {
                    //se preguntra si la peticion fue exitosa
                    if(response.isSuccessful()){
                        List<Revista> respueta_revista = response.body();
                        //Declaramos una variable para concatenar todos los datos y mostrarlos en pantalla
                        String datos_concat = "Revistas Encontradas con el código: " + String.valueOf(codigo) +"\n";
                        //Este indicie solament ayudara al no concatenera los caracteres ------ cuando llegue al ultmo item
                        int indice = 0;
                        for(Revista re: respueta_revista){
                            datos_concat += "issue_id: " + re.getIssue_id() + "\n"+
                            "volume: " + re.getVolume() +  "\n"+
                            "number: " + re.getNumber() +  "\n"+
                            "year: " + re.getYear() +  "\n"+
                            "date_published: " + re.getDate_published() + "\n"+
                            "title: " + re.getTitle() + "\n"+
                            "doi: " + re.getDoi() + "\n"+
                            "cover: " + re.getCover() +"\n";

                            if(indice < respueta_revista.size() - 1){
                                datos_concat += "----------------- \n";
                                indice ++;
                            }
                        }
                        //mostrmoas en consola el valor final de la variable
                        Log.d("datos finales =>", datos_concat);
                        //mostramos los datos en la siguiente actividad
                        mostrarDatos(datos_concat, "Consulta realizada consumiendo la API de Retrofit");
                    }
                } catch(Exception ex){
                    Log.d("catch", ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {
                Log.d("Error", "Error de conexión");
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void consultaRevistaVolley (int codigo){
        String url = "https://revistas.uteq.edu.ec/ws/issues.php?j_id=" + codigo;
        Log.d("url => ", url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonRespuesta = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String datos_concat = "Revistas Encontradas con el código: " + String.valueOf(codigo) +"\n";
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObj = new JSONObject(response.get(i).toString());
                                String title = jsonObj.getString("issue_id");

                                datos_concat += "issue_id: " + jsonObj.getString("issue_id") + "\n"+
                                        "volume: " + jsonObj.getString("volume") +  "\n"+
                                        "number: " + jsonObj.getString("number") +  "\n"+
                                        "year: " + jsonObj.getString("issue_id") +  "\n"+
                                        "date_published: " + jsonObj.getString("date_published") + "\n"+
                                        "title: " + jsonObj.getString("title") + "\n"+
                                        "doi: " + jsonObj.getString("doi") + "\n"+
                                        "cover: " + jsonObj.getString("cover") +"\n";
                                datos_concat += i < response.length() - 1 ? "----------------- \n" : "";

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("cathc => ", e.getMessage());
                            }
                        }
                        Log.d("datosfinales", datos_concat);
                        mostrarDatos(datos_concat, "Consulta realizada consumiendo la API de Android Volley");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Log.d("Error", error.getMessage());
                    }
                }
        );
        queue.add(jsonRespuesta);
    }

    public void mostrarDatos(String datos, String titulo){
        Intent intent = new Intent(MainActivity.this, actReponse.class);
        Bundle b = new Bundle();
        //Añadimos la información al intent
        b.putString("datosjson",datos);
        b.putString("titulo",titulo);
        intent.putExtras(b);
        //Iniciamos la siguiente actividad
        startActivity(intent);
    }

}