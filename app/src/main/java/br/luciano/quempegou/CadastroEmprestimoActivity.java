package br.luciano.quempegou;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.luciano.quempegou.enums.PrioridadeDevolucao;
import br.luciano.quempegou.models.Emprestimo;

public class CadastroEmprestimoActivity extends AppCompatActivity {

    public static final String KEY_ITEM_EMPRESTADO = "KEY_ITEM_EMPRESTADO";
    public static final String KEY_EMPRESTADO_PARA = "KEY_EMPRESTADO_PARA";
    public static final String KEY_PRIORIDADE_DEVOLUCAO = "KEY_PRIORIDADE_DEVOLUCAO";
    public static final String KEY_EH_FRAGIL = "KEY_EH_FRAGIL";
    public static final String KEY_ITEM_DEVOLVIDO = "KEY_ITEM_DEVOLVIDO";
    public static final String KEY_OBSERVACOES = "KEY_OBSERVACOES";
    public static final String KEY_MODO = "MODO";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;


    private EditText edtItemEmprestado, edtObservacoes;
    private Spinner spnAmigos;
    private CheckBox chkFragil, chkDevolucao;
    private RadioGroup rgbPrioridadeDevolucao;
    private RadioButton rdbBaixa, rdbAlta;


    private int modo;
    private Emprestimo emprestimoOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_emprestimo);

        edtItemEmprestado = findViewById(R.id.edtItemEmprestado);
        spnAmigos = findViewById(R.id.spnAmigos);
        rgbPrioridadeDevolucao = findViewById(R.id.rgbPrioridadeDevolucao);
        chkFragil = findViewById(R.id.chkFragil);
        edtObservacoes = findViewById(R.id.edtObservacoes);
        chkDevolucao = findViewById(R.id.chkDevolucao);
        rdbBaixa = findViewById(R.id.rdbBaixa);
        rdbAlta = findViewById(R.id.rdbAlta);

        populaSpinner();

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.novo_emprestimo));
            } else {
                setTitle(getString(R.string.editar_emprestimo));

                String itemEmprestado = bundle.getString(CadastroEmprestimoActivity.KEY_ITEM_EMPRESTADO);
                int emprestadoPara = bundle.getInt(CadastroEmprestimoActivity.KEY_EMPRESTADO_PARA);
                String prioridadeDevolucaoTexto = bundle.getString(CadastroEmprestimoActivity.KEY_PRIORIDADE_DEVOLUCAO);
                boolean ehFragil = bundle.getBoolean(CadastroEmprestimoActivity.KEY_EH_FRAGIL);
                boolean itemDevolvido = bundle.getBoolean(CadastroEmprestimoActivity.KEY_ITEM_DEVOLVIDO);
                String observacoes = bundle.getString(CadastroEmprestimoActivity.KEY_OBSERVACOES);

                PrioridadeDevolucao prioridadeDevolucao = PrioridadeDevolucao.valueOf(prioridadeDevolucaoTexto);

                emprestimoOriginal = new Emprestimo(
                        itemEmprestado,
                        emprestadoPara,
                        prioridadeDevolucao,
                        ehFragil,
                        itemDevolvido,
                        observacoes);

                edtItemEmprestado.setText(itemEmprestado);
                spnAmigos.setSelection(emprestadoPara);

                switch (prioridadeDevolucao) {
                    case BAIXA:
                        rdbBaixa.setChecked(true);
                        break;
                    case ALTA:
                        rdbAlta.setChecked(true);
                        break;
                }

                if (ehFragil) {
                    chkFragil.setChecked(true);
                }

                if (itemDevolvido) {
                    chkDevolucao.setChecked(true);
                }
                edtObservacoes.setText(observacoes);
            }
        }

    }

    private void populaSpinner() {

        String[] amigosLista = getResources().getStringArray(R.array.amigos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                amigosLista);

        spnAmigos.setAdapter(adapter);
    }

    public void limparCampos() {

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

    public void salvarValores() {
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

        if (modo == MODO_EDITAR &&
                itemEmprestado.equals(emprestimoOriginal.getNomeItemEmprestado()) &&
                emprestadoPara == emprestimoOriginal.getAmigo() &&
                prioridadeDevolucao == emprestimoOriginal.getPrioridadeDevolucao() &&
                ehFragil == emprestimoOriginal.isFragil() &&
                itemDevolvido == emprestimoOriginal.isDevolvido() &&
                observacoes.equals(emprestimoOriginal.getObservacao())) {

            //não houve alteração
            setResult(CadastroEmprestimoActivity.RESULT_CANCELED);
            finish();
            return;
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.mniSalvar) {
            salvarValores();
            return true;
        } else {
            if (idMenuItem == R.id.mniLimpar) {
                limparCampos();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
