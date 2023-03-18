package com.example.eduorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText correo,contraseña;
    private Button iniciar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        correo=findViewById(R.id.txtEmail);
        contraseña=findViewById(R.id.txtContraseña);
        iniciar=findViewById(R.id.btnIniciar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {
                    // Código para registrar usuario
                }
            }
        });
    }

    private boolean validarCampos() {
        boolean camposLlenos = true;

        if (TextUtils.isEmpty(correo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese su email", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        if (TextUtils.isEmpty(contraseña.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        return camposLlenos;
    }
    public void irRegistro (View view){

        Intent cambio= new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(cambio);

    }

}