package edu.epsevg.prop.lab.c4.heuristica;

import edu.epsevg.prop.lab.c4.Tauler;

/**
 * Función heurística para evaluar posiciones del tablero
 */
public class Heuristica {

    // Pesos para la evaluación
    private static final int PESO_TRES_EN_LINEA = 5000; // Amenazas críticas
    private static final int PESO_DOS_EN_LINEA = 100;
    private static final int PESO_CENTRO = 10;

    /**
     * Evalúa una posición del tablero desde el punto de vista de un jugador
     * 
     * @param t       Tablero a evaluar
     * @param miColor Color del jugador que evalúa (1 o -1)
     * @return Puntuación de la posición (positiva = buena, negativa = mala)
     */
    public int h(Tauler t, int miColor) {
        int puntuacion = 0;

        // Evaluar líneas de 3 fichas (amenazas/oportunidades)
        puntuacion += contarLineas(t, miColor, 3) * PESO_TRES_EN_LINEA;
        puntuacion -= contarLineas(t, -miColor, 3) * PESO_TRES_EN_LINEA;

        // Evaluar líneas de 2 fichas
        puntuacion += contarLineas(t, miColor, 2) * PESO_DOS_EN_LINEA;
        puntuacion -= contarLineas(t, -miColor, 2) * PESO_DOS_EN_LINEA;

        // Evaluar control del centro
        puntuacion += evaluarCentro(t, miColor) * PESO_CENTRO;

        return puntuacion;
    }

    /**
     * Cuenta líneas de una longitud específica para un color
     */
    private int contarLineas(Tauler t, int color, int longitud) {
        int contador = 0;
        int mida = t.getMida();

        // Verificar líneas horizontales
        for (int fila = 0; fila < mida; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, 0, 1, color, longitud);
            }
        }

        // Verificar líneas verticales
        for (int fila = 0; fila <= mida - 4; fila++) {
            for (int col = 0; col < mida; col++) {
                contador += verificarLinea(t, fila, col, 1, 0, color, longitud);
            }
        }

        // Verificar diagonales (arriba-derecha)
        for (int fila = 0; fila <= mida - 4; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, 1, 1, color, longitud);
            }
        }

        // Verificar diagonales (abajo-derecha)
        for (int fila = 3; fila < mida; fila++) {
            for (int col = 0; col <= mida - 4; col++) {
                contador += verificarLinea(t, fila, col, -1, 1, color, longitud);
            }
        }

        return contador;
    }

    /**
     * Verifica si hay una línea de longitud específica en una dirección
     */
    private int verificarLinea(Tauler t, int fila, int col, int deltaFila, int deltaCol, int color, int longitud) {
        int fichasColor = 0;
        int espaciosVacios = 0;

        // Verificar 4 posiciones en la dirección dada
        for (int i = 0; i < 4; i++) {
            int f = fila + i * deltaFila;
            int c = col + i * deltaCol;
            int valorCasilla = t.getColor(f, c);

            if (valorCasilla == color) {
                fichasColor++;
            } else if (valorCasilla == 0) {
                espaciosVacios++;
            } else {
                // Hay una ficha del oponente, esta línea está bloqueada
                return 0;
            }
        }

        // Si tenemos exactamente la longitud deseada y el resto son espacios vacíos
        if (fichasColor == longitud && espaciosVacios == (4 - longitud)) {
            return 1;
        }

        return 0;
    }

    /**
     * Evalúa el control del centro del tablero
     */
    private int evaluarCentro(Tauler t, int miColor) {
        int puntuacion = 0;
        int mida = t.getMida();
        int centro = mida / 2;

        // Dar más valor a las fichas cercanas al centro
        for (int fila = 0; fila < mida; fila++) {
            for (int col = 0; col < mida; col++) {
                if (t.getColor(fila, col) == miColor) {
                    int distanciaCentro = Math.abs(col - centro);
                    puntuacion += (4 - distanciaCentro);
                }
            }
        }

        return puntuacion;
    }
}
