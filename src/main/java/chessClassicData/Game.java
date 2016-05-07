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

	public void addPosition(Position p) {
		p.setIndex(nextIndex);
		nextIndex++;
		positionTable.put(p.getIndex(), p);
	}

	public Position getPosition(int index) {
		return positionTable.get(index);
	}
}
