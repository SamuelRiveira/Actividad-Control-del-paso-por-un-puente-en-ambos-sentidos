package com.mycompany.puentethreadsafe;

import java.util.Random;

public class Puentethreadsafe {
    public static void main(String[] args) {
        // Constantes
        final int MINIMO_TIEMPO_LLEGADA = 1;
        final int MAXIMO_TIEMPO_LLEGADA = 30;
        final int MINIMO_TIEMPO_PASO = 10;
        final int MAXIMO_TIEMPO_PASO = 50;
        final int MINIMO_PESO_PERSONA = 40;
        final int MAXIMO_PESO_PERSONA = 120;

        // Variables
        final Puente puente = new Puente();
        String idPersona = "";
        int tiempoLlegada;
        int tiempoPaso;
        int pesoPersona;

        // Bucle infinito creando personas para cruzar el puente
        int numeroPersona = 0;
        while (true) {
            // Crear una persona
            numeroPersona++;
            idPersona = "Persona " + numeroPersona;
            tiempoLlegada = numeroAleatorio(MINIMO_TIEMPO_LLEGADA, MAXIMO_TIEMPO_LLEGADA);
            tiempoPaso = numeroAleatorio(MINIMO_TIEMPO_PASO, MAXIMO_TIEMPO_PASO);
            pesoPersona = numeroAleatorio(MINIMO_PESO_PERSONA, MAXIMO_PESO_PERSONA);

            // Asignar dirección aleatoria
            String sentido = numeroAleatorio(0, 1) == 0 ? "NORTE" : "SUR";
            System.out.printf("La %s llegará en %d segundos, pesa %d kilos, irá hacia %s y tardará %d segundos en cruzar.\n",
                    idPersona, tiempoLlegada, pesoPersona, sentido, tiempoPaso);

            Thread hiloPersona = new Thread(new Persona(idPersona, tiempoPaso, pesoPersona, sentido, puente));
            
            // Esperar a que llegue
            try {
                Thread.sleep(tiempoLlegada * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // La persona cruza
            hiloPersona.start();
        }
    }

    public static int numeroAleatorio(int valorMinimo, int valorMaximo) {
        Random r = new Random();
        return valorMinimo + r.nextInt((valorMaximo - valorMinimo) + 1);
    }
}
