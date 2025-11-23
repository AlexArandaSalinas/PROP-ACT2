package edu.epsevg.prop.lab.c4;

/**
 * Clase para evaluar posiciones del tablero
 * 
 * @author Alex Aranda Salinas
 */
public class Heuristica {

    // Pesos para la evaluación
    private static final int PESO_TRES = 50000;
    private static final int PESO_DOS = 1000;
    private static final int PESO_CENTRO = 100;

    /**
     * Evalúa una posición del tablero
     * 
     * @param t Tablero a evaluar
     * @param miColor Color del jugador (1 o -1)
     * @return Puntuación (positiva = buena, negativa = mala)
     */
    public int h(Tauler t, int miColor) {
        int puntuacion = 0;

        // Contar líneas de 3 fichas
        puntuacion += contarLineas(t, miColor, 3) * PESO_TRES;
        puntuacion -= contarLineas(t, -miColor, 3) * PESO_TRES;

        // Contar líneas de 2 fichas
        puntuacion += contarLineas(t, miColor, 2) * PESO_DOS;
        puntuacion -= contarLineas(t, -miColor, 2) * PESO_DOS;

        // Evaluar control del centro
        puntuacion += evaluarCentro(t, miColor) * PESO_CENTRO;

        // Detectar amenazas inmediatas
        puntuacion += detectarAmenazas(t, miColor);

        return puntuacion;
    }

    /**
     * Detecta si alguien puede ganar en el siguiente turno
     */
    private int detectarAmenazas(Tauler t, int miColor) {
        int mida = t.getMida();

        // Verificar si puedo ganar
        for (int col = 0; col < mida; col++) {
            if (t.movpossible(col)) {
                Tauler copia = new Tauler(t);
                copia.afegeix(col, miColor);
                if (copia.solucio(col, miColor)) {
                    return 90000000; // Puedo ganar!
                }
            }
        }

        // Verificar si el oponente puede ganar
        for (int col = 0; col < mida; col++) {
            if (t.movpossible(col)) {
                Tauler copia = new Tauler(t);
                copia.afegeix(col, -miColor);
                if (copia.solucio(col, -miColor)) {
                    return -90000000; // El oponente puede ganar!
                }
            }
        }

        return 0;
    }

    /**
     * Cuenta líneas de una longitud específica
     */
    private int contarLineas(Tauler t, int color, int longitud) {
        int contador = 0;
        int mida = t.getMida();

        // Horizontales
        for (int fila = 0; fila < mida; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, 0, 1, color, longitud);
            }
        }

        // Verticales
        for (int fila = 0; fila <= mida - 4; fila++) {
            for (int col = 0; col < mida; col++) {
                contador += verificarLinea(t, fila, col, 1, 0, color, longitud);
            }
        }

        // Diagonales /
        for (int fila = 0; fila <= mida - 4; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, 1, 1, color, longitud);
            }
        }

        // Diagonales \
        for (int fila = 3; fila < mida; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, -1, 1, color, longitud);
            }
        }

        return contador;
    }

    /**
     * Verifica si hay una línea en una dirección
     */
    private int verificarLinea(Tauler t, int fila, int col, int deltaFila, int deltaCol, int color, int longitud) {
        int fichas = 0;
        int vacias = 0;

        // Verificar 4 posiciones
        for (int i = 0; i < 4; i++) {
            int f = fila + i * deltaFila;
            int c = col + i * deltaCol;
            int casilla = t.getColor(f, c);

            if (casilla == color) {
                fichas++;
            } else if (casilla == 0) {
                vacias++;
            } else {
                return 0; // Bloqueada
            }
        }

        // Si tenemos la longitud deseada
        if (fichas == longitud && vacias == (4 - longitud)) {
            return 1;
        }

        return 0;
    }

    /**
     * Evalúa el control del centro
     */
    private int evaluarCentro(Tauler t, int miColor) {
        int puntuacion = 0;
        int mida = t.getMida();
        int centro = mida / 2;

        for (int fila = 0; fila < mida; fila++) {
            for (int col = 0; col < mida; col++) {
                if (t.getColor(fila, col) == miColor) {
                    int distancia = Math.abs(col - centro);
                    puntuacion += (4 - distancia);
                }
            }
        }

        return puntuacion;
    }
}
