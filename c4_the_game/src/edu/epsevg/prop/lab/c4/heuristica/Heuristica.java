package edu.epsevg.prop.lab.c4.heuristica;

import edu.epsevg.prop.lab.c4.Tauler;

/**
 * Función heurística para evaluar posiciones del tablero de Conecta 4.
 * 
 * <p>
 * Esta clase implementa una función de evaluación que asigna una puntuación
 * numérica
 * a cualquier posición del tablero, permitiendo al algoritmo Minimax comparar
 * diferentes
 * estados del juego sin necesidad de explorar hasta el final de la partida.
 * </p>
 * 
 * <p>
 * La evaluación se basa en tres factores principales:
 * </p>
 * <ul>
 * <li><b>Amenazas críticas:</b> Líneas de 3 fichas del mismo color (peso
 * 50000)</li>
 * <li><b>Oportunidades:</b> Líneas de 2 fichas del mismo color (peso 1000)</li>
 * <li><b>Control del centro:</b> Fichas en columnas centrales (peso 100)</li>
 * </ul>
 * 
 * <p>
 * La puntuación final es positiva si favorece al jugador y negativa si favorece
 * al oponente.
 * </p>
 * 
 * @author Alex Aranda Salinas
 * @version 1.0
 */
public class Heuristica {

    /** Peso para líneas de 3 fichas (amenazas/oportunidades críticas) */
    private static final int PESO_TRES_EN_LINEA = 50000;

    /** Peso para líneas de 2 fichas (oportunidades en desarrollo) */
    private static final int PESO_DOS_EN_LINEA = 1000;

    /** Peso para el control de columnas centrales */
    private static final int PESO_CENTRO = 100;

    /**
     * Evalúa una posición del tablero desde el punto de vista de un jugador.
     * 
     * <p>
     * Esta función calcula una puntuación que refleja qué tan favorable es la
     * posición actual para el jugador especificado. La evaluación considera:
     * </p>
     * <ul>
     * <li>Líneas de 3 fichas propias (suma puntos) y del oponente (resta
     * puntos)</li>
     * <li>Líneas de 2 fichas propias (suma puntos) y del oponente (resta
     * puntos)</li>
     * <li>Control del centro del tablero (suma puntos)</li>
     * </ul>
     * 
     * @param t       Tablero a evaluar
     * @param miColor Color del jugador que evalúa (1 para rojo, -1 para azul)
     * @return Puntuación de la posición (positiva si favorece a miColor, negativa
     *         si favorece al oponente)
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

        // MEJORA: Detectar amenazas inmediatas (victoria en el siguiente turno)
        puntuacion += detectarAmenazasInmediatas(t, miColor);

        return puntuacion;
    }

    /**
     * Detecta si algún jugador puede ganar en el siguiente movimiento.
     * 
     * <p>
     * Esta función simula todos los movimientos posibles para ambos jugadores
     * y verifica si alguno resulta en victoria inmediata. Esto permite detectar
     * amenazas que están fuera del alcance de la profundidad de búsqueda.
     * </p>
     * 
     * @param t       Tablero a analizar
     * @param miColor Color del jugador que evalúa
     * @return Penalización/bonificación muy alta si hay amenaza/oportunidad
     *         inmediata
     */
    private int detectarAmenazasInmediatas(Tauler t, int miColor) {
        int mida = t.getMida();

        // Verificar si YO puedo ganar en el siguiente turno
        for (int col = 0; col < mida; col++) {
            if (t.movpossible(col)) {
                Tauler copia = new Tauler(t);
                copia.afegeix(col, miColor);
                if (copia.solucio(col, miColor)) {
                    // ¡Puedo ganar en el siguiente turno! (ya lo detecta moviment, pero refuerza)
                    return 90000000;
                }
            }
        }

        // Verificar si el OPONENTE puede ganar en el siguiente turno
        for (int col = 0; col < mida; col++) {
            if (t.movpossible(col)) {
                Tauler copia = new Tauler(t);
                copia.afegeix(col, -miColor);
                if (copia.solucio(col, -miColor)) {
                    // ¡El oponente puede ganar! Esto es MUY malo
                    return -90000000;
                }
            }
        }

        return 0; // No hay amenazas inmediatas
    }

    /**
     * Cuenta el número de líneas de una longitud específica para un color dado.
     * 
     * <p>
     * Este método examina todas las posibles líneas de 4 casillas en el tablero
     * (horizontales, verticales y diagonales) y cuenta cuántas contienen
     * exactamente
     * la cantidad especificada de fichas del color dado, sin fichas del oponente.
     * </p>
     * 
     * @param t        Tablero a analizar
     * @param color    Color de las fichas a contar (1 o -1)
     * @param longitud Número de fichas del color especificado que debe haber en la
     *                 línea (2 o 3)
     * @return Número de líneas encontradas con la longitud especificada
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
     * Verifica si hay una línea de longitud específica en una dirección dada.
     * 
     * <p>
     * Examina 4 casillas consecutivas en la dirección especificada y determina
     * si contienen exactamente el número deseado de fichas del color dado, con el
     * resto de casillas vacías (sin fichas del oponente).
     * </p>
     * 
     * @param t         Tablero a analizar
     * @param fila      Fila inicial
     * @param col       Columna inicial
     * @param deltaFila Incremento de fila para cada paso (0 para horizontal, 1 o -1
     *                  para diagonal/vertical)
     * @param deltaCol  Incremento de columna para cada paso (0 para vertical, 1
     *                  para horizontal/diagonal)
     * @param color     Color de las fichas a buscar
     * @param longitud  Número de fichas del color especificado que debe haber
     * @return 1 si se encuentra una línea válida, 0 en caso contrario
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
     * Evalúa el control del centro del tablero para un jugador.
     * 
     * <p>
     * En Conecta 4, controlar las columnas centrales es estratégicamente importante
     * porque ofrece más oportunidades de formar líneas ganadoras. Este método
     * asigna
     * más puntos a las fichas colocadas en columnas cercanas al centro.
     * </p>
     * 
     * @param t       Tablero a evaluar
     * @param miColor Color del jugador a evaluar
     * @return Puntuación basada en el control del centro (mayor valor = mejor
     *         control)
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
