package chessfx.core.game;

import java.util.Hashtable;

import chessfx.core.board.IReadableGamePosition;
import chessfx.core.board.IWritableGamePosition;

public class GameMoves implements IGameMoves{
	
	private int nextAvailableId = 0;
	private IWritableGamePosition currentPosition;
	private Hashtable<Integer, IWritableGamePosition> moveTable = new Hashtable<>();
	
	public GameMoves(IWritableGamePosition firstPosition) {
		firstPosition.setId(nextAvailableId);
		nextAvailableId ++;
		this.currentPosition = firstPosition;
		this.moveTable.put(firstPosition.getId(), firstPosition);
	}
	
	@Override
	public void goToNextPosition() {
		if (!this.getCurrentPosition().isLastPositionOfTheLine())
			this.setCurrentPosition(this.getCurrentPosition().getNextPosition().getId());
	}
	
	@Override
	public void goToPreviousPosition() {
		if(!this.getCurrentPosition().isFirstPositionOfTheGame())
			this.setCurrentPosition(this.getCurrentPosition().getPreviousPosition().getId());
	}
	
	@Override
	public void goToBeginningOfLine() {
		while (!this.currentPosition.isFirstPositionOfTheGame() || !this.currentPosition.isFirstPositionOfTheLine()){
			this.goToPreviousPosition();
		}
	}
	
	@Override
	public IReadableGamePosition getCurrentPosition() {
		return this.currentPosition;
	}
	
	@Override
	public void goToFirstPosition() {
		while(!this.getCurrentPosition().isFirstPositionOfTheGame()){
			this.goToPreviousPosition();
		}
	}
	
	@Override
	public void goToEndOfLine() {
		while(!this.getCurrentPosition().isLastPositionOfTheLine()){
			this.goToNextPosition();
		}
	}
	
	@Override
	public void setCurrentPosition(int id) {
		this.currentPosition = this.moveTable.get(id);
	}
	
	@Override
	public void addPositionAndGoTo(IWritableGamePosition p) {
		if(!this.getCurrentPosition().isLastPositionOfTheLine() && this.getCurrentPosition().getNextPosition().equals(p)){
			this.goToNextPosition();
			return;
		}else{
			for (IReadableGamePosition subline : this.getCurrentPosition().getSublines()) {
				if(subline.equals(p)){
					this.setCurrentPosition(subline.getId());
					return;
				}
			}
		}
		p.setId(nextAvailableId);
		nextAvailableId ++;
		IWritableGamePosition cWritePos = this.moveTable.get(this.getCurrentPosition().getId());
		if (cWritePos.isLastPositionOfTheLine()){
			cWritePos.setNextPosition(p);
		}else{
			p.setIsFirstOfTheLine(true);
			cWritePos.addSubline(p);
		}
		this.moveTable.put(p.getId(),p);
		this.setCurrentPosition(p.getId());
	}

}
