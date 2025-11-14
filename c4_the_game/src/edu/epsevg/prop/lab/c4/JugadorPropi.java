package edu.epsevg.prop.lab.c4;

/**
 * Jugador propi
 * "Alea jacta est"
 * @author Profe
 */
public class JugadorPropi
  implements Jugador, IAuto
{
  private String nom;
  
  public JugadorPropi()
  {
    nom = "JugadorPropi";
  }
  
  public int moviment(Tauler t, int color)
  {
    //calcular la jugada en base a la heuristica (minmax)
      
    return 1;
  }
  
  public String nom()
  {
    return nom;
  }
}


