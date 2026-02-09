package br.luciano.quempegou;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import br.luciano.quempegou.enums.PrioridadeDevolucao;

public class EmprestimosActivity extends AppCompatActivity {

    private ListView lsvEmprestimos;
    private List<Emprestimo> listaEmprestimos;

    private EmprestimoAdapter emprestimoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimos);

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

        String[] itenEmprestados = getResources().getStringArray(R.array.item_emprestado);
        int[] amigos_emprestimo = getResources().getIntArray(R.array.amigos_emprestimo);
        int[] prioridadeDevolucaoEmprestimo = getResources().getIntArray(R.array.prioridade_devolucao);
        int[] fragil = getResources().getIntArray(R.array.fragil);
        int[] devolvido = getResources().getIntArray(R.array.devolucao);
        String[] observacoesEmprestimos = getResources().getStringArray(R.array.observacoes_emprestimos);

        listaEmprestimos = new ArrayList<>();

        Emprestimo emprestimo;
        boolean ehFragil;
        boolean emprestimoDevolvido;
        PrioridadeDevolucao prioridadeDevolucao;

        PrioridadeDevolucao[] prioridadeDevolucaos = PrioridadeDevolucao.values();

        for (int i = 0; i < itenEmprestados.length; i++) {
            ehFragil = (fragil[i] == 1);
            emprestimoDevolvido = (devolvido[i] == 1);

            prioridadeDevolucao = prioridadeDevolucaos[prioridadeDevolucaoEmprestimo[i]];

            emprestimo = new Emprestimo(
                    itenEmprestados[i],
                    amigos_emprestimo[i],
                    prioridadeDevolucao,
                    ehFragil,
                    emprestimoDevolvido,
                    observacoesEmprestimos[i]
            );

            listaEmprestimos.add(emprestimo);
        }

        emprestimoAdapter = new EmprestimoAdapter(this,
                listaEmprestimos);

        lsvEmprestimos.setAdapter(emprestimoAdapter);
    }
}