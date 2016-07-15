package chessfx.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chessfx.core.InvalidPieceException;
import chessfx.core.Move;
import chessfx.core.Piece;
import chessfx.core.Square;
import chessfx.core.board.IReadableGamePosition;
import chessfx.core.game.IGame;
import chessfx.core.game.IGameMoves;
import chessfx.ui.controllers.PromotionHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Classe servant a manipuler une partie. Fourni la propriété currentPosition.
 * 
 * @author Lucas
 *
 */
public class GameSelectionModel {
	private ObjectProperty<IReadableGamePosition> currentPosition = new SimpleObjectProperty<>();
	private IGame game;
	private IGameMoves gameMoves;
	private PromotionHandler promotionHandler;
	private List<ChangeListener> listeners = new ArrayList<>();
	
	public GameSelectionModel(IGame iGame) {
		this.game = iGame;
		this.gameMoves = this.game.getMoves();
		this.gameMoves.goToFirstPosition();
		this.currentPosition.set(this.gameMoves.getCurrentPosition());
	}

	public void addListener(ChangeListener c){
		this.listeners.add(c);
	}
	public void setGame(IGame g) {
		this.game = g;
		this.gameMoves = g.getMoves();
		this.gameMoves.goToFirstPosition();
		this.currentPosition.set(gameMoves.getCurrentPosition());
	}

	public void change(){
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener changeListener : listeners) {
			changeListener.stateChanged(e);
		}
	}
	public void setPromotionHandler(PromotionHandler promotionHandler) {
		this.promotionHandler = promotionHandler;
	}

	public ObjectProperty<IReadableGamePosition> getCurrentPosition() {
		return currentPosition;
	}

	public IGameMoves getMoves() {
		return this.gameMoves;
	}

	public void goToNextMove() {
		this.gameMoves.goToNextPosition();
		this.getCurrentPosition().set(this.gameMoves.getCurrentPosition());
	}

	public void goToPreviousMove() {
		this.gameMoves.goToPreviousPosition();
		this.getCurrentPosition().set(this.gameMoves.getCurrentPosition());
	}

	public void goToPosition(int index) {
		this.gameMoves.setCurrentPosition(index);
		this.getCurrentPosition().set(this.gameMoves.getCurrentPosition());
	}

	public Piece getPieceAt(Square s) {
		return currentPosition.get().getPieceAt(s);
	}

	public List<Square> getLegalTargets(Square from) {
		return this.getCurrentPosition().get().getValidTargets(from);
	}

	public void userMakesLegalMove(Square from, Square to) {
		Move m = new Move(from, to, this.getPieceAt(from));
		if (m.isPromotion()) {
			m.setPromotionPiece(this.promotionHandler.askPromotion());
		}
		try {
			this.gameMoves.addPositionAndGoTo(this.getCurrentPosition().get().getPositionAfter(m));
		} catch (InvalidPieceException e) {
			e.printStackTrace();
		}
		this.getCurrentPosition().set(this.gameMoves.getCurrentPosition());
	}
}
