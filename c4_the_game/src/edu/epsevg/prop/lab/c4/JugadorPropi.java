package edu.epsevg.prop.lab.c4;

/**
 * Jugador propi - Implementación con Minimax
 * "Alea jacta est"
 * 
 * @author Profe
 */
public class JugadorPropi implements Jugador, IAuto {
  private String nom;

  // Constantes para el algoritmo Minimax
  private static final int PROFUNDIDAD_MAX = 6;
  private static final int VICTORIA = 10000;
  private static final int DERROTA = -10000;

  public JugadorPropi() {
    nom = "JugadorPropi";
  }

  public int moviment(Tauler t, int color) {
    int mejorColumna = -1;
    int mejorValor = Integer.MIN_VALUE;

    for (int col = 0; col < t.getMida(); col++) {
      if (t.movpossible(col)) {

        Tauler copia = new Tauler(t);
        copia.afegeix(col, color);

        if (copia.solucio(col, color)) {
          return col;
        }

        int valor = minimax(copia, PROFUNDIDAD_MAX - 1, false, color, -color, col);

        if (valor > mejorValor) {
          mejorValor = valor;
          mejorColumna = col;
        }
      }
    }

    return mejorColumna;
  }

  /**
   * Algoritmo Minimax básico
   */
  private int minimax(Tauler t, int profundidad, boolean esMax, int miColor, int colorActual, int ultimaColumna) {
    // Caso base: verificar si el último movimiento fue ganador
    if (t.solucio(ultimaColumna, -colorActual)) {
      // El jugador anterior (-colorActual) acaba de ganar
      if (-colorActual == miColor) {
        return VICTORIA;
      } else {
        return DERROTA;
      }
    }

    // Caso base: no hay más movimientos (empate)
    if (!t.espotmoure()) {
      return 0;
    }

    // Caso base: profundidad máxima alcanzada
    if (profundidad == 0) {
      // Sin heurística por ahora, retornamos 0
      return 0;
    }

    // Caso recursivo
    if (esMax) {
      // Nodo MAX:
      int maxValor = Integer.MIN_VALUE;

      for (int col = 0; col < t.getMida(); col++) {
        if (t.movpossible(col)) {
          Tauler copia = new Tauler(t);
          copia.afegeix(col, colorActual);

          int valor = minimax(copia, profundidad - 1, false, miColor, -colorActual, col);
          maxValor = Math.max(maxValor, valor);
        }
      }

      return maxValor;
    } else {
      // Nodo MIN:
      int minValor = Integer.MAX_VALUE;

      for (int col = 0; col < t.getMida(); col++) {
        if (t.movpossible(col)) {
          Tauler copia = new Tauler(t);
          copia.afegeix(col, colorActual);

          int valor = minimax(copia, profundidad - 1, true, miColor, -colorActual, col);
          minValor = Math.min(minValor, valor);
        }
      }

      return minValor;
    }
  }

  public String nom() {
    return nom;
  }
}
