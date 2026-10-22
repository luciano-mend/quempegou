package br.luciano.quempegou;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;
import java.util.List;

import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.models.Emprestimo;
import br.luciano.quempegou.models.PrioridadeDevolucao;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;
import br.luciano.quempegou.utils.UtilsAlert;

public class CadastroEmprestimoActivity extends AppCompatActivity {

    public static final String KEY_MODO = "MODO";
    public static final String KEY_ID = "ID";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;


    private EditText edtItemEmprestado, edtObservacoes, edtDataEmprestimo, edtDataDevolucao;
    private Spinner spnAmigos;
    private CheckBox chkFragil, chkDevolucao;
    private RadioGroup rgbPrioridadeDevolucao;
    private RadioButton rdbBaixa, rdbAlta;
    private Button btnIncluirAmigos, btnAmigos;


    private int modo;
    private Emprestimo emprestimoOriginal;
    private long dataEmprestimoEmMillis;
    private Long dataDevolucaoEmMillis;

    private List<Amigo> listaAmigosAtivos;

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
        edtDataEmprestimo = findViewById(R.id.edtDataEmprestimo);
        edtDataDevolucao = findViewById(R.id.edtDataDevolucao);
        btnIncluirAmigos = findViewById(R.id.btnIncluirAmigos);
        btnAmigos = findViewById(R.id.btnAmigos);

        edtDataEmprestimo.setOnClickListener(v -> mostrarDatePicker(true));
        edtDataDevolucao.setOnClickListener(v -> {
            if (chkDevolucao.isChecked()) {
                mostrarDatePicker(false);
            }
        });

        chkDevolucao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            edtDataDevolucao.setEnabled(isChecked);
            if (isChecked) {
                if (dataDevolucaoEmMillis == null) {
                    dataDevolucaoEmMillis = System.currentTimeMillis();
                }
                exibirData(edtDataDevolucao, dataDevolucaoEmMillis);
            } else {
                dataDevolucaoEmMillis = null;
                edtDataDevolucao.setText(null);
            }
        });

        btnIncluirAmigos.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadastroAmigoActivity.class);
            intent.putExtra(CadastroAmigoActivity.KEY_MODO, CadastroAmigoActivity.MODO_NOVO);
            startActivity(intent);
        });

        btnAmigos.setOnClickListener(v -> {
            Intent intent = new Intent(this, AmigosActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.novo_emprestimo));
                dataEmprestimoEmMillis = System.currentTimeMillis();
                dataDevolucaoEmMillis = null;
                exibirData(edtDataEmprestimo, dataEmprestimoEmMillis);
                edtDataDevolucao.setEnabled(false);
                edtDataDevolucao.setText(null);
            } else {
                setTitle(getString(R.string.editar_emprestimo));

                long idEmprestimo = bundle.getLong(KEY_ID);

                EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);
                emprestimoOriginal = database.getEmprestimoDao().getById(idEmprestimo);

                edtItemEmprestado.setText(emprestimoOriginal.getNomeItemEmprestado());

                switch (emprestimoOriginal.getPrioridadeDevolucao()) {
                    case BAIXA:
                        rdbBaixa.setChecked(true);
                        break;
                    case ALTA:
                        rdbAlta.setChecked(true);
                        break;
                }

                if (emprestimoOriginal.isFragil()) {
                    chkFragil.setChecked(true);
                }

                if (emprestimoOriginal.isDevolvido()) {
                    chkDevolucao.setChecked(true);
                }
                edtObservacoes.setText(emprestimoOriginal.getObservacao());

                dataEmprestimoEmMillis = emprestimoOriginal.getDataEmprestimo();
                dataDevolucaoEmMillis = emprestimoOriginal.getDataDevolucao();
                
                exibirData(edtDataEmprestimo, dataEmprestimoEmMillis);
                
                if (emprestimoOriginal.isDevolvido()) {
                    edtDataDevolucao.setEnabled(true);
                    exibirData(edtDataDevolucao, dataDevolucaoEmMillis);
                } else {
                    edtDataDevolucao.setEnabled(false);
                    edtDataDevolucao.setText(null);
                }

                edtItemEmprestado.requestFocus();
                edtItemEmprestado.setSelection(edtItemEmprestado.getText().length());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Long idAmigoSelecionado = null;
        if (modo == MODO_EDITAR && emprestimoOriginal != null) {
            idAmigoSelecionado = emprestimoOriginal.getAmigo();
        } else if (spnAmigos.getSelectedItem() != null) {
            idAmigoSelecionado = ((Amigo) spnAmigos.getSelectedItem()).getId();
        }
        
        carregarSpinnerAmigos(idAmigoSelecionado);
    }

    private void carregarSpinnerAmigos(Long idAmigoSelecionado) {
        EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);
        listaAmigosAtivos = database.getAmigoDao().getAllAtivos();
        
        if (idAmigoSelecionado != null) {
            boolean encontrado = false;
            for (Amigo a : listaAmigosAtivos) {
                if (a.getId().equals(idAmigoSelecionado)) {
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                Amigo amigoInativo = database.getAmigoDao().getById(idAmigoSelecionado);
                if (amigoInativo != null) {
                    listaAmigosAtivos.add(0, amigoInativo);
                }
            }
        }

        ArrayAdapter<Amigo> adapter = new ArrayAdapter<Amigo>(this, android.R.layout.simple_spinner_item, listaAmigosAtivos) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                Amigo amigo = getItem(position);
                tv.setText(amigo.getNome());
                if (!amigo.isAtivo()) {
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                Amigo amigo = getItem(position);
                tv.setText(amigo.getNome());
                if (!amigo.isAtivo()) {
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }
        };
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAmigos.setAdapter(adapter);

        if (idAmigoSelecionado != null) {
            for (int i = 0; i < listaAmigosAtivos.size(); i++) {
                if (listaAmigosAtivos.get(i).getId().equals(idAmigoSelecionado)) {
                    spnAmigos.setSelection(i);
                    break;
                }
            }
        }
    }

    private void mostrarDatePicker(boolean ehEmprestimo) {
        int tituloId = ehEmprestimo ? R.string.data_emprestimo : R.string.data_devolucao;
        MaterialDatePicker<Long> seletorData = MaterialDatePicker.Builder.datePicker()
                .setTitleText(tituloId)
                .setSelection(ehEmprestimo ? dataEmprestimoEmMillis : (dataDevolucaoEmMillis != null ? dataDevolucaoEmMillis : MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        seletorData.addOnPositiveButtonClickListener(selecao -> {
            if (ehEmprestimo) {
                dataEmprestimoEmMillis = selecao;
                exibirData(edtDataEmprestimo, dataEmprestimoEmMillis);
            } else {
                dataDevolucaoEmMillis = selecao;
                exibirData(edtDataDevolucao, dataDevolucaoEmMillis);
            }
        });

        seletorData.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void exibirData(EditText editText, Long millis) {
        if (millis == null) {
            editText.setText(null);
        } else {
            java.text.DateFormat formatador = DateFormat.getDateFormat(this);
            editText.setText(formatador.format(new Date(millis)));
        }
    }

    public void limparCampos() {

        edtItemEmprestado.setText(null);
        edtObservacoes.setText(null);
        edtItemEmprestado.requestFocus();
        spnAmigos.setSelection(0);
        rgbPrioridadeDevolucao.clearCheck();
        chkFragil.setChecked(false);
        chkDevolucao.setChecked(false);
        
        dataEmprestimoEmMillis = System.currentTimeMillis();
        dataDevolucaoEmMillis = null;
        exibirData(edtDataEmprestimo, dataEmprestimoEmMillis);
        edtDataDevolucao.setText(null);
        edtDataDevolucao.setEnabled(false);

        Toast.makeText(this,
                R.string.as_entradas_foram_apagadas,
                Toast.LENGTH_LONG).show();
    }

    public void salvarValores() {
        String itemEmprestado = edtItemEmprestado.getText().toString().trim();

        if (itemEmprestado.isEmpty()) {

            UtilsAlert.mostrarAviso(this,
                    R.string.campo_item_emprestado_nao_preenchido);

            edtItemEmprestado.requestFocus();
            return;
        }

        Amigo amigoSelecionado = (Amigo) spnAmigos.getSelectedItem();

        if (amigoSelecionado == null) {
            UtilsAlert.mostrarAviso(this,
                    R.string.nao_existem_amigos_cadastrados);

            return;
        }

        int radioButtonId = rgbPrioridadeDevolucao.getCheckedRadioButtonId();
        PrioridadeDevolucao prioridadeDevolucao;

        if (radioButtonId == R.id.rdbBaixa) {
            prioridadeDevolucao = PrioridadeDevolucao.BAIXA;
        } else if (radioButtonId == R.id.rdbAlta) {
            prioridadeDevolucao = PrioridadeDevolucao.ALTA;
        } else {
            UtilsAlert.mostrarAviso(this,
                    R.string.selecione_uma_prioridade_de_devolucao);

            return;
        }

        boolean ehFragil = chkFragil.isChecked();
        boolean itemDevolvido = chkDevolucao.isChecked();

        String observacoes = edtObservacoes.getText().toString().trim();

        Emprestimo emprestimo = new Emprestimo(itemEmprestado, amigoSelecionado.getId(),
                                               prioridadeDevolucao, ehFragil,
                                               itemDevolvido, observacoes,
                                               dataEmprestimoEmMillis, dataDevolucaoEmMillis);

        if (emprestimo.equals(emprestimoOriginal)) {
            //não houve alteração
            setResult(CadastroEmprestimoActivity.RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();

        EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);

        if (modo == MODO_NOVO) {
            long novoId = database.getEmprestimoDao().insert(emprestimo);

            if (novoId <= 0) {
                UtilsAlert.mostrarAviso(this,
                        R.string.erro_ao_inserir);
                return;
            }

            emprestimo.setId(novoId);

        } else {
            emprestimo.setId(emprestimoOriginal.getId());

            if (database.getEmprestimoDao().update(emprestimo) != 1) {
                UtilsAlert.mostrarAviso(this,
                        R.string.erro_ao_atualizar);
                return;
            }
        }
        intentResposta.putExtra(KEY_ID, emprestimo.getId());

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
