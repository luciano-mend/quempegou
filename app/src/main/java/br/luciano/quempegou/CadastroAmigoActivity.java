package br.luciano.quempegou;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroAmigoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_amigo);

        setTitle(R.string.cadastro_amigo);
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
            // Futuramente implementar salvar amigo
            return true;
        } else if (idMenuItem == R.id.mniLimpar) {
            // Futuramente implementar limpar campos
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
