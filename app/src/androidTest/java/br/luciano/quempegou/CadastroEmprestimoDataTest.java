package br.luciano.quempegou;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CadastroEmprestimoDataTest {

    @Test
    public void deveIniciarComDataEmprestimoPreenchidaEDevolucaoVazia() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edtDataEmprestimo)).check(matches(not(withText(""))));
            onView(withId(R.id.edtDataDevolucao)).check(matches(withText("")));
            onView(withId(R.id.edtDataDevolucao)).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void deveHabilitarEPreencherDataAoMarcarDevolvido() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.chkDevolucao)).perform(scrollTo(), click());
            
            onView(withId(R.id.edtDataDevolucao)).check(matches(isEnabled()));
            onView(withId(R.id.edtDataDevolucao)).check(matches(not(withText(""))));
        }
    }

    @Test
    public void deveAbrirDatePickerAoClicarNoCampoDataEmprestimo() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edtDataEmprestimo)).perform(scrollTo(), click());
            
            onView(withText(R.string.data_emprestimo)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void deveAbrirDatePickerAoClicarNoCampoDataDevolucaoHabilitado() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.chkDevolucao)).perform(scrollTo(), click());
            
            onView(withId(R.id.edtDataDevolucao)).perform(scrollTo(), click());
            
            onView(withText(R.string.data_devolucao)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void deveLimparDataDevolucaoAoDesmarcarCheckbox() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.chkDevolucao)).perform(scrollTo(), click());
            onView(withId(R.id.chkDevolucao)).perform(scrollTo(), click());

            onView(withId(R.id.edtDataDevolucao)).check(matches(withText("")));
            onView(withId(R.id.edtDataDevolucao)).check(matches(not(isEnabled())));
        }
    }
}
