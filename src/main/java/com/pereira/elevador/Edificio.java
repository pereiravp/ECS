package com.pereira.elevador;
import java.util.List;
import java.util.ArrayList;

public class Edificio {
    private List<Elevador> elevadores;
    private int andarMinimo;
    private int andarMaximo;

    public Edificio(int numeroElevadores, int andarMinimo, int andarMaximo){
        this.andarMaximo = andarMaximo;
        this.andarMinimo = andarMinimo;
        this.elevadores = new ArrayList<>();

        for(int i = 0; i < numeroElevadores; i++){
            elevadores.add(new Elevador(andarMinimo, andarMaximo));
        }
    }

    public void atribuirPedido(int andarPedido){
        Elevador melhor = null;
        int distanciaMelhor = Integer.MAX_VALUE;

        for(Elevador e : this.elevadores){
            int distancia = Math.abs(e.getAndar() - andarPedido);
            if(distancia < distanciaMelhor) {
                distanciaMelhor = distancia;
                melhor = e;
            }
        }
        if (melhor == null) return;
        melhor.adicionarPedido(andarPedido);
    }

    public List<Elevador> getElevadores(){
        return new ArrayList<>(elevadores);
    }
}
