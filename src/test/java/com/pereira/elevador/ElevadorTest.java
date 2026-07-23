package com.pereira.elevador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElevadorTest {

    @Test
    public void subirColocaElevadorNoAndarCorreto() {
        Elevador e = new Elevador(-1,10);
        e.subir(5);
        assertEquals(5, e.getAndar());
    }

    @Test
    public void descerColocaElevadorNoAndarCorreto(){
        Elevador e = new Elevador(-1,10);
        e.subir(5);
        e.descer(2);
        assertEquals(2, e.getAndar());
    }

    @Test
    public void subirParaAndarInferiorNaoMoveElevador() {
        Elevador e = new Elevador(-1,10);
        e.subir(5);
        e.subir(1);
        assertEquals(5, e.getAndar());
    }

    @Test
    public void aposSubirEstadoVoltaAParado(){
        Elevador e = new Elevador(-1,10);
        e.subir(4);
        assertEquals(Estado.PARADO, e.getEstado());
    }

    @Test
    public void elevadorPortasAbertasNaoAndaParaSubir(){
        Elevador e = new Elevador(-1,10);
        e.subir(2);
        e.abrirPortas();
        e.subir(4);
        assertEquals(2, e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }

    @Test
    public void elevadorPortasAbertasNaoAndaParaDescer(){
        Elevador e = new Elevador(-1,10);
        e.subir(4);
        e.abrirPortas();
        e.descer(0);
        assertEquals(4,e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }

    @Test
    public void elevadorVaiParaCima(){
        Elevador e = new Elevador(-1,10);
        e.irPara(4);
        assertEquals(4,e.getAndar());
    }

    @Test
    public void elevadorVaiParaBaixo(){
        Elevador e = new Elevador(-1,10);
        e.subir(3);
        e.irPara(1);
        assertEquals(1, e.getAndar());
    }

    @Test
    public void elevadorFicaNoSitio(){
        Elevador e = new Elevador(-1,10);
        e.subir(3);
        e.irPara(3);
        assertEquals(3, e.getAndar());
    }

    @Test
    public void elevadorTemPortasAbertasNaoAndar(){
        Elevador e = new Elevador(-1,10);
        e.irPara(3);
        e.abrirPortas();
        e.irPara(1);
        assertEquals(3, e.getAndar());
        assertEquals(Estado.PORTAS_ABERTAS, e.getEstado());
    }

    @Test
    public void elevadorNaoAndaComAndarInvalido(){
        Elevador e = new Elevador(-1,10);
        e.irPara(3);
        e.irPara(50);
        assertEquals(3, e.getAndar());
    }

    public void testarRotaDoElevador(){
        Elevador e = new Elevador(-1, 10);

        e.adicionarPedido(5);
        e.adicionarPedido(2);
        e.passo();
        assertEquals(2, e.getAndar());
    }

    @Test
    public void doisPassosServemTodosOsPedidos() {
        Elevador e = new Elevador(-1, 10);
        e.adicionarPedido(5);
        e.adicionarPedido(2);
        e.passo();
        e.passo();
        assertEquals(5, e.getAndar());
    }

    @Test
    public void passoInverteDirecaoQuandoNaoHaPedidosAcima() {
        Elevador e = new Elevador(-1, 10);
        e.subir(8);
        e.adicionarPedido(2);
        e.passo();
        assertEquals(2, e.getAndar());
    }

    @Test
    public void recebePedidoParaElevador(){
        Elevador e = new Elevador(-1,10);
        e.adicionarPedido(3);
        assertTrue(e.getPedidos().contains(3));
    }

    @Test
    public void edificioComElevadorRecebidoPedido(){
        Edificio edificio = new Edificio(2, -1, 10);
        edificio.atribuirPedido(3);
        assertTrue(edificio.getElevadores().get(0).getPedidos().contains(3));
    }
}
