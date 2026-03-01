package br.luciano.quempegou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import br.luciano.quempegou.enums.PrioridadeDevolucao;

public class CadastroEmprestimoActivity extends AppCompatActivity {

    public static final String KEY_ITEM_EMPRESTADO = "KEY_ITEM_EMPRESTADO";
    public static final String KEY_EMPRESTADO_PARA = "KEY_EMPRESTADO_PARA";
    public static final String KEY_PRIORIDADE_DEVOLUCAO = "KEY_PRIORIDADE_DEVOLUCAO";
    public static final String KEY_EH_FRAGIL = "KEY_EH_FRAGIL";
    public static final String KEY_ITEM_DEVOLVIDO = "KEY_ITEM_DEVOLVIDO";
    public static final String KEY_OBSERVACOES = "KEY_OBSERVACOES";

    private EditText edtItemEmprestado, edtObservacoes;
    private Spinner spnAmigos;
    private RadioGroup rgbPrioridadeDevolucao;
    private CheckBox chkFragil, chkDevolucao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimo);

        setTitle(getString(R.string.novo_emprestimo));

        edtItemEmprestado = findViewById(R.id.edtItemEmprestado);
        spnAmigos = findViewById(R.id.spnAmigos);
        rgbPrioridadeDevolucao = findViewById(R.id.rgbPrioridadeDevolucao);
        chkFragil = findViewById(R.id.chkFragil);
        edtObservacoes = findViewById(R.id.edtObservacoes);
        chkDevolucao = findViewById(R.id.chkDevolucao);

        populaSpinner();

    }

    private void populaSpinner() {

        String[] amigosLista = getResources().getStringArray(R.array.amigos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                amigosLista);

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

        int emprestadoPara = spnAmigos.getSelectedItemPosition();

        if (emprestadoPara == AdapterView.INVALID_POSITION) {
            Toast.makeText(this,
                    R.string.nao_existem_amigos_cadastrados,
                    Toast.LENGTH_LONG).show();
            return;
        }

        int radioButtonId = rgbPrioridadeDevolucao.getCheckedRadioButtonId();
        PrioridadeDevolucao prioridadeDevolucao;

        if (radioButtonId == R.id.rdbBaixa) {
            prioridadeDevolucao = PrioridadeDevolucao.BAIXA;
        } else if (radioButtonId == R.id.rdbAlta) {
            prioridadeDevolucao = PrioridadeDevolucao.ALTA;
        } else {
            Toast.makeText(this,
                    R.string.selecione_uma_prioridade_de_devolucao,
                    Toast.LENGTH_LONG).show();
            return;
        }

        boolean ehFragil = chkFragil.isChecked();
        boolean itemDevolvido = chkDevolucao.isChecked();

        String observacoes = edtObservacoes.getText().toString().trim();

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_ITEM_EMPRESTADO, itemEmprestado);
        intentResposta.putExtra(KEY_EMPRESTADO_PARA, emprestadoPara);
        intentResposta.putExtra(KEY_PRIORIDADE_DEVOLUCAO, prioridadeDevolucao.toString());
        intentResposta.putExtra(KEY_EH_FRAGIL, ehFragil);
        intentResposta.putExtra(KEY_ITEM_DEVOLVIDO, itemDevolvido);
        intentResposta.putExtra(KEY_OBSERVACOES, observacoes);

        setResult(CadastroEmprestimoActivity.RESULT_OK, intentResposta);

        //finaliza a activity e a resposta é devolvida
        finish();
    }

}
