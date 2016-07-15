package chessfx.pgn;

import java.io.BufferedReader;
import java.io.IOException;

import chessfx.core.Move;
import chessfx.core.board.IWritableGamePosition;
import chessfx.core.game.IGame;
import chessfx.core.game.IGameMoves;

public class PgnReader {
	private BufferedReader reader;
	private String line;
	private IGame game;
	private IGameMoves moves;
	private int commentLevel = 0;

	public PgnReader(IGame game) {
		this.game = game;
		this.moves = game.getMoves();
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public IGame parseGame() throws IOException {
		line = reader.readLine();
		parseHeader();
		while (line != null) {
			cleanLine();
			System.out.println(line);
			int i = 0;
			StringBuffer token = new StringBuffer();
			while (i < line.length()) {
				char c = line.charAt(i);
				if (c == '(') {
					enterVariation();
				} else if (c == ')') {
					exitVariation();
				} else if (c == ' ') {
					String san = token.toString();
					//System.out.println(san);
					token.setLength(0);
					san.replaceAll("\\s", "");
					if (san.length() > 0) {
						if (san.equals("*") || san.equals("1-0") || san.equals("0-1") || san.equals("1/2-1/2")) {
							parseResult(san);
						} else {
							try {
								IWritableGamePosition p = moves.getCurrentPosition()
										.getPositionAfter(Move.getMoveFromSan(san, moves.getCurrentPosition()));
								moves.addPositionAndGoTo(p);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else {
					token.append(c);
				}
				i++;
			}
			line = reader.readLine();
		}
		return this.game;
	}

	private void parseHeader() throws IOException {
		// TODO : écrire une fonction qui fait quelque chose.
		while (line.startsWith("[")) {
			line = reader.readLine();
		}
	}

	private void parseResult(String result) {
		// TODO : implémenter la méthode
	}

	private void enterVariation() {
		moves.goToPreviousPosition();
	}

	private void exitVariation() {
		moves.goToBeginningOfLine();
		moves.goToPreviousPosition();
		moves.goToNextPosition();
	}

	/**
	 * Supprime de la ligne tout ce que l'on ne veut pas pour l'instant. Est
	 * appelée à disparaitre si le programme devient complet.
	 */
	private void cleanLine() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		// supprime commentaires et numéros de coup;
		while (i < line.length()) {
			char c = line.charAt(i);
			// Commentaires
			if (c == '{') {
				commentLevel++;
			} else if (c == '}') {
				commentLevel--;
			} else if (commentLevel == 0) {
				// ignore les commentaires
				if (c == '.') {
					// supprime numéros de coups
					while (sb.length() > 0 && Character.isDigit(sb.charAt(sb.length() - 1))) {
						sb.deleteCharAt(sb.length() - 1);
					}
				} else {
					sb.append(c);
				}
			}
			i++;
		}
		// supprime NAG
		int indexNag = sb.indexOf("$");
		while (indexNag >= 0) {
			int end = indexNag + 1;
			while (Character.isDigit(sb.charAt(end))) {
				end++;
			}
			sb.delete(indexNag, end);
			indexNag = sb.indexOf("$");
		}

		// ajout d'un espace à la fin, nécessaire pour que le dernier coup soit
		// correctement lu.
		sb.append(" ");
		line = sb.toString();
		line = line.replace("(", " ( ");
		line = line.replace(")", " ) ");
	}
}
