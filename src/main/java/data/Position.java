package data;

/*
 * Contient une position, c'est à dire le bitboard associé ainsi qu'un pointeur
 * vers la ou les prochaine positions ainsi que vers la position précédente.
 */
public class Position {
	Bitboard bitboard;
	Position nextPosition;
	Position previousPosition;
	
	/**
	 * Crée une position vide;
	 */
	public Position() {
		this.bitboard = new Bitboard();
	}
	
	/**
	 * Retourne True s'il n'y a pas de piece dans la position
	 * @return
	 */
	public boolean isEmpty(){
		return this.bitboard.isEmpty();
	}
}
