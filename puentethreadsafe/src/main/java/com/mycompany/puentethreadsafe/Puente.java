package com.mycompany.puentethreadsafe;

public class Puente {

    private int numeroPersonas = 0;
    private int pesoPersonas = 0;
    private String sentidoActual = null;
    private int personasNorte = 0;
    private int personasSur = 0;

    private static final int MAXIMO_PERSONAS = 4;
    private static final int MAXIMO_PESO = 300;
    private static final int MAXIMO_PERSONAS_SENTIDO = 3;

    // Método de entrada al puente
    public synchronized void entrar(Persona persona) throws InterruptedException {
        String sentidoPersona = persona.getSentido();

        // Verificamos las condiciones de espera
        while ((numeroPersonas >= MAXIMO_PERSONAS) ||  // Si el puente está lleno
               (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO) ||  // Si el peso excede el máximo
               (sentidoPersona.equals("NORTE") && personasNorte >= MAXIMO_PERSONAS_SENTIDO) ||  // Si ya hay demasiadas personas hacia el Norte
               (sentidoPersona.equals("SUR") && personasSur >= MAXIMO_PERSONAS_SENTIDO)) {  // Si ya hay demasiadas personas hacia el Sur

            System.out.printf("*** La %s debe esperar. (Condición: %s)\n", persona.getIdPersona(), 
                              (numeroPersonas >= MAXIMO_PERSONAS) ? "Puente lleno" :
                              (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO) ? "Peso excedido" :
                              (sentidoPersona.equals("NORTE") && personasNorte >= MAXIMO_PERSONAS_SENTIDO) ? "Demasiadas personas hacia NORTE" :
                              "Demasiadas personas hacia SUR");
            this.wait();  // Espera hasta que alguien salga o haya espacio
        }

        // Si pasa las condiciones, entra en el puente
        numeroPersonas++;
        pesoPersonas += persona.getPesoPersona();

        if (sentidoPersona.equals("NORTE")) {
            personasNorte++;
        } else {
            personasSur++;
        }

        // Se actualiza la dirección del puente solo si no hay personas cruzando
        if (sentidoActual == null) {
            sentidoActual = sentidoPersona;
        }

        System.out.printf(">>> La %s entra en dirección %s. Estado del puente: %d personas, %d kilos.\n",
                persona.getIdPersona(), sentidoPersona, numeroPersonas, pesoPersonas);
    }

    // Método de salida del puente
    public synchronized void salir(Persona persona) {
        String sentidoPersona = persona.getSentido();
        numeroPersonas--;
        pesoPersonas -= persona.getPesoPersona();

        if (sentidoPersona.equals("NORTE")) {
            personasNorte--;
        } else {
            personasSur--;
        }

        // Si el puente está vacío, permite que alguien más entre
        if (numeroPersonas == 0) {
            sentidoActual = null;  // Reiniciar el sentido
        }

        System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n", persona.getIdPersona(), numeroPersonas, pesoPersonas);

        // Notificar a las personas que están esperando
        this.notifyAll();
    }
}
