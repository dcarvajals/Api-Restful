package com.example.api_restful;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class actReponse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_reponse);

        TextView txt_datos = (TextView)findViewById(R.id.txt_response);
        TextView lbl_titulo = (TextView)findViewById(R.id.lbl_titulo);
        Bundle bundle = this.getIntent().getExtras();
        txt_datos.setText(bundle.getString("datosjson"));
        lbl_titulo.setText(bundle.getString("titulo"));
    }

    public void regresar (View view){
        Intent intent = new Intent(actReponse.this, MainActivity.class);
        startActivity(intent);
    }
}