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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    private EditText txtUsuario,txtCorreo,txtContra,txtConContra;
    private Button btnResgistrar;
    private FirebaseAuth mAuth;
     private FirebaseFirestore mFirestore;

     //Cuando s einicie la pantalla verificamos si ya se ha autenticado el usuario
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
        setContentView(R.layout.activity_registro);

        mAuth=FirebaseAuth.getInstance();
        //Relación con la prate gráfica
        txtUsuario=findViewById(R.id.txtNombreCompleto);
        txtCorreo=findViewById(R.id.txt_Email);
        txtContra=findViewById(R.id.txt_Contraseña);
        txtConContra=findViewById(R.id.txt_Contraseña2);
        btnResgistrar=findViewById(R.id.btn_Registrarme);
        mFirestore=FirebaseFirestore.getInstance();


        //Agregamos un listener al boton de registrar,que llama al método regsiter
       btnResgistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register ();
            }
        });
    }

    //Definimos todo el método register
    private void register(){
        String usuario=txtUsuario.getText().toString();
        String mail=txtCorreo.getText().toString();
        String password=txtContra.getText().toString();
        String confirmPassword=txtConContra.getText().toString();
        //Verificamos que no haya campos vacios
        if(!usuario.isEmpty() && !mail.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
            //Verificamos que se haya ingresado un correo valido con el metodo "isEmailValid"
            if(isEmailValid(mail)){
                //Verificamos que ambas contraseñas ingresadas sean iguales
                if(password.equals(confirmPassword)){
                    //Definimos que la longitud minima de la contraseña sea de 6
                    if(password.length() >= 6){
                        //enviamos los datos de usuario,mail y password al método "createUser"
                    createUser(usuario,mail,password);
                    }else{
                        //Si la contraseña no tiene minimo 6 caracteres manda un error
                        Toast.makeText(RegistroActivity.this, "La Contraseña Debe Tener Almenos 6 Caracteres", Toast.LENGTH_SHORT).show();
                        txtContra.setError("Minimo 6 Caracteres");
                        txtContra.requestFocus();
                    }
                }else{
                    //Si no coinciden las contraseñas mandamos un error
                    Toast.makeText(RegistroActivity.this, "Las Contraseñas No Coinciden", Toast.LENGTH_SHORT).show();
                    txtConContra.setError("Las Contraseñas no Coinciden");
                    txtConContra.requestFocus();
                }
            }else{
                //Si el correo no tiene un formato valido se manda el error
                Toast.makeText(RegistroActivity.this, "Ingresa Un Correo Valido", Toast.LENGTH_SHORT).show();
                txtCorreo.setError("Ingrese un Correo Valido");
                txtCorreo.requestFocus();
            }
            //Si hay campos vacios va a mandar alertas de que campos faltan
        }else{
            if (TextUtils.isEmpty(txtUsuario.getText().toString())) {
                txtUsuario.setError("Ingrese su nombre");
                txtUsuario.requestFocus();
            }if (TextUtils.isEmpty(txtCorreo.getText().toString())) {
                txtCorreo.setError("Ingrese su Correo");
                txtCorreo.requestFocus();
            }if (TextUtils.isEmpty(txtContra.getText().toString())) {
                txtContra.setError("Ingrese una Contraseña");
                txtContra.requestFocus();
            }if (TextUtils.isEmpty(txtConContra.getText().toString())) {
                //Toast.makeText(getApplicationContext(), "Confirme su contraseña", Toast.LENGTH_SHORT).show();
                txtConContra.setError("Confirme la Contraseña");
                txtConContra.requestFocus();
            }
            Toast.makeText(RegistroActivity.this, "Llena Los Campos Para Continuar", Toast.LENGTH_SHORT).show();

        }
    }

    //Definimos el método que creara o dara de alta al usuario
    private void createUser(String usuario, String mail, String password) {
        //Decimos que va a crear un usuario para la parte de autenticación con Correo y Contraseña
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si se guardo el usuario entonces
                if(task.isSuccessful()){
                    //va a guardar el id del usuario
                    String id = mAuth.getCurrentUser().getUid();
                    //Y va a guardar los campos de usuario, mail y password para guardarlos en la base de datos
                    Map<String,Object> map =new HashMap<>();
                    map.put("usuario",usuario);
                    map.put("mail",mail);
                    map.put("password",password);

                    //Con fire store vamos a hacer el alta de información en la colleción de Users con el nombre o identificador guardado en la variable id
                    mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //si se realizo el alta exitosamente nos manda al login
                          if(task.isSuccessful()){
                              Intent i=new Intent(RegistroActivity.this,LoginActivity.class);
                              startActivity(i);
                              finish();
                              Toast.makeText(RegistroActivity.this, "El Usuario se Almaceno Correctamente", Toast.LENGTH_SHORT).show();

                          }
                        }
                    });

                }else{
                    Toast.makeText(RegistroActivity.this, "No Se Pudo Registrar El Usuario", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    //definimos el metodo para validar el correo
    public boolean isEmailValid(String mail){
        //con la exresión regular defiminos que tiene que tener un @ y un punto para tomarlo como valido
        String expression="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern= Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(mail);
        return matcher.matches();
    }

    //metodo para ir a la Pantalla de login
    public void irLogin (View view){

        Intent cambio= new Intent(RegistroActivity.this,LoginActivity.class);
        startActivity(cambio);
        finish();

    }
}