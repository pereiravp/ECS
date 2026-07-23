package com.pereira.elevador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EdificioTest {

    @Test
    public void pedidoAtribuidoFicaRegistadoNumElevador() {
        Edificio edificio = new Edificio(2, -1, 10);
        edificio.atribuirPedido(3);
        assertTrue(edificio.getElevadores().get(0).getPedidos().contains(3));
    }
}
