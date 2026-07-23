package com.pereira.elevador;
public class Elevador{

    private int andar;
    private Estado estado;

    // Construtor
    public Elevador(){
        andar = 0;
        estado = Estado.PARADO;
    }

    // métodos
    public void subir (int N){
        int andarInicial = andar;
        if(andarInicial >= N) return;

        estado = Estado.A_SUBIR;
            for(int i = 0; i < N - andarInicial; i++){
                andar++;
            }
        estado = Estado.PARADO;
    }

    public void descer (int N){
        int andarInicial = andar;
        if(andarInicial <= N) return;

        estado = Estado.A_DESCER;
            for(int i = 0; i < andarInicial-N; i++){
                andar--;
            }
        estado = Estado.PARADO;
    }

    // GETTERS
    public int getAndar(){
        return this.andar;
    }
    public Estado getEstado(){
        return this.estado;
    }

}
