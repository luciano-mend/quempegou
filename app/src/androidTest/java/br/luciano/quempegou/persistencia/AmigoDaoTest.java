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

import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.models.Emprestimo;
import br.luciano.quempegou.models.PrioridadeDevolucao;

@RunWith(AndroidJUnit4.class)
public class AmigoDaoTest {

    private EmprestimosDatabase db;
    private AmigoDao amigoDao;
    private EmprestimoDao emprestimoDao;

    @Before
    public void criarBanco() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, EmprestimosDatabase.class)
                .allowMainThreadQueries()
                .build();
        amigoDao = db.getAmigoDao();
        emprestimoDao = db.getEmprestimoDao();
    }

    @After
    public void fecharBanco() {
        db.close();
    }

    @Test
    public void deveInserirUmAmigoComSucesso() {
        long agora = System.currentTimeMillis();
        Amigo amigo = new Amigo("Ricardo", "Vizinho", agora, true);

        long id = amigoDao.insert(amigo);
        Amigo salvo = amigoDao.getById(id);

        assertNotNull(salvo);
        assertEquals("Ricardo", salvo.getNome());
        assertTrue(salvo.isAtivo());
    }

    @Test
    public void deveListarApenasAmigosAtivos() {
        long agora = System.currentTimeMillis();
        amigoDao.insert(new Amigo("Ativo 1", "", agora, true));
        amigoDao.insert(new Amigo("Inativo 1", "", agora, false));
        amigoDao.insert(new Amigo("Ativo 2", "", agora, true));

        List<Amigo> ativos = amigoDao.getAllAtivos();

        assertEquals(2, ativos.size());
        for (Amigo a : ativos) {
            assertTrue(a.isAtivo());
        }
    }

    @Test
    public void deveContarEmprestimosDeUmAmigo() {
        long agora = System.currentTimeMillis();
        long idAmigo = amigoDao.insert(new Amigo("João", "", agora, true));

        assertEquals(0, amigoDao.countEmprestimosByAmigo(idAmigo));

        emprestimoDao.insert(new Emprestimo("Furadeira", idAmigo, PrioridadeDevolucao.BAIXA, false, false, "", agora, null));
        emprestimoDao.insert(new Emprestimo("Martelo", idAmigo, PrioridadeDevolucao.ALTA, false, false, "", agora, null));

        assertEquals(2, amigoDao.countEmprestimosByAmigo(idAmigo));
    }

    @Test
    public void deveAtualizarAmigoComSucesso() {
        long agora = System.currentTimeMillis();
        long id = amigoDao.insert(new Amigo("Pedro", "Antiga", agora, true));
        
        Amigo p = amigoDao.getById(id);
        p.setNome("Pedro Silva");
        p.setAtivo(false);
        
        amigoDao.update(p);
        Amigo atualizado = amigoDao.getById(id);
        
        assertEquals("Pedro Silva", atualizado.getNome());
        assertTrue(!atualizado.isAtivo());
    }

    @Test
    public void deveRemoverAmigoSemEmprestimos() {
        long id = amigoDao.insert(new Amigo("Remover", "", System.currentTimeMillis(), true));
        Amigo a = amigoDao.getById(id);
        
        amigoDao.delete(a);
        assertNull(amigoDao.getById(id));
    }
}
