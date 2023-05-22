package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.*;
import com.chess.engine.board.Move.*;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTORS = { 7, 8, 9, 16 };
	
	public Pawn(final int pieceCoordinate, final Alliance pieceAlliance) {
		super(pieceCoordinate, pieceAlliance, PieceType.PAWN, true);
	}
	
	public Pawn(final int pieceCoordinate, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(pieceCoordinate, pieceAlliance, PieceType.PAWN, isFirstMove);
	}
	
	public Piece getPromotionPiece() {
		return new Queen(this.pieceCoordinate, this.pieceAlliance);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
		List<Move> legalMoves = new ArrayList<>();
		int destinationCoordinate;
		
		for(final int currentCandidate : CANDIDATE_MOVE_VECTORS) {
			destinationCoordinate = this.pieceCoordinate + (this.pieceAlliance.getDirection() * currentCandidate);
			
			if (!BoardUtilities.isValidBoardCoordinate(destinationCoordinate)) {
				continue;
			}
			
			final Tile destinationTile = board.getTile(destinationCoordinate);
			//Valid move for value of 7
			if (currentCandidate == 7 && !(isMoveException(this.pieceCoordinate, currentCandidate, this.pieceAlliance))) {
				if (board.getPiece(destinationCoordinate) != null) {
					if (this.pieceAlliance != destinationTile.getPiece().pieceAlliance) {
						if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
							legalMoves.add(new PawnPromotionMove(new PawnCapturingMove(board, this, destinationCoordinate, destinationTile.getPiece())));
						}
						else {
							legalMoves.add(new PawnCapturingMove(board, this, destinationCoordinate, destinationTile.getPiece()));
						}
					}
				}
				else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() == 
						(this.pieceCoordinate + (this.pieceAlliance.getOppositeDirection()))) {
						if (this.pieceAlliance != board.getEnPassantPawn().getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantMove(board, this, destinationCoordinate, board.getEnPassantPawn()));
						}
				}
			}
			//Valid move for value of 8
			else if (currentCandidate == 8 && !destinationTile.isTileOccupied()) {
				if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
					legalMoves.add(new PawnPromotionMove(new PawnMove(board, this, destinationCoordinate)));
				}
				else {
					legalMoves.add(new PawnMove(board, this, destinationCoordinate));
				}
			}
			//Valid move for value of 9
			else if (currentCandidate == 9 && !(isMoveException(this.pieceCoordinate, currentCandidate, this.pieceAlliance))) {
				if (board.getPiece(destinationCoordinate) != null) {
					if (this.pieceAlliance != destinationTile.getPiece().pieceAlliance) {
						if (this.pieceAlliance.isPawnPromotionSquare(destinationCoordinate)) {
							legalMoves.add(new PawnPromotionMove(new PawnCapturingMove(board, this, destinationCoordinate, destinationTile.getPiece())));
						}
						else {
							legalMoves.add(new PawnCapturingMove(board, this, destinationCoordinate, destinationTile.getPiece()));
						}
					}
				}
				//EnPassant
				else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() == 
						(this.pieceCoordinate - (this.pieceAlliance.getOppositeDirection()))) {
						if (this.pieceAlliance != board.getEnPassantPawn().getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantMove(board, this, destinationCoordinate, board.getEnPassantPawn()));
						}
				}
			}
			//Valid move for value of 16
			else if (currentCandidate == 16 && this.isFirstMove()) {
				final int tileBehind = destinationCoordinate - 8 * this.pieceAlliance.getDirection();
				if (!(destinationTile.isTileOccupied() || board.getTile(tileBehind).isTileOccupied())) {
					legalMoves.add(new PawnJumpMove(board, this, destinationCoordinate));
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	private boolean isMoveException(final int currentCoordinate, final int candidateOffset, final Alliance alliance) {
		return isFirstColumnException(currentCoordinate, candidateOffset, alliance) || isEigthColumnException(currentCoordinate, candidateOffset, alliance);
	}
	
	private static boolean isFirstColumnException(final int currentCoordinate, final int candidateOffset, final Alliance alliance) {
		if (currentCoordinate % 8 == 0) {
			if ((candidateOffset == 7 && alliance.isBlack()) || (candidateOffset == 9 && alliance.isWhite())) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isEigthColumnException(final int currentCoordinate, final int candidateOffset, final Alliance alliance) {
		if ((currentCoordinate + 1) % 8 == 0 && ((candidateOffset == 7 && alliance.isWhite()) 
				|| (candidateOffset == 9 && alliance.isBlack()))) {
			return true;
		}
		return false;
	}
	
	@Override
	public Piece movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinates(), move.getMovedPiece().getPieceAlliance(), false);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

}
