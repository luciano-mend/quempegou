package br.luciano.quempegou;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroEmprestimoActivity extends AppCompatActivity {

    private EditText edtItemEmprestado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emprestimo);

        edtItemEmprestado = findViewById(R.id.edtItemEmprestado);

    }

    public void limparCampos(View view) {

        edtItemEmprestado.setText(null);
        edtItemEmprestado.requestFocus();

        Toast.makeText(this,
                R.string.as_entradas_foram_apagadas,
                Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {
        String itemEmprestado = edtItemEmprestado.getText().toString().trim();

        if (itemEmprestado == null || itemEmprestado.isEmpty()) {
            Toast.makeText(this,
                    R.string.campo_item_emprestado_nao_preenchido,
                    Toast.LENGTH_LONG).show();

            edtItemEmprestado.requestFocus();
            return;
        }

        Toast.makeText(this,
                "Item emprestado: " + itemEmprestado,
                Toast.LENGTH_LONG).show();
    }

}