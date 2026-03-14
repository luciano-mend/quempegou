package br.luciano.quempegou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.luciano.quempegou.adapters.EmprestimoAdapter;
import br.luciano.quempegou.enums.PrioridadeDevolucao;
import br.luciano.quempegou.models.Emprestimo;

public class EmprestimosActivity extends AppCompatActivity {

    private ListView lsvEmprestimos;
    private List<Emprestimo> listaEmprestimos;

    private EmprestimoAdapter emprestimoAdapter;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    public static final String ARQUIVO_PREFERENCIAS = "br.luciano.quempegou.PREFERENCIAS";

    private boolean ordenacaoNaoDevolvidos = false;

    public static final String KEY_ORDENACAO_NAO_DEVOLVIDOS = "ORDENACAO_NAO_DEVOLVIDOS";

    private final ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.emprestimos_item_selecionado, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.mniEditar) {
                editarEmprestimo();
                return true;
            } else {
                if (idMenuItem == R.id.mniExcluir) {
                    excluirEmprestimo();
                    mode.finish();
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackground(backgroundDrawable);
            }
            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;
            posicaoSelecionada = -1;
            lsvEmprestimos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimos);

        setTitle(getString(R.string.controle_de_emprestimos));

        lerPreferencias();

        lsvEmprestimos = findViewById(R.id.lsvEmprestimos);

        lsvEmprestimos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                posicaoSelecionada = posicao;

                editarEmprestimo();
            }
        });

        lsvEmprestimos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                if (actionMode != null) {
                    return false;
                }
                posicaoSelecionada = posicao;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();
                view.setBackgroundColor(ContextCompat.getColor(EmprestimosActivity.this, R.color.azul_selecionado));

                lsvEmprestimos.setEnabled(false);

                actionMode = startSupportActionMode(actionCallback);


                return true;
            }
        });
        
        popularEmprestimos();

        registerForContextMenu(lsvEmprestimos);
    }

    private void popularEmprestimos() {

        listaEmprestimos = new ArrayList<>();

        emprestimoAdapter = new EmprestimoAdapter(this,
                listaEmprestimos);

        lsvEmprestimos.setAdapter(emprestimoAdapter);
    }

    public void abrirSobre() {

        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherNovoEmprestimo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            String itemEmprestado = bundle.getString(CadastroEmprestimoActivity.KEY_ITEM_EMPRESTADO);
                            int emprestadoPara = bundle.getInt(CadastroEmprestimoActivity.KEY_EMPRESTADO_PARA);
                            String prioridadeDevolucao = bundle.getString(CadastroEmprestimoActivity.KEY_PRIORIDADE_DEVOLUCAO);
                            boolean ehFragil = bundle.getBoolean(CadastroEmprestimoActivity.KEY_EH_FRAGIL);
                            boolean itemDevolvido = bundle.getBoolean(CadastroEmprestimoActivity.KEY_ITEM_DEVOLVIDO);
                            String observacoes = bundle.getString(CadastroEmprestimoActivity.KEY_OBSERVACOES);

                            Emprestimo emprestimo = new Emprestimo(itemEmprestado,
                                    emprestadoPara,
                                    PrioridadeDevolucao.valueOf(prioridadeDevolucao),
                                    ehFragil,
                                    itemDevolvido,
                                    observacoes);

                            listaEmprestimos.add(emprestimo);

                            definirOrdenacao();
                        }
                    }
                }
            });

    public void abrirNovoEmprestimo() {
        Intent intent = new Intent(this, CadastroEmprestimoActivity.class);

        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);

        launcherNovoEmprestimo.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.emprestimos_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.mniIncluir) {
            abrirNovoEmprestimo();
            return true;
        } else {
            if (idMenuItem == R.id.mniSobre) {
                abrirSobre();
                return true;
            } if (idMenuItem == R.id.mniOrdenarNaoDevolvidos) {
                boolean valor = !item.isChecked();

                salvarPreferencias(valor);
                item.setChecked(valor);

                if (!listaEmprestimos.isEmpty()) {
                    definirOrdenacao();
                }

                return true;
            } if (idMenuItem == R.id.mniRestaurar) {
                restaurarPadroes();
                definirOrdenacao();

                Toast.makeText(this,
                        getString(R.string.configuracoes_padroes_restauradas),
                        Toast.LENGTH_SHORT).show();

                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    ActivityResultLauncher<Intent> launcherEditarEmprestimo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            String itemEmprestado = bundle.getString(CadastroEmprestimoActivity.KEY_ITEM_EMPRESTADO);
                            int emprestadoPara = bundle.getInt(CadastroEmprestimoActivity.KEY_EMPRESTADO_PARA);
                            String prioridadeDevolucao = bundle.getString(CadastroEmprestimoActivity.KEY_PRIORIDADE_DEVOLUCAO);
                            boolean ehFragil = bundle.getBoolean(CadastroEmprestimoActivity.KEY_EH_FRAGIL);
                            boolean itemDevolvido = bundle.getBoolean(CadastroEmprestimoActivity.KEY_ITEM_DEVOLVIDO);
                            String observacoes = bundle.getString(CadastroEmprestimoActivity.KEY_OBSERVACOES);

                            Emprestimo emprestimo = listaEmprestimos.get(posicaoSelecionada);
                            emprestimo.setNomeItemEmprestado(itemEmprestado);
                            emprestimo.setAmigo(emprestadoPara);
                            emprestimo.setPrioridadeDevolucao(PrioridadeDevolucao.valueOf(prioridadeDevolucao));
                            emprestimo.setFragil(ehFragil);
                            emprestimo.setDevolvido(itemDevolvido);
                            emprestimo.setObservacao(observacoes);

                            definirOrdenacao();
                        }

                    }

                    posicaoSelecionada = -1;

                    if (actionMode != null) {
                        actionMode.finish();
                    }

                }
            });

    private void editarEmprestimo() {
        Emprestimo emprestimo = listaEmprestimos.get(posicaoSelecionada);

        Intent intent = new Intent(this, CadastroEmprestimoActivity.class);

        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_EDITAR);
        intent.putExtra(CadastroEmprestimoActivity.KEY_ITEM_EMPRESTADO, emprestimo.getNomeItemEmprestado());
        intent.putExtra(CadastroEmprestimoActivity.KEY_EMPRESTADO_PARA, emprestimo.getAmigo());
        intent.putExtra(CadastroEmprestimoActivity.KEY_PRIORIDADE_DEVOLUCAO, emprestimo.getPrioridadeDevolucao().toString());
        intent.putExtra(CadastroEmprestimoActivity.KEY_EH_FRAGIL, emprestimo.isFragil());
        intent.putExtra(CadastroEmprestimoActivity.KEY_ITEM_DEVOLVIDO, emprestimo.isDevolvido());
        intent.putExtra(CadastroEmprestimoActivity.KEY_OBSERVACOES, emprestimo.getObservacao());

        launcherEditarEmprestimo.launch(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.mniOrdenarNaoDevolvidos).setChecked(ordenacaoNaoDevolvidos);
        return true;
    }

    private void excluirEmprestimo() {
        listaEmprestimos.remove(posicaoSelecionada);
        emprestimoAdapter.notifyDataSetChanged();
    }

    private void lerPreferencias() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);

        // busco a chave, caso ela não exista ela será criada com o valor da variável, pois ela já contem o valor false por padrão
        ordenacaoNaoDevolvidos = shared.getBoolean(KEY_ORDENACAO_NAO_DEVOLVIDOS, ordenacaoNaoDevolvidos);
    }

    private void salvarPreferencias(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(KEY_ORDENACAO_NAO_DEVOLVIDOS, novoValor);
        editor.apply();

        ordenacaoNaoDevolvidos = novoValor;
    }

    private void definirOrdenacao() {
        if (ordenacaoNaoDevolvidos) {
            Collections.sort(listaEmprestimos, Emprestimo.ordenacaoNaoDevolvidos);
        } else {
            Collections.sort(listaEmprestimos, Emprestimo.ordenacaoCrescente);
        }

        emprestimoAdapter.notifyDataSetChanged();
    }

    private void restaurarPadroes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.apply();

        ordenacaoNaoDevolvidos = false;
    }
}
