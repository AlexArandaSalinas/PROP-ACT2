package edu.epsevg.prop.lab.c4;

/**
 * Jugador Propio - Implementación con Minimax y Poda Alfa-Beta
 * 
 * @author Alex Aranda Salinas
 */
public class JugadorPropi implements Jugador, IAuto {
  private String nom;
  private Heuristica heuristica;
  private int profundidadMax;
  private int nodosExplorados;

  // Constantes para victoria y derrota
  private static final int VICTORIA = 100000000;
  private static final int DERROTA = -100000000;

  /**
   * Constructor por defecto
   * Crea un jugador con profundidad 8
   */
  public JugadorPropi() {
    this(8);
  }

  /**
   * Constructor con profundidad personalizada
   * 
   * @param profundidad Profundidad máxima de búsqueda
   */
  public JugadorPropi(int profundidad) {
    nom = "JugadorPropi";
    heuristica = new Heuristica();
    profundidadMax = profundidad;
    nodosExplorados = 0;
  }

  /**
   * Genera orden de exploración centro-primero
   * 
   * @param mida Tamaño del tablero
   * @return Array con orden de columnas
   */
  private int[] generarOrdenCentroPrimero(int mida) {
    int[] orden = new int[mida];
    int centro = mida / 2;
    int izq = centro - 1;
    int der = centro;
    int pos = 0;

    // Alternar entre centro-derecha y centro-izquierda
    while (pos < mida) {
      if (der < mida) {
        orden[pos++] = der++;
      }
      if (izq >= 0 && pos < mida) {
        orden[pos++] = izq--;
      }
    }

    return orden;
  }

  /**
   * Decide el mejor movimiento
   * 
   * @param t     Tablero actual
   * @param color Color del jugador (1 o -1)
   * @return Columna donde jugar (0-7)
   */
  @Override
  public int moviment(Tauler t, int color) {
    int mejorColumna = -1;
    int mejorValor = Integer.MIN_VALUE;

    nodosExplorados = 0;

    // Probar cada columna posible (orden centro-primero para mejor poda)
    int[] ordenColumnas = generarOrdenCentroPrimero(t.getMida());
    for (int i = 0; i < ordenColumnas.length; i++) {
      int col = ordenColumnas[i];
      if (!t.movpossible(col))
        continue;

      Tauler copia = new Tauler(t);
      copia.afegeix(col, color);

      // Comprobar victoria inmediata
      if (copia.solucio(col, color)) {
        return col;
      }

      int alpha = Integer.MIN_VALUE;
      int beta = Integer.MAX_VALUE;

      int valor = minValor(copia, -color, col, profundidadMax - 1, color, alpha, beta);

      if (valor > mejorValor) {
        mejorValor = valor;
        mejorColumna = col;
      }
    }

    System.out.println("Nodos explorados: " + nodosExplorados);
    return mejorColumna;
  }

  /**
   * Nodo MIN - Minimiza el valor (turno del oponente)
   * 
   * @param t              Tablero actual
   * @param color          Color del jugador actual
   * @param ultimaCol      Última columna jugada
   * @param profundidad    Profundidad restante
   * @param jugadorInicial Color del jugador inicial
   * @param alpha          Valor alpha
   * @param beta           Valor beta
   * @return Valoración de la posición
   */
  private int minValor(Tauler t, int color, int ultimaCol, int profundidad, int jugadorInicial, int alpha, int beta) {
    // Comprobar si el movimiento anterior fue ganador
    if (t.solucio(ultimaCol, -color)) {
      if (-color == jugadorInicial) {
        return VICTORIA;
      } else {
        return DERROTA;
      }
    }

    // Tablero lleno (empate)
    if (!t.espotmoure()) {
      return 0;
    }

    // Profundidad máxima alcanzada - evaluar con heurística
    if (profundidad == 0) {
      nodosExplorados++;
      return heuristica.h(t, jugadorInicial);
    }

    int valor = Integer.MAX_VALUE;

    // Explorar en orden centro-primero
    int[] ordenColumnas = generarOrdenCentroPrimero(t.getMida());
    for (int i = 0; i < ordenColumnas.length; i++) {
      int col = ordenColumnas[i];
      if (!t.movpossible(col))
        continue;

      Tauler copia = new Tauler(t);
      copia.afegeix(col, color);

      valor = Math.min(valor, maxValor(copia, -color, col, profundidad - 1, jugadorInicial, alpha, beta));

      // Poda alfa
      if (valor <= alpha) {
        return valor;
      }
      beta = Math.min(beta, valor);
    }

    return valor;
  }

  /**
   * Nodo MAX - Maximiza el valor (nuestro turno)
   * 
   * @param t              Tablero actual
   * @param color          Color del jugador actual
   * @param ultimaCol      Última columna jugada
   * @param profundidad    Profundidad restante
   * @param jugadorInicial Color del jugador inicial
   * @param alpha          Valor alpha
   * @param beta           Valor beta
   * @return Valoración de la posición
   */
  private int maxValor(Tauler t, int color, int ultimaCol, int profundidad, int jugadorInicial, int alpha, int beta) {
    // Comprobar si el movimiento anterior fue ganador
    if (t.solucio(ultimaCol, -color)) {
      if (-color == jugadorInicial) {
        return VICTORIA;
      } else {
        return DERROTA;
      }
    }

    // Tablero lleno (empate)
    if (!t.espotmoure()) {
      return 0;
    }

    // Profundidad máxima alcanzada - evaluar con heurística
    if (profundidad == 0) {
      nodosExplorados++;
      return heuristica.h(t, jugadorInicial);
    }

    int valor = Integer.MIN_VALUE;

    // Explorar en orden centro-primero
    int[] ordenColumnas = generarOrdenCentroPrimero(t.getMida());
    for (int i = 0; i < ordenColumnas.length; i++) {
      int col = ordenColumnas[i];
      if (!t.movpossible(col))
        continue;

      Tauler copia = new Tauler(t);
      copia.afegeix(col, color);

      valor = Math.max(valor, minValor(copia, -color, col, profundidad - 1, jugadorInicial, alpha, beta));

      // Poda beta
      if (beta <= valor) {
        return valor;
      }
      alpha = Math.max(alpha, valor);
    }

    return valor;
  }

  /**
   * Devuelve el nombre del jugador
   * 
   * @return Nombre del jugador
   */
  @Override
  public String nom() {
    return nom;
  }
}
