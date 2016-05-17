package chessfx.core.game;

public class Game implements IGame {

	private IGameMoves gameMoves;

	public Game(IGameMoves gameMoves) {
		this.gameMoves = gameMoves;
	}

	@Override
	public IGameMoves getMoves() {
		return gameMoves;
	}
}
