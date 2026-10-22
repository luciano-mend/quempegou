package br.luciano.quempegou.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class AmigoTest {

    @Test
    public void deveCriarAmigoComCamposCorretos() {
        long agora = System.currentTimeMillis();
        Amigo amigo = new Amigo("Carlos", "Amigo de infância", agora, true);

        assertEquals("Carlos", amigo.getNome());
        assertEquals("Amigo de infância", amigo.getObservacao());
        assertEquals(agora, amigo.getDataInclusao());
        assertEquals(true, amigo.isAtivo());
    }

    @Test
    public void deveRespeitarEqualsEHashCode() {
        long agora = System.currentTimeMillis();
        
        Amigo a1 = new Amigo("Ana", "Obs", agora, true);
        Amigo a2 = new Amigo("ana", "OBS", agora, true); // Case insensitive
        Amigo a3 = new Amigo("Ana", "Outra Obs", agora, true);
        Amigo a4 = new Amigo("Ana", "Obs", agora + 1, true);

        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertNotEquals(a1, a4);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
