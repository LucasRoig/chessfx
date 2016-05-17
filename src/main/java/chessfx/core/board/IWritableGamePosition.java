package chessfx.core.board;

public interface IWritableGamePosition extends IReadableGamePosition {

	public void setId(int id);

	public void setNextPosition(IWritableGamePosition p);

	public void setPreviousPosition(IWritableGamePosition p);

	public void addSubline(IWritableGamePosition p);

	public void removeSubline(IWritableGamePosition p);

	public void setIsFirstOfTheLine(boolean b);
}
