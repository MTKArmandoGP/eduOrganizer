package com.example.eduorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {
    private EditText txtNombre,txtCorreo,txtContra,txtConContra;
    private Button btnResgistrar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth=FirebaseAuth.getInstance();
        //Relación con la prate gráfica
        txtNombre=findViewById(R.id.txtNombreCompleto);
        txtCorreo=findViewById(R.id.txt_Email);
        txtContra=findViewById(R.id.txt_Contraseña);
        txtConContra=findViewById(R.id.txt_Contraseña2);
        btnResgistrar=findViewById(R.id.btn_Registrarme);

       btnResgistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,contraseña;
                contraseña=txtContra.getText().toString();
                email=txtCorreo.getText().toString();
                if (validarCampos()) {

                    mAuth.createUserWithEmailAndPassword(email, contraseña)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(RegistroActivity.this, "Cuenta Creada",
                                                Toast.LENGTH_SHORT).show();


                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(RegistroActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }

            }
        });
    }
    private boolean validarCampos() {
        boolean camposLlenos = true;

        if (TextUtils.isEmpty(txtNombre.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese su nombre completo", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        if (TextUtils.isEmpty(txtCorreo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese su email", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        if (TextUtils.isEmpty(txtContra.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        if (TextUtils.isEmpty(txtConContra.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Confirme su contraseña", Toast.LENGTH_SHORT).show();
            camposLlenos = false;
        }

        return camposLlenos;
    }
    public void irLogin (View view){

        Intent cambio= new Intent(RegistroActivity.this,LoginActivity.class);
        startActivity(cambio);
        finish();

    }
}