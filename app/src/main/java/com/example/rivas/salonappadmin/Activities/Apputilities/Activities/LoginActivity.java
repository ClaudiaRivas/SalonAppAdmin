package com.example.rivas.salonappadmin.Activities.Apputilities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rivas.salon_rmr.R;

public class LoginActivity extends AppCompatActivity {


    //comentario
    //otra prueba
    private EditText userName;
    private EditText userPassword;
    private Button btnIngresar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        userName= findViewById(R.id. userName);
        userPassword = findViewById(R.id.userPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = userName.getText().toString();
                String password = userPassword.getText().toString();

                if(!usuario.isEmpty() && !password.isEmpty()){
                    Intent btnIngresar = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(btnIngresar);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Campos vacios", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
