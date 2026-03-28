package br.luciano.quempegou.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.luciano.quempegou.models.Emprestimo;
import br.luciano.quempegou.models.PrioridadeDevolucao;

@RunWith(AndroidJUnit4.class)
public class EmprestimoDaoTest {

    private EmprestimosDatabase db;
    private EmprestimoDao dao;

    @Before
    public void criarBanco() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EmprestimosDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getEmprestimoDao();
    }

    @After
    public void fecharBanco() {
        db.close();
    }

    @Test
    public void deveInserirUmEmprestimoComSucesso() {
        long agora = System.currentTimeMillis();
        Emprestimo emprestimo = new Emprestimo("Livro de Java", 1, PrioridadeDevolucao.ALTA, false, false, "Devolver logo", agora, agora);

        long id = dao.insert(emprestimo);
        Emprestimo emprestimoSalvo = dao.getById(id);

        assertNotNull(emprestimoSalvo);
        assertEquals("Livro de Java", emprestimoSalvo.getNomeItemEmprestado());
        assertEquals(agora, emprestimoSalvo.getDataEmprestimo());
    }

    @Test
    public void deveRemoverUmEmprestimoComSucesso() {
        long agora = System.currentTimeMillis();
        Emprestimo emprestimo = new Emprestimo("Furadeira", 1, PrioridadeDevolucao.BAIXA, false, false, "", agora, agora);
        long id = dao.insert(emprestimo);
        Emprestimo salvo = dao.getById(id);

        dao.delete(salvo);
        Emprestimo deletado = dao.getById(id);

        assertNull(deletado);
    }

    @Test
    public void deveListarTodosOsEmprestimosEmOrdemAlfabetica() {
        long agora = System.currentTimeMillis();
        dao.insert(new Emprestimo("Calculadora", 1, PrioridadeDevolucao.BAIXA, false, false, "", agora, agora));
        dao.insert(new Emprestimo("Alicate", 2, PrioridadeDevolucao.BAIXA, true, false, "", agora, agora));

        List<Emprestimo> lista = dao.getAllAscending();

        assertEquals(2, lista.size());
        assertEquals("Alicate", lista.get(0).getNomeItemEmprestado());
        assertEquals("Calculadora", lista.get(1).getNomeItemEmprestado());
    }

    @Test
    public void deveListarTodosOsEmprestimosEmOrdemAlfabeticaInversa() {
        long agora = System.currentTimeMillis();
        dao.insert(new Emprestimo("Alicate", 1, PrioridadeDevolucao.BAIXA, false, false, "", agora, agora));
        dao.insert(new Emprestimo("Calculadora", 2, PrioridadeDevolucao.BAIXA, false, false, "", agora, agora));

        List<Emprestimo> lista = dao.getAllDescending();

        assertEquals("Calculadora", lista.get(0).getNomeItemEmprestado());
        assertEquals("Alicate", lista.get(1).getNomeItemEmprestado());
    }

    @Test
    public void deveListarEmprestimosNaoDevolvidosPrimeiro() {
        long agora = System.currentTimeMillis();
        Emprestimo e1 = new Emprestimo("Zebra (Devolvido)", 1, PrioridadeDevolucao.BAIXA, false, true, "", agora, agora);
        Emprestimo e2 = new Emprestimo("Abajur (Não Devolvido)", 2, PrioridadeDevolucao.BAIXA, false, false, "", agora, agora);
        dao.insert(e1);
        dao.insert(e2);

        List<Emprestimo> lista = dao.getAllAscendingNaoDevolvidos();

        assertEquals("Abajur (Não Devolvido)", lista.get(0).getNomeItemEmprestado());
        assertTrue(!lista.get(0).isDevolvido());
    }

    @Test
    public void deveAtualizarUmEmprestimoParaDevolvido() {
        long agora = System.currentTimeMillis();
        Emprestimo emprestimo = new Emprestimo("Furadeira", 3, PrioridadeDevolucao.ALTA, false, false, "", agora, agora);
        long id = dao.insert(emprestimo);
        Emprestimo paraAtualizar = dao.getById(id);
        paraAtualizar.setDevolvido(true);

        dao.update(paraAtualizar);
        Emprestimo atualizado = dao.getById(id);

        assertTrue(atualizado.isDevolvido());
    }

    @Test
    public void deveRetornarNuloAoBuscarIdInexistente() {
        Emprestimo inexistente = dao.getById(999);

        assertNull(inexistente);
    }
}
