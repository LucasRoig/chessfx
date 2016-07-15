package chessfx.core.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chessfx.core.InvalidPieceException;
import chessfx.core.board.IReadableGamePosition;
import chessfx.core.board.IWritableGamePosition;

public class Game implements IGame, Serializable {

	private IGameMoves gameMoves;
	public Game(IGameMoves gameMoves) {
		this.gameMoves = gameMoves;
	}

	@Override
	public IGameMoves getMoves() {
		return gameMoves;
	}
	
	/**
	 * Rajoute les coups de g aux coups de la partie actuelle.
	 * @throws InvalidPieceException 
	 */
	@Override
	public void merge(IGame g) throws Exception {
		IGameMoves gMoves = g.getMoves();
		gMoves.goToFirstPosition();
		this.gameMoves.goToFirstPosition();
		this.doMerge(gMoves);
	}
	
	private void doMerge(IGameMoves gMoves) throws Exception{
		//TODO : Opti ?
		if(gMoves.getCurrentPosition().isLastPositionOfTheLine()){
			return;
		}
		else{
			List<IReadableGamePosition> nextPositions = new ArrayList<>();
			nextPositions.addAll(gMoves.getCurrentPosition().getSublines()); //positions suivantes dans la partie à fusionner
			nextPositions.add(gMoves.getCurrentPosition().getNextPosition());
			nextPositions.removeIf(p -> p == null);
			List<IReadableGamePosition> nextMainPosition = new ArrayList<>();
			nextMainPosition.addAll(this.gameMoves.getCurrentPosition().getSublines());//positions suivantes dans la partie principale
			nextMainPosition.add(this.gameMoves.getCurrentPosition().getNextPosition());
			nextMainPosition.removeIf(p -> p == null);
			int saveMainGameId = this.gameMoves.getCurrentPosition().getId();
			int saveMergeGameId = gMoves.getCurrentPosition().getId();
			for(IReadableGamePosition position : nextPositions){
				int i = 0;
				IReadableGamePosition trouve = null;
				while(trouve == null && i < nextMainPosition.size()){
					IReadableGamePosition t = nextMainPosition.get(i);
					System.out.println(position == null);
					boolean b = t.equals(position);
					if (nextMainPosition.get(i).equals(position)){
						trouve = nextMainPosition.get(i);
					}
					i++;
				}
				if(trouve != null){
					//la position est déjà présente dans la partie principale
					this.gameMoves.setCurrentPosition(trouve.getId());
					gMoves.setCurrentPosition(position.getId());
					this.doMerge(gMoves);
					this.gameMoves.setCurrentPosition(saveMainGameId);
					gMoves.setCurrentPosition(saveMergeGameId);
				}else{
					//La position n'a pas été trouvée
					IWritableGamePosition newPosition = this.gameMoves.getCurrentPosition().getPositionAfter(position.getLastMove());
					this.gameMoves.addPositionAndGoTo(newPosition);
					gMoves.setCurrentPosition(position.getId());
					doMerge(gMoves);
					this.gameMoves.setCurrentPosition(saveMainGameId);
					gMoves.setCurrentPosition(saveMergeGameId);
				}
			}
		}
	}
}
