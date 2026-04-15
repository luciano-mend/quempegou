package br.luciano.quempegou;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;

import java.util.List;

import br.luciano.quempegou.adapters.AmigoAdapter;
import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;
import br.luciano.quempegou.utils.UtilsAlert;

public class AmigosActivity extends AppCompatActivity {

    private ListView lsvAmigos;
    private List<Amigo> listaAmigos;
    private AmigoAdapter amigoAdapter;

    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private View viewSelecionada;
    private Drawable backgroundDrawable;

    private final ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.amigos_item_selecionado, menu);
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
                editarAmigo();
                return true;
            } else if (idMenuItem == R.id.mniExcluir) {
                excluirAmigo();
                return true;
            }
            return false;
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
            lsvAmigos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        setTitle(R.string.amigos);

        lsvAmigos = findViewById(R.id.lsvAmigos);

        lsvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                posicaoSelecionada = posicao;
                editarAmigo();
            }
        });

        lsvAmigos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                if (actionMode != null) {
                    return false;
                }
                posicaoSelecionada = posicao;
                viewSelecionada = view;
                backgroundDrawable = view.getBackground();
                view.setBackgroundColor(ContextCompat.getColor(AmigosActivity.this, R.color.azul_selecionado));

                lsvAmigos.setEnabled(false);
                actionMode = startSupportActionMode(actionCallback);
                return true;
            }
        });

        popularAmigos();
    }

    private void popularAmigos() {
        listaAmigos = EmprestimosDatabase.getInstance(this).getAmigoDao().getAllAscending();
        amigoAdapter = new AmigoAdapter(this, listaAmigos);
        lsvAmigos.setAdapter(amigoAdapter);
    }

    private void editarAmigo() {
        Amigo amigo = listaAmigos.get(posicaoSelecionada);
        Intent intent = new Intent(this, CadastroAmigoActivity.class);
        intent.putExtra(CadastroAmigoActivity.KEY_MODO, CadastroAmigoActivity.MODO_EDITAR);
        intent.putExtra(CadastroAmigoActivity.KEY_ID, amigo.getId());
        launcherEditarAmigo.launch(intent);
    }

    private void excluirAmigo() {
        final Amigo amigo = listaAmigos.get(posicaoSelecionada);

        EmprestimosDatabase database = EmprestimosDatabase.getInstance(this);
        int quantidadeEmprestimos = database.getAmigoDao().countEmprestimosByAmigo(amigo.getId());

        if (quantidadeEmprestimos > 0) {
            UtilsAlert.mostrarAviso(this, R.string.amigo_possui_emprestimos);
            actionMode.finish();
            return;
        }

        String mensagem = getString(R.string.confirmacao_exclusao_amigo, amigo.getNome().toUpperCase());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (database.getAmigoDao().delete(amigo) != 1) {
                    UtilsAlert.mostrarAviso(AmigosActivity.this, R.string.erro_ao_excluir);
                    return;
                }
                listaAmigos.remove(posicaoSelecionada);
                amigoAdapter.notifyDataSetChanged();
                actionMode.finish();
            }
        };

        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);
    }

    ActivityResultLauncher<Intent> launcherEditarAmigo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        popularAmigos();
                    }
                    posicaoSelecionada = -1;
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                }
            });
}
