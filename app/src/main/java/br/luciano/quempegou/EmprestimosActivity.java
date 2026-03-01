package br.luciano.quempegou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.luciano.quempegou.adapters.EmprestimoAdapter;
import br.luciano.quempegou.enums.PrioridadeDevolucao;
import br.luciano.quempegou.models.Emprestimo;

public class EmprestimosActivity extends AppCompatActivity {

    private ListView lsvEmprestimos;
    private List<Emprestimo> listaEmprestimos;

    private EmprestimoAdapter emprestimoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimos);

        setTitle(getString(R.string.controle_de_emprestimos));


        lsvEmprestimos = findViewById(R.id.lsvEmprestimos);

        lsvEmprestimos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Emprestimo emprestimo = (Emprestimo) lsvEmprestimos.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.o_item_emprestado) + emprestimo.getNomeItemEmprestado() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();

            }
        });
        
        popularEmprestimos();
    }

    private void popularEmprestimos() {

        listaEmprestimos = new ArrayList<>();

        emprestimoAdapter = new EmprestimoAdapter(this,
                listaEmprestimos);

        lsvEmprestimos.setAdapter(emprestimoAdapter);
    }

    public void abrirSobre(View view) {

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
                            emprestimoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    public void abrirNovoEmprestimo(View view) {
        Intent intent = new Intent(this, CadastroEmprestimoActivity.class);
        launcherNovoEmprestimo.launch(intent);
    }
}
