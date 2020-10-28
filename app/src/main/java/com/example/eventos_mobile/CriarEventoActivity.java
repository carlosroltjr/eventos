package com.example.eventos_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventos_mobile.database.EventosDAO;
import com.example.eventos_mobile.modelo.Evento;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriarEventoActivity extends AppCompatActivity {

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);
        setTitle("Criar Evento");

        carregarEvento();
    }

    private void carregarEvento() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.getExtras().get("eventoEdicao") != null) {

            Evento evento = (Evento) intent.getExtras().get("eventoEdicao");

            EditText editTextNome = findViewById(R.id.editText_nome);
            EditText editTextData = findViewById(R.id.editText_data);
            EditText editTextLocal = findViewById(R.id.editText_local);

            editTextNome.setText(evento.getNome());
            editTextData.setText(String.valueOf(evento.getData()));
            editTextLocal.setText(evento.getLocal());

            id = evento.getId();
        }
    }

    public void onClickVoltar(View v) {
        finish();
    }

    public void onClickSalvar(View v) {
        EditText editTextNome = findViewById(R.id.editText_nome);
        EditText editTextData = findViewById(R.id.editText_data);
        EditText editTextLocal = findViewById(R.id.editText_local);

        String nome = editTextNome.getText().toString();
        String data = editTextData.getText().toString();
        String local = editTextLocal.getText().toString();

        String expressaoRegularData = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(expressaoRegularData);
        Matcher matcher = pattern.matcher(data);

        if (nome.length() < 3) {
            Toast.makeText(CriarEventoActivity.this, "Mínimo de três letras para o nome", Toast.LENGTH_LONG).show();
        } else if (!matcher.find()) {
            Toast.makeText(CriarEventoActivity.this, "Formato de data aceito 12/34/5678", Toast.LENGTH_LONG).show();
        } else if (local.equals("")) {
            Toast.makeText(CriarEventoActivity.this, "Campo Local obrigatório", Toast.LENGTH_LONG).show();
        } else {
            Evento evento = new Evento(id, nome, data, local);
            EventosDAO eventosDAO = new EventosDAO(getBaseContext());

            boolean salvou = eventosDAO.salvar(evento);

            if (salvou) {
                finish();
            } else {
                Toast.makeText(CriarEventoActivity.this, "Erro ao salvar", Toast.LENGTH_LONG).show();
            }
        }
    }
}
