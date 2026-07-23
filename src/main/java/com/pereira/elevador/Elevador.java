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

        // Verificações de Segurança
        if(estado == Estado.PORTAS_ABERTAS) return;
        if(andarInicial >= N) return;

        estado = Estado.A_SUBIR;
            for(int i = 0; i < N - andarInicial; i++){
                andar++;
            }
        estado = Estado.PARADO;
    }

    public void descer (int N){
        int andarInicial = andar;

        // Verificações de Segurança
        if(estado == Estado.PORTAS_ABERTAS) return;
        if(andarInicial <= N) return;

        estado = Estado.A_DESCER;
            for(int i = 0; i < andarInicial-N; i++){
                andar--;
            }
        estado = Estado.PARADO;
    }

    public void abrirPortas(){
    if(estado != Estado.PARADO) return;
    estado = Estado.PORTAS_ABERTAS;
    }

    public void fecharPortas(){
        if(estado != Estado.PORTAS_ABERTAS) return;
        estado = Estado.PARADO;
    }

    public void irPara(int N){
        if (N > andar) subir(N);
        else if (N < andar) descer(N);
    }

    // GETTERS
    public int getAndar(){
        return this.andar;
    }
    public Estado getEstado(){
        return this.estado;
    }

}
