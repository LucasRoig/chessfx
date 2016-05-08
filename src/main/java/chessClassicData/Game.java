package chessClassicData;

import java.util.Hashtable;

public class Game {
	private Hashtable<Integer, Position> positionTable = new Hashtable<>(); // Conserve
																			// toutes
																			// les
																			// positions
																			// de
																			// la
																			// partie
	private int nextIndex = 0;
	
	/**
	 * Retourne une partie initialisée dans la position de départ.
	 * @return
	 */
	public static Game getStartingGame(){
		Game g = new Game();
		Position p = Position.getStartingPosition();
		g.setPositionIndex(p);
		g.addToPositionTable(p);
		return g;
	}
	/**
	 * Ajoute la position à la partie après l'index donnée.
	 * Si la position de l'index donné a déjà une position suivante, ajoute une variante.
	 * Si la position est déjà dans les variantes ou en tant que position suivante, ne fait rien.
	 * Retourne l'index de la nouvelle position.
	 * @param newPosition
	 * @param index
	 */
	public int addPositionAfter(Position newPosition,int index) {
		Position indexPosition = this.getPosition(index);
		if(indexPosition.isLastPositon()){
			this.setPositionIndex(newPosition);
			this.addToPositionTable(newPosition);
			indexPosition.setNextPosition(newPosition);
			return newPosition.getIndex();
		}else if(indexPosition.getNextPosition().equals(newPosition)){
			return indexPosition.getNextPosition().getIndex();
		}else {
			for (Position subline : indexPosition.getSublines()) {
				if(subline.equals(newPosition)){
					return subline.getIndex();
				}
			}
			this.setPositionIndex(newPosition);
			this.addToPositionTable(newPosition);
			indexPosition.addSubLine(newPosition);
			return newPosition.getIndex();
		}
	}

	public Position getPosition(int index) {
		return positionTable.get(index);
	}
	
	/**
	 * Ajoute la position p à la table des position.
	 * @param p
	 */
	private void addToPositionTable(Position p){
		this.positionTable.put(p.getIndex(),p);
	}
	/**
	 * Initialise l'index de la position conformément à la partie
	 * @param p
	 */
	private void setPositionIndex(Position p){
		p.setIndex(nextIndex);
		nextIndex ++;
	}
}
