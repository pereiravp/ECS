package com.pereira.elevador;

import java.util.ArrayList;
import java.util.List;

public class Edificio {

    // Campos

    private List<Elevador> elevadores;

    // Construtor

    public Edificio(int numeroElevadores, int andarMinimo, int andarMaximo) {
        this.elevadores = new ArrayList<>();

        for (int i = 0; i < numeroElevadores; i++) {
            elevadores.add(new Elevador(andarMinimo, andarMaximo));
        }
    }

    // Atribuicao de pedidos

    public void atribuirPedido(int andarPedido) {
        Elevador melhor = null;
        int distanciaMelhor = Integer.MAX_VALUE;

        for (Elevador e : elevadores) {
            int distancia = Math.abs(e.getAndar() - andarPedido);
            if (distancia < distanciaMelhor) {
                distanciaMelhor = distancia;
                melhor = e;
            }
        }

        if (melhor == null) return;
        melhor.adicionarPedido(andarPedido);
    }

    // Getters

    public List<Elevador> getElevadores() {
        return new ArrayList<>(elevadores);
    }
}
