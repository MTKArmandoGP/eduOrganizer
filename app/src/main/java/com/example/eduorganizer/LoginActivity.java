package com.example.eduorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText correo,contraseña;
    private Button iniciar;
    private FirebaseAuth mAuth;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuthG;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

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
        findViewById(R.id.logInGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

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

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {

        FirebaseUser user1=FirebaseAuth.getInstance().getCurrentUser();
        if(user1!=null){
            Intent intent=new Intent(this,PantallaPrincipalActivity.class);
            startActivity((intent));
            Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
            finish();

        }

    }

}