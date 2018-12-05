package com.example.rivas.salonappadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.rivas.salonappadmin.R;


public class MainActivity extends AppCompatActivity {

    private Button btnIngresarSesion ;
    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnIngresarSesion = (Button) findViewById(R.id.btnIngresarSesion);

        btnIngresarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btnIngresarSesion = new Intent( MainActivity.this, RegistrationActivity.class);
                startActivity(btnIngresarSesion);
            }
        });

        btnIniciar = (Button) findViewById(R.id.btnIniciar);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btnIniciar = new Intent( MainActivity.this, LoginActivity.class);
                startActivity(btnIniciar);
            }
        });

    }

}
