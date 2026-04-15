package br.luciano.quempegou.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EmprestimoTest {

    @Test
    public void deveCriarEmprestimoComNovosCampos() {
        long dataEmprestimo = 1672531200000L;
        Long dataDevolucao = 1673395200000L;
        long idAmigo = 10L;
        
        Emprestimo emprestimo = new Emprestimo(
                "Item Teste",
                idAmigo,
                PrioridadeDevolucao.ALTA,
                true,
                false,
                "Obs",
                dataEmprestimo,
                dataDevolucao
        );

        assertEquals(idAmigo, emprestimo.getAmigo());
        assertEquals(dataEmprestimo, emprestimo.getDataEmprestimo());
        assertEquals(dataDevolucao, emprestimo.getDataDevolucao());
    }

    @Test
    public void deveRespeitarEqualsComNovosCampos() {
        long agora = System.currentTimeMillis();
        long idAmigo = 1L;
        
        Emprestimo e1 = new Emprestimo("Item", idAmigo, PrioridadeDevolucao.BAIXA, false, false, "Obs", agora, agora);
        Emprestimo e2 = new Emprestimo("Item", idAmigo, PrioridadeDevolucao.BAIXA, false, false, "Obs", agora, agora);
        Emprestimo e3 = new Emprestimo("Item", idAmigo, PrioridadeDevolucao.BAIXA, false, false, "Obs", agora + 1000, agora);

        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
    }
}
