package com.mycompany.puentethreadsafe;

public class Persona implements Runnable {
    // Atributos
    private final String idPersona;
    private final int tiempoPaso;
    private final int pesoPersona;
    private final Puente puente;
    private final String sentido; // Dirección de la persona

    // Constructor
    public Persona(String idPersona, int tiempoPaso, int pesoPersona, String sentido, Puente puente) {
        this.idPersona = idPersona;
        this.tiempoPaso = tiempoPaso;
        this.pesoPersona = pesoPersona;
        this.puente = puente;
        this.sentido = sentido;
    }

    // Getters
    public String getIdPersona() {
        return idPersona;
    }

    public int getTiempoPaso() {
        return tiempoPaso;
    }

    public int getPesoPersona() {
        return pesoPersona;
    }

    public String getSentido() {
        return sentido;
    }

    // Método run()
    @Override
    public void run() {
        System.out.printf(">>> La %s con %d kilos quiere cruzar hacia %s en %d segundos.\n",
                idPersona, pesoPersona, sentido, tiempoPaso);

        // Entrar
        try {
            puente.entrar(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Cruzar
        try {
            Thread.sleep(tiempoPaso + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Salir
        puente.salir(this);
    }
}
