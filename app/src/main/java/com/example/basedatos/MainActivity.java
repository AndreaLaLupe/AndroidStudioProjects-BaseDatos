package com.example.basedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    //Obtener datos
    Spinner ContadorOpciones;
    DatePicker pickerDate;
    TimePicker pickerTime;
    Button btnGetDate, btnGetTime, btnGuardarDatos;
    TextView tvwDate, tvwTime;

    //Guardar informacion
    Button btnGuardar;
    EditText etDato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Desplegar datos de los numeros del contador
        ContadorOpciones = (Spinner) findViewById(R.id.opciones);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ContadorOpciones, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ContadorOpciones.setAdapter(adapter);

        //Obtener fecha por calendario
        tvwDate = (TextView) findViewById(R.id.textViewDate);
        pickerDate = (DatePicker) findViewById(R.id.datePicker1);
        btnGetDate = (Button) findViewById(R.id.buttonDate);
        btnGetDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvwDate.setText(pickerDate.getDayOfMonth() + "/" + (pickerDate.getMonth() + 1) + "/" + pickerDate.getYear());
            }
        });

        //Obtener hora
        tvwTime = (TextView) findViewById(R.id.textViewTime);
        pickerTime = (TimePicker) findViewById(R.id.timePicker1);
        pickerTime.setIs24HourView(true);
        btnGetTime = (Button) findViewById(R.id.buttonTime);
        btnGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = pickerTime.getHour();
                    minute = pickerTime.getMinute();
                }
                else{
                    hour = pickerTime.getCurrentHour();
                    minute = pickerTime.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                tvwTime.setText(hour +":"+ minute+" "+am_pm);
            }
        });

        //Guadar informacion
        btnGuardar=(Button)findViewById(R.id.Guardar);
        btnGuardarDatos=(Button)findViewById(R.id.GuardarBase);
        etDato=(EditText)findViewById(R.id.etDato);

        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("http://-IP-/DatosContadores/Guardar.php");
            }
        });


        final AyudaDB ayudadb= new AyudaDB(getApplicationContext());
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = ayudadb.getWritableDatabase();
                ContentValues valores= new ContentValues();
                valores.put(AyudaDB.DatosTabla.COLUMNA_OPCIONES, ContadorOpciones.getSelectedItem().toString());
                valores.put(AyudaDB.DatosTabla.COLUMNA_FECHA, tvwDate.getText().toString());
                valores.put(AyudaDB.DatosTabla.COLUMNA_HORA, tvwTime.getText().toString());
                valores.put(AyudaDB.DatosTabla.COLUMNA_DATO, etDato.getText().toString());

                Long Guadado=db.insert(AyudaDB.DatosTabla.TABLE_NAME,AyudaDB.DatosTabla.COLUMNA_OPCIONES,valores);
                Toast.makeText(getApplicationContext(),"Guardado: "+Guadado,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
            }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("Opciones",ContadorOpciones.getSelectedItem().toString());
                parametros.put("Fecha",tvwDate.getText().toString());
                parametros.put("Hora",tvwTime.getText().toString());
                parametros.put("Dato",etDato.getText().toString());

                return super.getParams();
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
