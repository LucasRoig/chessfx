package chessfx.core.game;

import java.io.Serializable;

import chessfx.core.ChessColors;

public class Opening extends Game implements IOpening, Serializable {
	private ChessColors color;
	private String name;
	private int id;

	public Opening(IGameMoves gameMoves) {
		super(gameMoves);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IGameMoves getMoves() {
		return super.getMoves();
	}

	@Override
	public ChessColors getColor() {
		return this.color;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setColor(ChessColors color) {
		this.color = color;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
