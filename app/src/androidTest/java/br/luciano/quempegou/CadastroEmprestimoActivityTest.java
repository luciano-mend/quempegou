package br.luciano.quempegou;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;

@RunWith(AndroidJUnit4.class)
public class CadastroEmprestimoActivityTest {

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        EmprestimosDatabase db = EmprestimosDatabase.getInstance(context);
        
        // Limpa e prepara amigos para os testes de UI
        db.clearAllTables();
        db.getAmigoDao().insert(new Amigo("Amigo Teste 1", "", System.currentTimeMillis(), true));
        db.getAmigoDao().insert(new Amigo("Amigo Teste 2", "", System.currentTimeMillis(), true));
    }

    @Test
    public void deveMostrarErroAoTentarSalvarSemNomeDoItem() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.mniSalvar)).perform(click());
            onView(withText(R.string.campo_item_emprestado_nao_preenchido)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void deveMostrarErroAoTentarSalvarSemPrioridade() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edtItemEmprestado)).perform(typeText("Furadeira"), closeSoftKeyboard());

            onView(withId(R.id.mniSalvar)).perform(click());

            onView(withText(R.string.selecione_uma_prioridade_de_devolucao)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void devePermitirSelecionarAmigoNoSpinner() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.spnAmigos)).perform(scrollTo(), click());

            // Agora o Spinner contém objetos Amigo, não Strings
            onData(allOf(is(instanceOf(Amigo.class)))).atPosition(1).perform(click());

            onView(withId(R.id.spnAmigos)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void deveMarcarOpcoesDeCheckbox() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(android.R.id.content)).perform(closeSoftKeyboard());

            onView(withId(R.id.chkFragil)).perform(scrollTo(), click());
            onView(withId(R.id.chkDevolucao)).perform(scrollTo(), click());

            onView(withId(R.id.chkFragil)).check(matches(isChecked()));
            onView(withId(R.id.chkDevolucao)).check(matches(isChecked()));
        }
    }

    @Test
    public void deveLimparCamposAoClicarNoMenuLimpar() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edtItemEmprestado)).perform(typeText("Furadeira"), closeSoftKeyboard());
            onView(withId(R.id.mniLimpar)).perform(click());
            onView(withId(R.id.edtItemEmprestado)).check(matches(withText("")));
        }
    }
}
