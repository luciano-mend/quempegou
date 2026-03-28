package br.luciano.quempegou;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.luciano.quempegou.models.Emprestimo;
import br.luciano.quempegou.models.PrioridadeDevolucao;
import br.luciano.quempegou.persistencia.EmprestimosDatabase;

@RunWith(AndroidJUnit4.class)
public class LayoutPositionTest {

    @Test
    public void validarLayoutCadastroEmprestimo() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CadastroEmprestimoActivity.class);
        intent.putExtra(CadastroEmprestimoActivity.KEY_MODO, CadastroEmprestimoActivity.MODO_NOVO);
        try (ActivityScenario<CadastroEmprestimoActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edtItemEmprestado)).check(isCompletelyBelow(withId(R.id.textViewItemEmprestado)));
            onView(withId(R.id.textViewParaQuem)).check(isCompletelyBelow(withId(R.id.edtItemEmprestado)));
            onView(withId(R.id.spnAmigos)).check(isCompletelyBelow(withId(R.id.textViewParaQuem)));
            onView(withId(R.id.textViewPrioridade)).check(isCompletelyBelow(withId(R.id.spnAmigos)));
            onView(withId(R.id.rgbPrioridadeDevolucao)).check(isCompletelyBelow(withId(R.id.textViewPrioridade)));
            onView(withId(R.id.chkFragil)).check(isCompletelyRightOf(withId(R.id.rgbPrioridadeDevolucao)));
            onView(withId(R.id.textViewDataEmprestimo)).check(isCompletelyBelow(withId(R.id.rgbPrioridadeDevolucao)));
            onView(withId(R.id.chkDevolucao)).check(isCompletelyRightOf(withId(R.id.textViewDataEmprestimo)));
            onView(withId(R.id.edtDataEmprestimo)).check(isCompletelyBelow(withId(R.id.textViewDataEmprestimo)));
            onView(withId(R.id.edtDataDevolucao)).check(isCompletelyRightOf(withId(R.id.edtDataEmprestimo)));
            onView(withId(R.id.textViewObservacoes)).check(isCompletelyBelow(withId(R.id.edtDataEmprestimo)));
            onView(withId(R.id.edtObservacoes)).check(isCompletelyBelow(withId(R.id.textViewObservacoes)));
        }
    }

    @Test
    public void validarLayoutLinhaListaEmprestimos() {
        String itemUnico = "ITEM_LAYOUT_TEST_" + System.currentTimeMillis();
        Context context = ApplicationProvider.getApplicationContext();
        EmprestimosDatabase db = EmprestimosDatabase.getInstance(context);
        db.getEmprestimoDao().insert(new Emprestimo(itemUnico, 0, PrioridadeDevolucao.BAIXA, false, false, "", System.currentTimeMillis(), null));

        try (ActivityScenario<EmprestimosActivity> scenario = ActivityScenario.launch(EmprestimosActivity.class)) {
            // Matcher para encontrar a linha específica que inserimos
            Matcher<View> linhaEspecifica = isDescendantOfA(hasDescendant(withText(itemUnico)));

            onView(allOf(withId(R.id.txvValorItemEmprestado), withText(itemUnico)))
                    .check(isCompletelyRightOf(allOf(withId(R.id.txvRotuloItemEmprestado), linhaEspecifica)));

            onView(allOf(withId(R.id.txvRotuloNomeAmigo), linhaEspecifica))
                    .check(isCompletelyBelow(allOf(withId(R.id.txvValorItemEmprestado), withText(itemUnico))));

            onView(allOf(withId(R.id.txvValorNomeAmigo), linhaEspecifica))
                    .check(isCompletelyRightOf(allOf(withId(R.id.txvRotuloNomeAmigo), linhaEspecifica)));

            onView(allOf(withId(R.id.txvRotuloPrioridadeDevolucao), linhaEspecifica))
                    .check(isCompletelyBelow(allOf(withId(R.id.txvValorNomeAmigo), linhaEspecifica)));

            onView(allOf(withId(R.id.txvValorPrioridadeDevolucao), linhaEspecifica))
                    .check(isCompletelyRightOf(allOf(withId(R.id.txvRotuloPrioridadeDevolucao), linhaEspecifica)));
        }
    }

    @Test
    public void validarLayoutSobre() {
        try (ActivityScenario<SobreActivity> scenario = ActivityScenario.launch(SobreActivity.class)) {
            onView(withId(R.id.textViewAluno)).check(isCompletelyBelow(withId(R.id.imageViewLogo)));
            onView(withId(R.id.textViewCurso)).check(isCompletelyBelow(withId(R.id.textViewAluno)));
            onView(withId(R.id.textViewEmail)).check(isCompletelyBelow(withId(R.id.textViewCurso)));
            onView(withId(R.id.textViewDescricao)).check(isCompletelyBelow(withId(R.id.textViewEmail)));
            onView(withId(R.id.imgUtfpr)).check(isCompletelyBelow(withId(R.id.textViewDescricao)));
            onView(withId(R.id.textViewUtfpr)).check(isCompletelyBelow(withId(R.id.imgUtfpr)));
        }
    }
}
