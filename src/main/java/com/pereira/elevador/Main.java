package com.pereira.elevador;

public class Main {
    public static void main(String[] args) {
        Elevador e = new Elevador();
        e.subir(5);
        System.out.println(e.getAndar() + " " + e.getEstado());
        e.descer(2);
        System.out.println(e.getAndar() + " " + e.getEstado());
        e.subir(1);
        System.out.println(e.getAndar() + " " + e.getEstado());
    }
}
