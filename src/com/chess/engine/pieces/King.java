package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Move.*;

public class King extends Piece {
	
	private final static int[] CANDIDATE_MOVE_VECTORS = { -9, -8, -7, -1, 1, 7, 8, 9 };
	
	public King(final int pieceCoordinate, final Alliance pieceAlliance) {
		super(pieceCoordinate, pieceAlliance, PieceType.KING, true);
	}
	
	public King(final int pieceCoordinate, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(pieceCoordinate, pieceAlliance, PieceType.KING, isFirstMove);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move> legalMoves = new ArrayList<>();
		int destinationCoordinate;
		
		for(final int currentCandidate : CANDIDATE_MOVE_VECTORS) {
			//If there is exception to rules, do not check legality
			if(isMoveException(this.pieceCoordinate, currentCandidate)) {
				continue;
			}
			destinationCoordinate = this.pieceCoordinate + currentCandidate;
			//If kings move stays within boundaries of the board
			if ((BoardUtilities.isValidBoardCoordinate(destinationCoordinate))) {
				final Tile destinationTile = board.getTile(destinationCoordinate);
				//If tile is empty
				if (!destinationTile.isTileOccupied()) {
					legalMoves.add(new PositioningMove(board, this, destinationCoordinate));
				}
				//If tile contains opposing alliance piece, add move to list
				else if (this.pieceAlliance != destinationTile.getPiece().getPieceAlliance()) {
					legalMoves.add(new CapturingMove(board, this, destinationCoordinate, destinationTile.getPiece()));
				}
			}
		}	
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public boolean isKing() {
		return true;
	}
	
	//Create a new King with a isFirstMove value of false
	@Override
	public Piece movePiece(Move move) {
		return new King(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance(), false);
	}
	
	//King move exceptions occur when trying to move too far left or right of the board
	private static boolean isMoveException (final int currentCoordinate, final int candidateOffset) {
		return isFirstColumnException(currentCoordinate, candidateOffset) || isEigthColumnException(currentCoordinate, candidateOffset);
	}
			
	private static boolean isFirstColumnException(final int pieceCoordinate, final int candidateOffset) {
		return pieceCoordinate % 8 == 0 && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
	}
			
	private static boolean isEigthColumnException(final int pieceCoordinate, final int candidateOffset) {
		return (pieceCoordinate - 7)% 8 == 0 && (candidateOffset == 1 || candidateOffset == 9 || candidateOffset == -7);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
}
