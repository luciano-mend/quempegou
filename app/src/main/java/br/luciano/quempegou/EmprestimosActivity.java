package br.luciano.quempegou;

import android.content.DialogInterface;
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
import java.util.List;

import br.luciano.quempegou.adapters.EmprestimoAdapter;
import br.luciano.quempegou.models.PrioridadeDevolucao;
import br.luciano.quempegou.models.Emprestimo;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;
import br.luciano.quempegou.utils.UtilsAlert;

public class EmprestimosActivity extends AppCompatActivity {

    private ListView lsvEmprestimos;
    private List<Emprestimo> listaEmprestimos;

    private EmprestimoAdapter emprestimoAdapter;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    public static final String ARQUIVO_PREFERENCIAS = "br.luciano.quempegou.PREFERENCIAS";

    private boolean ordenacaoNaoDevolvidos = true;

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

        if (ordenacaoNaoDevolvidos) {
            listaEmprestimos = EmprestimosDatabase.getInstance(this).getEmprestimoDao().getAllAscendingNaoDevolvidos();
        } else {
            listaEmprestimos = EmprestimosDatabase.getInstance(this).getEmprestimoDao().getAllAscending();
        }

        emprestimoAdapter = new EmprestimoAdapter(this,
                listaEmprestimos);

        lsvEmprestimos.setAdapter(emprestimoAdapter);
    }

    public void abrirSobre() {

        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherNovoEmprestimo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            long idEmprestimo = bundle.getLong(CadastroEmprestimoActivity.KEY_ID);

                            Emprestimo emprestimo = EmprestimosDatabase
                                    .getInstance(EmprestimosActivity.this)
                                    .getEmprestimoDao()
                                    .getById(idEmprestimo);

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
                confirmaRestaurarPadroes();

                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    ActivityResultLauncher<Intent> launcherEditarEmprestimo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            long idEmprestimo = bundle.getLong(CadastroEmprestimoActivity.KEY_ID);

                            Emprestimo emprestimoEditado = EmprestimosDatabase
                                    .getInstance(EmprestimosActivity.this)
                                    .getEmprestimoDao()
                                    .getById(idEmprestimo);

                            listaEmprestimos.set(posicaoSelecionada, emprestimoEditado);

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
        intent.putExtra(CadastroEmprestimoActivity.KEY_ID, emprestimo.getId());

        launcherEditarEmprestimo.launch(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.mniOrdenarNaoDevolvidos).setChecked(ordenacaoNaoDevolvidos);
        return true;
    }

    private void excluirEmprestimo() {

        final Emprestimo emprestimo = listaEmprestimos.get(posicaoSelecionada);

        String mensagem = getString(R.string.confirmacao_exclusao_emprestimo, emprestimo.getNomeItemEmprestado().toUpperCase());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                EmprestimosDatabase database = EmprestimosDatabase.getInstance(EmprestimosActivity.this);

                if (database.getEmprestimoDao().delete(emprestimo) != 1) {
                    UtilsAlert.mostrarAviso(EmprestimosActivity.this,
                            R.string.erro_ao_excluir);
                    return;
                }

                listaEmprestimos.remove(posicaoSelecionada);
                emprestimoAdapter.notifyDataSetChanged();
                actionMode.finish();
            }
        };

        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);
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
            listaEmprestimos.sort(Emprestimo.ordenacaoNaoDevolvidos);
        } else {
            listaEmprestimos.sort(Emprestimo.ordenacaoCrescente);
        }

        emprestimoAdapter.notifyDataSetChanged();
    }

    private void confirmaRestaurarPadroes() {
        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                restaurarPadroes();
                definirOrdenacao();

                Toast.makeText(EmprestimosActivity.this,
                        getString(R.string.configuracoes_padroes_restauradas),
                        Toast.LENGTH_SHORT).show();
            }
        };

        UtilsAlert.confirmarAcao(this,
                R.string.deseja_voltar_padroes,
                listenerSim,
                null);

    }

    private void restaurarPadroes() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.apply();

        ordenacaoNaoDevolvidos = true;
    }
}
