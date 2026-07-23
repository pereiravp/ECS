package com.pereira.elevador;

import java.util.TreeSet;

public class Elevador {

    // Campos

    private int andar;
    private Estado estado;
    private Direcao direcao;
    private int andarMinimo;
    private int andarMaximo;
    private TreeSet<Integer> pedidos;

    // Construtor

    public Elevador(int andarMinimo, int andarMaximo) {
        this.andarMinimo = andarMinimo;
        this.andarMaximo = andarMaximo;
        this.andar = 0;
        this.estado = Estado.PARADO;
        this.direcao = Direcao.SUBIR;
        this.pedidos = new TreeSet<>();
    }

    // Movimento

    public void subir(int N) {
        int andarInicial = andar;

        if (!andarValido(N)) return;
        if (estado == Estado.PORTAS_ABERTAS) return;
        if (andar >= N) return;

        estado = Estado.A_SUBIR;
        for (int i = 0; i < N - andarInicial; i++) {
            andar++;
        }
        estado = Estado.PARADO;
    }

    public void descer(int N) {
        int andarInicial = andar;

        if (!andarValido(N)) return;
        if (estado == Estado.PORTAS_ABERTAS) return;
        if (andar <= N) return;

        estado = Estado.A_DESCER;
        for (int i = 0; i < andarInicial - N; i++) {
            andar--;
        }
        estado = Estado.PARADO;
    }

    public void irPara(int N) {
        if (N > andar) subir(N);
        else if (N < andar) descer(N);
    }

    // Portas

    public void abrirPortas() {
        if (estado != Estado.PARADO) return;
        estado = Estado.PORTAS_ABERTAS;
    }

    public void fecharPortas() {
        if (estado != Estado.PORTAS_ABERTAS) return;
        estado = Estado.PARADO;
    }

    // Pedidos e escalonamento

    public void adicionarPedido(int andarPedido) {
        if (!andarValido(andarPedido)) return;
        pedidos.add(andarPedido);
    }

    public Integer proximoDestino() {
        if (pedidos.isEmpty()) return null;

        if (direcao == Direcao.SUBIR) {
            Integer acima = pedidos.higher(andar);
            if (acima != null) return acima;
            // nada acima: inverte e serve o mais próximo abaixo
            direcao = Direcao.DESCER;
            return pedidos.lower(andar);
        } else {
            Integer abaixo = pedidos.lower(andar);
            if (abaixo != null) return abaixo;
            direcao = Direcao.SUBIR;
            return pedidos.higher(andar);
        }
    }

    public void passo() {
        Integer destino = proximoDestino();
        if (destino == null) return;

        irPara(destino);
        pedidos.remove(destino);
    }

    // Validação

    private boolean andarValido(int N) {
        return N >= andarMinimo && N <= andarMaximo;
    }

    // Getters

    public int getAndar() {
        return andar;
    }

    public Estado getEstado() {
        return estado;
    }

    public Direcao getDirecao() {
        return direcao;
    }

    public TreeSet<Integer> getPedidos() {
        return new TreeSet<>(pedidos);
    }
}
