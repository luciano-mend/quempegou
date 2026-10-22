package br.luciano.quempegou;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;
import br.luciano.quempegou.utils.UtilsAlert;

public class CadastroAmigoActivity extends AppCompatActivity {

    public static final String KEY_MODO = "MODO";
    public static final String KEY_ID = "ID";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;

    private EditText edtNomeAmigo, edtObservacao;
    private CheckBox chkAtivo;
    private TextView txvIncluidoData;

    private int modo;
    private Amigo amigoOriginal;
    private long dataInclusaoEmMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_amigo);

        edtNomeAmigo = findViewById(R.id.edtNomeAmigo);
        edtObservacao = findViewById(R.id.edtObservacao);
        chkAtivo = findViewById(R.id.chkAtivo);
        txvIncluidoData = findViewById(R.id.txvIncluidoData);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.novo_amigo));
                dataInclusaoEmMillis = System.currentTimeMillis();
                exibirData(dataInclusaoEmMillis);
                chkAtivo.setChecked(true);
            } else {
                setTitle(getString(R.string.editar_amigo));

                long idAmigo = bundle.getLong(KEY_ID);
                EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);
                amigoOriginal = database.getAmigoDao().getById(idAmigo);

                edtNomeAmigo.setText(amigoOriginal.getNome());
                edtObservacao.setText(amigoOriginal.getObservacao());
                chkAtivo.setChecked(amigoOriginal.isAtivo());
                dataInclusaoEmMillis = amigoOriginal.getDataInclusao();
                exibirData(dataInclusaoEmMillis);

                edtNomeAmigo.requestFocus();
                edtNomeAmigo.setSelection(edtNomeAmigo.getText().length());
            }
        }
    }

    private void exibirData(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txvIncluidoData.setText(sdf.format(new Date(millis)));
    }

    public void salvarValores() {
        String nome = edtNomeAmigo.getText().toString().trim();

        if (nome.isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.campo_nome_amigo_nao_preenchido);
            edtNomeAmigo.requestFocus();
            return;
        }

        String observacao = edtObservacao.getText().toString().trim();
        boolean ativo = chkAtivo.isChecked();

        Amigo amigo = new Amigo(nome, observacao, dataInclusaoEmMillis, ativo);

        if (amigo.equals(amigoOriginal)) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();
        EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);

        if (modo == MODO_NOVO) {
            long novoId = database.getAmigoDao().insert(amigo);

            if (novoId <= 0) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_inserir);
                return;
            }
            amigo.setId(novoId);
        } else {
            amigo.setId(amigoOriginal.getId());

            if (database.getAmigoDao().update(amigo) != 1) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_atualizar);
                return;
            }
        }

        intentResposta.putExtra(KEY_ID, amigo.getId());
        setResult(RESULT_OK, intentResposta);
        finish();
    }

    public void limparCampos() {
        edtNomeAmigo.setText(null);
        edtObservacao.setText(null);
        chkAtivo.setChecked(true);
        edtNomeAmigo.requestFocus();

        Toast.makeText(this, R.string.as_entradas_foram_apagadas, Toast.LENGTH_LONG).show();
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
        } else if (idMenuItem == R.id.mniLimpar) {
            limparCampos();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
