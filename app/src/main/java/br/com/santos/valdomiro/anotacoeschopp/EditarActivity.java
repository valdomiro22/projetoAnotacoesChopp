package br.com.santos.valdomiro.anotacoeschopp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

import br.com.santos.valdomiro.anotacoeschopp.controller.DBController;
import br.com.santos.valdomiro.anotacoeschopp.model.Nota;
import br.com.santos.valdomiro.anotacoeschopp.util.AppUtil;

public class EditarActivity extends AppCompatActivity {

    EditText editTextEditarCodigo, editTextEditarHora;
    Button buttonSalvar, buttonVoltar, buttonDeletar;
    String id;

    Nota nota;
    DBController dbController;

    boolean isFormularioOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        inicializarElementos();

        dbController = new DBController(getApplicationContext());

        Intent intent = getIntent();
        editTextEditarCodigo.setText(intent.getStringExtra("codigo"));
        editTextEditarHora.setText(intent.getStringExtra("hora"));
        id = intent.getStringExtra("id");

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFormularioOk = validarFormulario();

                if (isFormularioOk) {
                    nota = new Nota();
                    nota.setCodigo(editTextEditarCodigo.getText().toString());
                    nota.setHora(editTextEditarHora.getText().toString());
                    nota.setId(Integer.parseInt(id));
                    dbController.alterarController(nota);
                    finish();
                } else {
                    validarFormulario();
                }

            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("apagar", "deletarController: Executao controller");
                dbController.deletarController(Integer.parseInt(id));
                finish();

            }
        });
    }

    private void inicializarElementos() {
        editTextEditarCodigo = findViewById(R.id.edit_codigo_editar);
        editTextEditarHora = findViewById(R.id.edit_hora_editar);
        buttonSalvar = findViewById(R.id.button_salvar);
        buttonVoltar = findViewById(R.id.button_voltar);
        buttonDeletar = findViewById(R.id.button_deletar);
    }

    private boolean validarFormulario() {
        boolean retorno = true;

        if (editTextEditarCodigo.length() != 6) {
            editTextEditarCodigo.setError("Quantidade de caracteres inválida");
            editTextEditarCodigo.requestFocus();
            retorno = false;
        }

        return retorno;
    }
}