package com.chess.engine.pieces;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Move.*;

public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTORS = { -9, -8, -7,  -1, 1, 7, 8, 9 };
	
	public Queen(final int pieceCoordinate, final Alliance pieceAlliance) {
		super(pieceCoordinate, pieceAlliance, PieceType.QUEEN, true);
	}
	
	public Queen(final int pieceCoordinate, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(pieceCoordinate, pieceAlliance, PieceType.QUEEN, isFirstMove);
	}
	
	@Override
	public List<Move> calculateLegalMoves(final Board board) {
		List<Move> legalMoves = new ArrayList<>();
		int currentCoordinate = this.pieceCoordinate;
		int destinationCoordinate;
		
		//For each move vector, iterate in that direction adding all possible moves until reaching an exception
		for (final int currentCandidate : CANDIDATE_MOVE_VECTORS) {
			destinationCoordinate = currentCoordinate;
			
			while (BoardUtilities.isValidBoardCoordinate(destinationCoordinate)) {
				if (isMoveException(destinationCoordinate, currentCandidate)) {
					break;
				}
				
				destinationCoordinate += currentCandidate;
				if (BoardUtilities.isValidBoardCoordinate(destinationCoordinate)) {
					final Tile destinationTile = board.getTile(destinationCoordinate);
					//If tile is empty
					if (!destinationTile.isTileOccupied()) {
						legalMoves.add(new PositioningMove(board, this, destinationCoordinate));
					}
					//If tile contains a piece of same alliance
					else if (this.pieceAlliance == destinationTile.getPiece().pieceAlliance) {
						break;
					}
					//If tile contains opposing alliance piece, add move to list
					else if (this.pieceAlliance != destinationTile.getPiece().getPieceAlliance()) {
						legalMoves.add(new CapturingMove(board, this, destinationCoordinate, destinationTile.getPiece())); 
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);		
	}
	
	@Override
	public Piece movePiece(Move move) {
		return new Queen(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance());
	}

	//Move exceptions can occur when the queen is on the 1st or eighth column
	private static boolean isMoveException (final int currentCoordinate, final int candidateOffset) {
		return isFirstColumnException(currentCoordinate, candidateOffset) || isEigthColumnException(currentCoordinate, candidateOffset);
	}
	
	private static boolean isFirstColumnException(final int pieceCoordinate, final int candidateOffset) {
		if (pieceCoordinate % 8 == 0 && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == -7)) {
			return true;
		}
		return false;
	}
	
	private static boolean isEigthColumnException(final int pieceCoordinate, final int candidateOffset) {
		if ((pieceCoordinate - 7) % 8 == 0 && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}
	
}
