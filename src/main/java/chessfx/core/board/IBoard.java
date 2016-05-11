package chessfx.core.board;

import java.util.List;

import chessfx.core.Piece;
import chessfx.core.InvalidPieceException;
import chessfx.core.Square;

public interface IBoard extends IReadableBoard{
	
	/**
	 * Place la piece p sur la case s
	 * @param s la case sur laquelle on veut placer une piece
	 * @param p la piece que l'on désire placer
	 * @throws InvalidPieceException
	 */
	public void setPieceAt(Square s, Piece p) throws InvalidPieceException;
	
	/**
	 * Retourne la liste des case atteignables par la piece située sur la case from
	 * Ne vérifié pas la légalité des coups sous-entendus
	 * @param from
	 * @return
	 */
	public List<Square> getTargets(Square from);
	
	/**
	 * Indique que la prise en passant est possible sur la case s
	 * @param s case sur laquelle la prise en passant est possible
	 */
	public void setEpSquare(Square s);
	

	/**
	 *Enleve toute pièce présente sur la case s
	 *@param s : case à vider
	 */
	public void removePieceAt(Square s);
	
	/**
	 * 
	 * @return true si le roi blanc est en échec
	 */
	public boolean isWhiteKingInCheck();
	
	/**
	 * 
	 * @return true si le roi noir est en échec
	 */
	public boolean isBlackKingInCheck();
	
	/**
	 * Retourne un nouveau IBoard dans le même état
	 * @return
	 */
	public IBoard copy();
	
	/**
	 * Retourne true si les cases E1,F1,G1 ne sont pas attaquées par les noirs
	 * @return
	 */
	public boolean canWhiteShortCastle();
	/**
	 * Retourne true si les cases E1,D1,C1 ne sont pas attaquées par les noirs
	 * @return
	 */
	public boolean canWhiteLongCastle();
	/**
	 * Retourne true si les cases E8,F8,G8 ne sont pas attaquées par les blancs
	 * @return
	 */
	public boolean canBlackShortCastle();
	/**
	 * Retourne true si les cases E8,D8,C8 ne sont pas attaquées par les blancs
	 * @return
	 */
	public boolean canBlackLongCastle();
}
