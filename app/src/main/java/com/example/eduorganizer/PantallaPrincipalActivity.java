package com.example.eduorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private FirebaseUser user;

    private TextView tvUser,saludoUser;

    private FirebaseFirestore mFirestore;

    private DrawerLayout drawerLayout;
    private ImageView menu;
    private LinearLayout home,notas,tareas,agenda,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        tvUser=findViewById(R.id.usuarioNav);
        mFirestore= FirebaseFirestore.getInstance();
        drawerLayout=findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        notas=findViewById(R.id.Notas);
        tareas=findViewById(R.id.Tareas);
        agenda=findViewById(R.id.Agenda);
        logout=findViewById(R.id.CerrarSesión);
        saludoUser=findViewById(R.id.tvSaludo_PantallaPrincipal);

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
                        tvUser.setText("Usuario: "+usuario);
                        saludoUser.setText("Hola "+usuario);
                    }else{
                        //Si no existe, mandamos el mensaje de que no se encontró
                        Toast.makeText(PantallaPrincipalActivity.this, "No se Encontro Coincidencia", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(PantallaPrincipalActivity.this,TareaActivity.class);
            }
        });
        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PantallaPrincipalActivity.this,MainAgendaActivity.class);
                startActivity(intent);
            }
        });
        //Agregamos un listener al botón para cerrar sesión y que nos mande al Login
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer((GravityCompat.START));
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity,Class secondActivity){
        Intent intent=new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
    public void irTareas(View view){
        Intent cambio= new Intent(PantallaPrincipalActivity.this,TareaActivity.class);
        startActivity(cambio);
        finish();
    }
}