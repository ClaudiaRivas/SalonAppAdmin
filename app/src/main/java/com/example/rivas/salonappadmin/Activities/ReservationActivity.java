package com.example.rivas.salonappadmin.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rivas.salonappadmin.R;


public class ReservationActivity extends AppCompatActivity {

    private Bundle values;
    private TextView txtNombre;
    private TextView txtPrecio;
    private TextView txtDescripcion;
    private ImageView imgServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_);

        txtNombre = (TextView) findViewById(R.id.txtName);
        txtPrecio = (TextView) findViewById(R.id.txtPrice);
        txtDescripcion = (TextView) findViewById(R.id.txtDescription);
        imgServicio = (ImageView)findViewById(R.id.imgService);
        values = getIntent().getExtras();
        txtNombre.setText(values.getString("nombre"));
        txtPrecio.setText("Precio: $".concat(values.getString("precio")));
        txtDescripcion.setText(values.getString("descripcion"));
        imgServicio.setImageResource(values.getInt("imagen"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
