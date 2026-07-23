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

    @Test
    public void descerColocaElevadorNoAndarCorreto(){
        Elevador e = new Elevador();
        e.subir(5);
        e.descer(2);
        assertEquals(2, e.getAndar());
    }

    @Test
    public void subirParaAndarInferiorNaoMoveElevador() {
        Elevador e = new Elevador();
        e.subir(5);
        e.subir(1);
        assertEquals(5, e.getAndar());
    }

    @Test
    public void aposSubirEstadoVoltaAParado(){
        Elevador e = new Elevador();
        e.subir(4);
        assertEquals(Estado.PARADO, e.getEstado());
    }

    @Test
    public void elevadorPortasAbertasNaoAndaParaSubir(){
        Elevador e = new Elevador();
        e.subir(2);
        e.abrirPortas();
        e.subir(4);
        assertEquals(2, e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }

    @Test
    public void elevadorPortasAbertasNaoAndaParaDescer(){
        Elevador e = new Elevador();
        e.subir(4);
        e.abrirPortas();
        e.descer(0);
        assertEquals(4,e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }

    @Test
    public void elevadorVaiParaCima(){
        Elevador e = new Elevador();
        e.irPara(4);
        assertEquals(4,e.getAndar());
    }

    @Test
    public void elevadorVaiParaBaixo(){
        Elevador e = new Elevador();
        e.subir(3);
        e.irPara(1);
        assertEquals(1, e.getAndar());
    }

    @Test
    public void elevadorFicaNoSitio(){
        Elevador e = new Elevador();
        e.subir(3);
        e.irPara(3);
        assertEquals(3, e.getAndar());
    }

    @Test
    public void elevadorTemPortasAbertasNaoAndar(){
        Elevador e = new Elevador();
        e.irPara(3);
        e.abrirPortas();
        e.irPara(1);
        assertEquals(3, e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }
}
