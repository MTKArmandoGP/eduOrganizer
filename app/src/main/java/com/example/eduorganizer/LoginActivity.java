package com.example.eduorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void irRegistro (View view){

        Intent cambio= new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(cambio);

    }

}