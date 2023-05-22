package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Move.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Knight extends Piece {
	
	private final static int[] CANDIDATE_MOVES = { -17, -15, -10, -6, 6, 10, 15, 17 };
	
	public Knight(final int pieceCoordinate, final Alliance pieceAlliance) {
		super(pieceCoordinate, pieceAlliance, PieceType.KNIGHT, true);
	}
	
	public Knight(final int pieceCoordinate, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(pieceCoordinate, pieceAlliance, PieceType.KNIGHT, isFirstMove);
	}
	
	@Override
	public List<Move> calculateLegalMoves(final Board board) {
		
		int destinationCoordinate;
		List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidate : CANDIDATE_MOVES) {
			//If there is exception to rules, do not check legality
			if(isMoveException(this.pieceCoordinate, currentCandidate)) {
				continue;
			}
			
			destinationCoordinate = this.pieceCoordinate + currentCandidate;
			//If knight move stays within boundaries of the board
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
	public Piece movePiece(Move move) {
		return new Knight(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
	}
	
	//Knight move exceptions occur when the knight is on one of the 4 outer files
	private static boolean isMoveException (final int pieceCoordinate, final int candidateOffset) {
		return isFirstColumnException(pieceCoordinate, candidateOffset) 
			|| isSecondColumnException(pieceCoordinate, candidateOffset) 
			|| isSeventhColumnException(pieceCoordinate, candidateOffset) 
			|| isEigthColumnException(pieceCoordinate, candidateOffset);
	}
	
	private static boolean isFirstColumnException(final int pieceCoordinate, final int candidateOffset) {
		if (pieceCoordinate % 8 == 0) {
			return candidateOffset == -17 || candidateOffset == -10 || candidateOffset == 6 || candidateOffset == 15;
		}
		return false;
	}
	
	private static boolean isSecondColumnException(final int pieceCoordinate, final int candidateOffset) {
		if ((pieceCoordinate - 1) % 8 == 0) {
			return candidateOffset == -10 || candidateOffset == 6;
		}
		return false;
	}
	
	private static boolean isSeventhColumnException(final int pieceCoordinate, final int candidateOffset) {
		if ((pieceCoordinate - 6) % 8 == 0) {
			return candidateOffset == -6 || candidateOffset == 10;
		}
		return false;
	}
	
	private static boolean isEigthColumnException(final int pieceCoordinate, final int candidateOffset) {
		if ((pieceCoordinate - 7) % 8 == 0) {
			return candidateOffset == -15 || candidateOffset == -6 || candidateOffset == 10 || candidateOffset == 17;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}
}
