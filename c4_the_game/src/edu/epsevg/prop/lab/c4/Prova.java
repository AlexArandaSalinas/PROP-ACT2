package edu.epsevg.prop.lab.c4;

/**
 * Clase de prueba para verificar el comportamiento del JugadorPropi
 * en situaciones difíciles y casos extremos.
 * 
 * @author Alex Aranda Salinas
 */
public class Prova {

    /**
     * Prueba que el jugador detecta y aprovecha una victoria inmediata
     */
    public static void testVictoriaInmediata() {
        System.out.println("\n=== TEST 1: Victoria Inmediata ===");
        Tauler t = new Tauler(8);

        // Crear situación donde ROJO puede ganar en columna 3
        t.afegeix(0, 1); // ROJO
        t.afegeix(1, 1); // ROJO
        t.afegeix(2, 1); // ROJO
        // Columna 3 está libre - debería jugar aquí para ganar

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO

        System.out.println("Columna elegida: " + columna);
        System.out.println("Esperado: 3 (victoria horizontal)");
        System.out.println("✓ Test " + (columna == 3 ? "PASADO" : "FALLADO"));
    }

    /**
     * Prueba que el jugador bloquea una amenaza del oponente
     */
    public static void testBloquearAmenaza() {
        System.out.println("\n=== TEST 2: Bloquear Amenaza ===");
        Tauler t = new Tauler(8);

        // AZUL tiene 3 en línea, ROJO debe bloquear
        t.afegeix(0, -1); // AZUL
        t.afegeix(1, -1); // AZUL
        t.afegeix(2, -1); // AZUL
        // Columna 3 está libre - ROJO debe bloquear aquí

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO debe bloquear

        System.out.println("Columna elegida: " + columna);
        System.out.println("Esperado: 3 (bloquear amenaza)");
        System.out.println("✓ Test " + (columna == 3 ? "PASADO" : "FALLADO"));
    }

    /**
     * Prueba situación de doble amenaza (fork)
     */
    public static void testDobleAmenaza() {
        System.out.println("\n=== TEST 3: Crear Doble Amenaza ===");
        Tauler t = new Tauler(8);

        // Crear situación donde ROJO puede crear doble amenaza
        t.afegeix(1, 1); // ROJO
        t.afegeix(2, 1); // ROJO
        t.afegeix(1, 1); // ROJO
        t.afegeix(3, -1); // AZUL
        t.afegeix(4, -1); // AZUL

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO

        System.out.println("Columna elegida: " + columna);
        System.out.println("Debería crear amenazas múltiples");
    }

    /**
     * Prueba victoria vertical
     */
    public static void testVictoriaVertical() {
        System.out.println("\n=== TEST 4: Victoria Vertical ===");
        Tauler t = new Tauler(8);

        // ROJO tiene 3 fichas en columna 4, puede ganar verticalmente
        t.afegeix(4, 1); // ROJO
        t.afegeix(4, 1); // ROJO
        t.afegeix(4, 1); // ROJO

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO

        System.out.println("Columna elegida: " + columna);
        System.out.println("Esperado: 4 (victoria vertical)");
        System.out.println("✓ Test " + (columna == 4 ? "PASADO" : "FALLADO"));
    }

    /**
     * Prueba victoria diagonal
     */
    public static void testVictoriaDiagonal() {
        System.out.println("\n=== TEST 5: Victoria Diagonal ===");
        Tauler t = new Tauler(8);

        // Crear diagonal ascendente para ROJO
        t.afegeix(0, 1); // ROJO en (0,0)

        t.afegeix(1, -1); // AZUL en (1,0)
        t.afegeix(1, 1); // ROJO en (1,1)

        t.afegeix(2, -1); // AZUL en (2,0)
        t.afegeix(2, -1); // AZUL en (2,1)
        t.afegeix(2, 1); // ROJO en (2,2)

        t.afegeix(3, -1); // AZUL en (3,0)
        t.afegeix(3, -1); // AZUL en (3,1)
        t.afegeix(3, -1); // AZUL en (3,2)
        // Columna 3 posición 3 está libre - ROJO puede ganar diagonal

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO

        System.out.println("Columna elegida: " + columna);
        System.out.println("Esperado: 3 (victoria diagonal)");
        System.out.println("✓ Test " + (columna == 3 ? "PASADO" : "FALLADO"));
    }

    /**
     * Prueba tablero casi lleno
     */
    public static void testTableroLleno() {
        System.out.println("\n=== TEST 6: Tablero Casi Lleno ===");
        Tauler t = new Tauler(8);

        // Llenar casi todo el tablero
        for (int col = 0; col < 7; col++) {
            for (int i = 0; i < 7; i++) {
                t.afegeix(col, (i % 2 == 0) ? 1 : -1);
            }
        }
        // Solo columna 7 tiene espacio

        t.pintaTaulerALaConsola();

        JugadorPropi jugador = new JugadorPropi();
        int columna = jugador.moviment(t, 1); // ROJO

        System.out.println("Columna elegida: " + columna);
        System.out.println("Esperado: 7 (única columna disponible)");
        System.out.println("✓ Test " + (columna == 7 ? "PASADO" : "FALLADO"));
    }

    /**
     * Ejecuta todos los tests
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  TESTS DE JUGADORPROPI - CASOS DIFÍCILES  ║");
        System.out.println("╚════════════════════════════════════════╝");

        testVictoriaInmediata();
        testBloquearAmenaza();
        testDobleAmenaza();
        testVictoriaVertical();
        testVictoriaDiagonal();
        testTableroLleno();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         TESTS COMPLETADOS              ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}
