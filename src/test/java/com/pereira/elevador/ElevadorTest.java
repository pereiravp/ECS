package com.pereira.elevador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevadorTest {

    @Test
    public void subirColocaElevadorNoAndarCorreto() {
        Elevador e = new Elevador();
        e.subir(5);
        assertEquals(5, e.getAndar());
    }
}
