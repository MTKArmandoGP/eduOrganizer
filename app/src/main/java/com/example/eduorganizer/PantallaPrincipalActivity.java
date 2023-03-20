package com.example.eduorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PantallaPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnSalir;
    private FirebaseUser user;

    private TextView tvUser;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        auth=FirebaseAuth.getInstance();
        btnSalir=findViewById(R.id.btnSalir);
        user=auth.getCurrentUser();
        tvUser=findViewById(R.id.tvUsuario);
        mFirestore= FirebaseFirestore.getInstance();

        //Se verifica si se ha iniciado sesión
        if(user==null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }else{

            //Guardamos el ID del usuario autenticado
            String userID=user.getUid();
            //Hacemos la refencia a la base de datos donde se enecuentra el nombre de usuario
            DocumentReference docRef=mFirestore.collection("Users").document(userID);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Varificamos qu de verdad exista el Registro
                    if(documentSnapshot.exists()){
                        //Rescatamos el Nombre de Usario ingresado por el Usuario
                        String usuario= documentSnapshot.getString("usuario");
                        //Lo mandamos al textview con el mensaje de Bievenida
                        tvUser.setText("Bienvenido "+usuario);
                    }else{
                        //Si no existe, mandamos el mensaje de que no se encontró
                        Toast.makeText(PantallaPrincipalActivity.this, "No se Encontro Coincidencia", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        //Agregamos un listener al botón para cerrar sesión y que nos mande al Login
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}