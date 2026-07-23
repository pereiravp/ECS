package com.pereira.elevador;

import java.security.PublicKey;
import java.util.TreeSet;

public class Elevador{

    private int andar;
    private Estado estado;
    private int andarMinimo;
    private int andarMaximo;
    private Direcao direcao;
    private TreeSet<Integer> pedidos;

    // Construtor
    public Elevador(int andarMinimo, int andarMaximo){
        andar = 0;
        estado = Estado.PARADO;
        this.andarMinimo = andarMinimo;
        this.andarMaximo = andarMaximo;
        this.direcao = Direcao.SUBIR;
        pedidos = new TreeSet<>();
    }

    // métodos básicos
    public void subir (int N){
        int andarInicial = andar;
        if(!andarValido(N)) return;
        // Verificações de Segurança
        if(estado == Estado.PORTAS_ABERTAS) return;
        if(andar >= N) return;

        estado = Estado.A_SUBIR;
            for(int i = 0; i < N - andarInicial; i++){
                andar++;
            }
        estado = Estado.PARADO;
    }

    public void descer (int N){
        int andarInicial = andar;
        if(!andarValido(N)) return;
        // Verificações de Segurança
        if(estado == Estado.PORTAS_ABERTAS) return;
        if(andar <= N) return;

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

    // metodos ligeiramente mais complexos

    public void adicionarPedido(int andar){
        if(!andarValido(andar)) return;
        this.pedidos.add(andar);
    }

    public Integer proximoDestino(){
        if (this.pedidos.isEmpty()) return null;

        if(direcao == Direcao.SUBIR){
            Integer acima = pedidos.higher(andar);
            if(acima != null) return acima;
            direcao = Direcao.DESCER;
            return pedidos.lower(andar);
        }
        else {
            Integer abaixo = pedidos.lower(andar);
            if(abaixo != null) return abaixo;
            direcao = Direcao.SUBIR;
            return pedidos.higher(andar);
        }
    }

    public void passo(){
        Integer destino = proximoDestino();
        if(destino == null) return;

        irPara(destino);
        pedidos.remove(destino);
    }

    // métodos auxiliar de verificação
    private boolean andarValido(int N){
    return N >= andarMinimo && N <= andarMaximo;
    }

    // GETTERS
    public int getAndar(){
        return this.andar;
    }
    public Estado getEstado(){
        return this.estado;
    }

    public Direcao getDirecao(){
        return this.direcao;
    }
    public TreeSet<Integer> getPedidos() {
        return new TreeSet<>(pedidos);
    }
}
