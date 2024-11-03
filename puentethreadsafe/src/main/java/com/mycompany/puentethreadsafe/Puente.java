package com.mycompany.puentethreadsafe;

public class Puente {
    // Constantes
    private static final int MAXIMO_PERSONAS = 4;
    private static final int MAXIMO_PERSONAS_SENTIDO = 3;
    private static final int MAXIMO_PESO = 300;

    // Variables
    private int numeroPersonas = 0;
    private int pesoPersonas = 0;
    private int personasNorte = 0;
    private int personasSur = 0;
    private String sentidoActual = null;

    // Constructor
    public Puente() {}

    // Getters
    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public int getPesoPersonas() {
        return pesoPersonas;
    }

    // Entrar
    public synchronized void entrar(Persona persona) throws InterruptedException {
        String sentidoPersona = persona.getSentido();
        while ((numeroPersonas >= MAXIMO_PERSONAS) ||
               (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO) ||
               (sentidoActual != null && !sentidoActual.equals(sentidoPersona)) ||
               (sentidoPersona.equals("NORTE") && personasNorte >= MAXIMO_PERSONAS_SENTIDO) ||
               (sentidoPersona.equals("SUR") && personasSur >= MAXIMO_PERSONAS_SENTIDO)) {
            System.out.printf("*** La %s debe esperar.\n", persona.getIdPersona());
            this.wait();
        }

        // Permitir el paso de la persona
        numeroPersonas++;
        pesoPersonas += persona.getPesoPersona();
        sentidoActual = sentidoPersona;
        if (sentidoPersona.equals("NORTE")) {
            personasNorte++;
        } else {
            personasSur++;
        }
        
        System.out.printf(">>> La %s entra en dirección %s. Estado del puente: %d personas, %d kilos.\n",
                persona.getIdPersona(), sentidoPersona, numeroPersonas, pesoPersonas);
    }

    // Salir
    public synchronized void salir(Persona persona) {
        String sentidoPersona = persona.getSentido();
        numeroPersonas--;
        pesoPersonas -= persona.getPesoPersona();

        if (sentidoPersona.equals("NORTE")) {
            personasNorte--;
        } else {
            personasSur--;
        }

        if (numeroPersonas == 0) {
            sentidoActual = null;
        }
        
        this.notifyAll();
        System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n",
                persona.getIdPersona(), numeroPersonas, pesoPersonas);
    }
}
