package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

import java.util.List;

public abstract class Piece {
	
	protected final int pieceCoordinate;
	protected final Alliance pieceAlliance;
	protected final boolean firstMove;
	protected final PieceType pieceType;
	
	Piece(final int pieceCoordinate, final Alliance pieceAlliance, final PieceType pieceType, 
			final boolean isFirstMove) {
		this.pieceCoordinate = pieceCoordinate;
		this.pieceAlliance = pieceAlliance;
		this.pieceType = pieceType;
		this.firstMove = isFirstMove;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) { //If 'other' is the same object that called the method
			return true; 
		}
		if (!(other instanceof Piece)) {	//If 'other' is not a Piece
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return pieceCoordinate == otherPiece.getPiecePosition()
				&& pieceAlliance == otherPiece.getPieceAlliance()
				&& firstMove == otherPiece.isFirstMove()
				&& pieceType == otherPiece.getPieceType();				
	}
	
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public boolean isFirstMove() {
		return this.firstMove;
	}
	
	public int getPiecePosition() {
		return this.pieceCoordinate;
	}
	
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	//isRook and isKing are overwritten to return true in applicable cases
	public boolean isRook() {
		return false;
	}
	
	public boolean isKing() {
		return false;
	}
	
	public abstract List<Move> calculateLegalMoves(final Board board);
	public abstract Piece movePiece(Move move);
	
	public enum PieceType {
				
		PAWN("P", 1),
        KNIGHT("N", 3),
        BISHOP("B", 3),
        ROOK("R", 3),
        QUEEN("Q", 9),
        KING("K", 0);
		
		private final String pieceName;
		private final int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue) {
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}

        int getPieceValue() {
			return this.pieceValue;
		}

		@Override
        public String toString() {
            return this.pieceName;
        }

	}
	
}
