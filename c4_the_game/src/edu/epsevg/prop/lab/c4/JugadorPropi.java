package edu.epsevg.prop.lab.c4;

import edu.epsevg.prop.lab.c4.heuristica.Heuristica;

/**
 * Implementación de un jugador de Conecta 4 utilizando el algoritmo Minimax con
 * poda Alfa-Beta.
 * 
 * <p>
 * Este jugador utiliza una búsqueda en profundidad con poda alfa-beta para
 * explorar
 * el árbol de juego y encontrar la mejor jugada posible. La evaluación de
 * posiciones
 * intermedias se realiza mediante una función heurística que considera
 * amenazas,
 * oportunidades y control del centro del tablero.
 * </p>
 * 
 * <p>
 * Características principales:
 * </p>
 * <ul>
 * <li>Algoritmo Minimax con poda Alfa-Beta</li>
 * <li>Profundidad de búsqueda configurable (por defecto 8)</li>
 * <li>Función heurística para evaluar posiciones intermedias</li>
 * <li>Contador de nodos explorados para análisis de rendimiento</li>
 * </ul>
 * 
 * @author Alex Aranda Salinas
 * @version 1.0
 */
public class JugadorPropi implements Jugador, IAuto {
  /** Nombre del jugador */
  private String nom;

  /** Función heurística para evaluar posiciones del tablero */
  private Heuristica heuristica;

  /** Profundidad máxima de búsqueda en el árbol minimax */
  private int profundidadMax;

  /** Contador de nodos explorados en la última búsqueda */
  private int nodosExplorados;

  /** Valor que representa una victoria garantizada */
  private static final int VICTORIA = 100000000;

  /** Valor que representa una derrota garantizada */
  private static final int DERROTA = -100000000;

  /**
   * Constructor con profundidad por defecto
   */
  public JugadorPropi() {
    this(8); // Profundidad por defecto: 8
  }

  /**
   * Constructor con profundidad parametrizada
   * 
   * @param profundidad Profundidad máxima del árbol minimax
   */
  public JugadorPropi(int profundidad) {
    nom = "JugadorPropi";
    heuristica = new Heuristica();
    profundidadMax = profundidad;
    nodosExplorados = 0;
  }

  /**
   * Determina el mejor movimiento para el jugador actual.
   * 
   * <p>
   * Este método implementa la lógica principal del jugador, utilizando el
   * algoritmo
   * Minimax con poda Alfa-Beta para explorar el árbol de juego y encontrar la
   * mejor
   * columna donde colocar la siguiente ficha.
   * </p>
   * 
   * <p>
   * El proceso incluye:
   * </p>
   * <ol>
   * <li>Verificación de victorias inmediatas</li>
   * <li>Evaluación de cada columna posible mediante Minimax</li>
   * <li>Selección de la columna con mejor valoración</li>
   * </ol>
   * 
   * @param t     Tablero actual del juego
   * @param color Color del jugador (1 para rojo, -1 para azul)
   * @return Índice de la columna donde se debe colocar la ficha (0-7)
   */
  public int moviment(Tauler t, int color) {
    int mejorColumna = -1;
    int mejorValor = Integer.MIN_VALUE;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    // Resetear contador de nodos explorados
    nodosExplorados = 0;

    System.out.println("\nTURNO: " + (color == 1 ? "ROJO" : "AZUL"));

    for (int col = 0; col < t.getMida(); col++) {
      if (t.movpossible(col)) {

        Tauler copia = new Tauler(t);
        copia.afegeix(col, color);

        if (copia.solucio(col, color)) {
          System.out.println("¡VICTORIA INMEDIATA en columna " + col + "!");
          return col;
        }

        int valor = minimax(copia, profundidadMax - 1, false, color, -color, col, alpha, beta);

        System.out.println("Col " + col + ": " + valor + (valor > mejorValor ? " ← MEJOR" : ""));

        if (valor > mejorValor) {
          mejorValor = valor;
          mejorColumna = col;
        }

        alpha = Math.max(alpha, valor);
      }
    }

    System.out
        .println("-> Juega columna " + mejorColumna + " (valor: " + mejorValor + ", nodos: " + nodosExplorados + ")\n");

    return mejorColumna;
  }

  /**
   * Implementación del algoritmo Minimax con poda Alfa-Beta.
   * 
   * <p>
   * Este método recursivo explora el árbol de juego alternando entre nodos MAX
   * (maximizando la puntuación del jugador) y nodos MIN (minimizando la
   * puntuación
   * del oponente). La poda alfa-beta elimina ramas que no pueden influir en la
   * decisión final, mejorando significativamente el rendimiento.
   * </p>
   * 
   * <p>
   * Casos base:
   * </p>
   * <ul>
   * <li>Victoria/Derrota: Retorna valores extremos (±100000000)</li>
   * <li>Empate: Retorna 0</li>
   * <li>Profundidad máxima: Evalúa la posición con la función heurística</li>
   * </ul>
   * 
   * @param t             Tablero actual
   * @param profundidad   Profundidad restante de búsqueda
   * @param esMax         true si es un nodo MAX (turno del jugador), false si es
   *                      MIN (turno del oponente)
   * @param miColor       Color del jugador que realiza la búsqueda
   * @param colorActual   Color del jugador que debe mover en este nodo
   * @param ultimaColumna Columna donde se colocó la última ficha
   * @param alpha         Mejor valor encontrado para MAX
   * @param beta          Mejor valor encontrado para MIN
   * @return Valoración de la posición desde el punto de vista de miColor
   */
  private int minimax(Tauler t, int profundidad, boolean esMax, int miColor, int colorActual, int ultimaColumna,
      int alpha, int beta) {
    // Caso base: verificar si el último movimiento fue ganador
    if (t.solucio(ultimaColumna, -colorActual)) {
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
      nodosExplorados++;
      return heuristica.h(t, miColor);
    }

    // Caso recursivo
    if (esMax) {
      // Nodo MAX:
      int maxValor = Integer.MIN_VALUE;

      for (int col = 0; col < t.getMida(); col++) {
        if (t.movpossible(col)) {
          Tauler copia = new Tauler(t);
          copia.afegeix(col, colorActual);

          int valor = minimax(copia, profundidad - 1, false, miColor, -colorActual, col, alpha, beta);
          maxValor = Math.max(maxValor, valor);

          // Poda alfa:
          alpha = Math.max(alpha, valor);
          if (alpha >= beta) {
            break; // Poda beta
          }
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

          int valor = minimax(copia, profundidad - 1, true, miColor, -colorActual, col, alpha, beta);
          minValor = Math.min(minValor, valor);

          // Poda beta:
          beta = Math.min(beta, valor);
          if (beta <= alpha) {
            break; // Poda alfa
          }
        }
      }

      return minValor;
    }
  }

  /**
   * Obtiene el nombre del jugador.
   * 
   * @return Nombre del jugador
   */
  public String nom() {
    return nom;
  }
}
