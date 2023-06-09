package com.example.eduorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AgregarTareaActivity extends AppCompatActivity {
    private EditText editTextTarea, editTextFecha, editTextHora;
    private Button btnConfirmar;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private BaseDatos baseDatos;

    private Button btnCancelar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);
        editTextTarea = findViewById(R.id.edit_text_tarea);
        editTextFecha = findViewById(R.id.edit_text_fecha);
        editTextHora = findViewById(R.id.edit_text_hora);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        baseDatos = new BaseDatos(this);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AgregarTareaActivity.this, "Tarea no guardada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgregarTareaActivity.this, TareaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int anio = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AgregarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                        editTextFecha.setText(dia + "/" + (mes+1) + "/" + anio);
                    }
                }, anio, mes, dia);

                datePickerDialog.show();
            }
        });

        editTextHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AgregarTareaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                        editTextHora.setText(hora + ":" + minuto);
                    }
                }, hora, minuto, false);

                timePickerDialog.show();
            }
        });
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tarea = editTextTarea.getText().toString();
                String fecha = editTextFecha.getText().toString();
                String hora = editTextHora.getText().toString();

                if (TextUtils.isEmpty(tarea) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(hora)) {
                    Toast.makeText(AgregarTareaActivity.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    long resultado = baseDatos.agregarTarea(tarea, fecha, hora);

                    if (resultado > 0) {
                        Toast.makeText(AgregarTareaActivity.this, "Tarea agregada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AgregarTareaActivity.this, "Error al agregar la tarea", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AgregarTareaActivity.this, TareaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}