package com.example.rivas.salonappadmin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rivas.salonappadmin.R;


public class RegistrationActivity extends AppCompatActivity {


    private Button btnGuardar;
    private EditText userNombre;
    private EditText userApellido;
    private EditText userEmail;
    private EditText userContrasena;
    private EditText userTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnGuardar = findViewById(R.id.btnGuardar);
        userNombre = findViewById(R.id.userNombre);
        userApellido = findViewById(R.id.userApellido);
        userEmail = findViewById(R.id.userEmail);
        userContrasena = findViewById(R.id.userContrasena);
        userTelefono = findViewById(R.id.userTelefono);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = userNombre.getText().toString();
                String apellido = userApellido.getText().toString();
                String correo = userEmail.getText().toString();
                String password = userContrasena.getText().toString();
                String telefono = userTelefono.getText().toString();

                if (!usuario.isEmpty() && !apellido.isEmpty() && !correo.isEmpty() && !password.isEmpty() && !telefono.isEmpty()) {
                    Intent btnGuardar = new Intent(RegistrationActivity.this, PrincipalActivity.class);
                    startActivity(btnGuardar);
                } else {
                    Toast.makeText(getApplicationContext(), "Campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
