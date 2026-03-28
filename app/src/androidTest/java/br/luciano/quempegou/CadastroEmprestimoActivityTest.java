package br.luciano.quempegou;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CadastroEmprestimoActivityTest {

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
            onView(withId(R.id.spnAmigos)).perform(click());

            onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

            onView(withId(R.id.spnAmigos)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void deveMarcarOpcoesDeCheckbox() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.chkFragil)).perform(click());
            onView(withId(R.id.chkDevolucao)).perform(click());

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
