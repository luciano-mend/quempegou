package br.luciano.quempegou;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CadastroEmprestimoActivity extends AppCompatActivity {

    private EditText edtItemEmprestado, edtObservacoes;
    private Spinner spnAmigos;
    private RadioGroup rgbPrioridadeDevolucao;
    private CheckBox chkFragil, chkDevolucao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimo);

        edtItemEmprestado = findViewById(R.id.edtItemEmprestado);
        spnAmigos = findViewById(R.id.spnAmigos);
        rgbPrioridadeDevolucao = findViewById(R.id.rgbPrioridadeDevolucao);
        chkFragil = findViewById(R.id.chkFragil);
        edtObservacoes = findViewById(R.id.edtObservacoes);
        chkDevolucao = findViewById(R.id.chkDevolucao);

        populaSpinner();

    }

    private void populaSpinner() {

        ArrayList<String> amisgosLista = new ArrayList<>();
        amisgosLista.add(getString(R.string.yoda));
        amisgosLista.add(getString(R.string.luke));
        amisgosLista.add(getString(R.string.leia));
        amisgosLista.add(getString(R.string.anakin));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                amisgosLista);

        spnAmigos.setAdapter(adapter);
    }

    public void limparCampos(View view) {

        edtItemEmprestado.setText(null);
        edtObservacoes.setText(null);
        edtItemEmprestado.requestFocus();
        spnAmigos.setSelection(0);
        rgbPrioridadeDevolucao.clearCheck();
        chkFragil.setChecked(false);
        chkDevolucao.setChecked(false);

        Toast.makeText(this,
                R.string.as_entradas_foram_apagadas,
                Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {
        String itemEmprestado = edtItemEmprestado.getText().toString().trim();

        if (itemEmprestado.isEmpty()) {
            Toast.makeText(this,
                    R.string.campo_item_emprestado_nao_preenchido,
                    Toast.LENGTH_LONG).show();

            edtItemEmprestado.requestFocus();
            return;
        }

        String emprestadoPara = spnAmigos.getSelectedItem().toString();

        if (emprestadoPara.isEmpty()) {
            Toast.makeText(this,
                    R.string.nao_existem_amigos_cadastrados,
                    Toast.LENGTH_LONG).show();
            return;
        }

        int radioButtonId = rgbPrioridadeDevolucao.getCheckedRadioButtonId();
        String prioridadeDevolucao;

        if (radioButtonId == R.id.rdbBaixa) {
            prioridadeDevolucao = getString(R.string.prioridade_baixa);
        } else if (radioButtonId == R.id.rdbAlta) {
            prioridadeDevolucao = getString(R.string.prioridade_alta);
        } else {
            Toast.makeText(this,
                    R.string.selecione_uma_prioridade_de_devolucao,
                    Toast.LENGTH_LONG).show();
            return;
        }

        boolean ehFragil = chkFragil.isChecked();
        boolean itemDevolvido = chkDevolucao.isChecked();

        String observacoes = edtObservacoes.getText().toString().trim();

        Toast.makeText(this,
                "Item emprestado: " + itemEmprestado +
                "\n" + "Emprestado para: " + emprestadoPara +
                "\n" + "Prioridade de devolução: " + prioridadeDevolucao +
                "\n" + "É frágil: " + (ehFragil ? "Sim" : "Não") +
                "\n" + "Item devolvido: " + (itemDevolvido ? "Sim" : "Não") +
                "\n" + "Observação: " + (observacoes.isEmpty() ? "Sem preenchimento" : observacoes),
                Toast.LENGTH_LONG).show();
    }

}