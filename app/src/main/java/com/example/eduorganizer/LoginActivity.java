package com.example.eduorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText correo,contraseña;
    private Button iniciar;
    private FirebaseAuth mAuth;

    //Cuando se inicie la pantalla verificamos si ya se ha autenticado el usuario
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),PantallaPrincipalActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        correo=findViewById(R.id.txtEmail);
        contraseña=findViewById(R.id.txtContraseña);
        iniciar=findViewById(R.id.btnIniciar);

        //Agregamos un listener al boton de iniciar para poder validar las credenciales
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardamos lo obtenido de los campos en las variables String
                String email,password;
                email=correo.getText().toString();
                password=contraseña.getText().toString();
                //Verificamos que no haya campos vacios con el metodo "validarCampos"
                if (validarCampos()) {
                    //decimos que queremos ingresar con la autenticación de Correo y Contraseña y le mandamos las variables String
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Si la autentificación fue exitosa, nos manda el mensaje de confirmación y nos lleva a la pantalla principal
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getApplicationContext(), "Campos Correctos", Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getApplicationContext(),PantallaPrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //Si los datos no coinciden con la base de datos, nos manda el mensaje de error
                                        Toast.makeText(LoginActivity.this, "Datos Incorrectos",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    //definimos el métdo de validarCampos
    private boolean validarCampos() {
        boolean camposLlenos = true;
        //Verificamos que no haya campos vacios
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
        finish();

    }

}