package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.board.Board.Builder;

public abstract class Move {

	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate,
			final boolean isFirstMove) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = isFirstMove;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getDestinationCoordinates() == otherMove.getDestinationCoordinates() &&
				getMovedPiece() == otherMove.getMovedPiece() && 
				this.isFirstMove == otherMove.isFirstMove;
	}
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	public int getDestinationCoordinates() {
		return this.destinationCoordinate;
	}
	
	public int getCurrentCoordinate() {
		return this.getMovedPiece().getPiecePosition();
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Piece getCapturedPiece() {
		return null;
	}
	
	public boolean isAttack() {
		return false;
	}
	
	public boolean isCastlingMove() {
		return false;
	}
	
	//Method used to carry out a move and return the resulting board
	public Board execute() {
		final Builder builder = new Builder();
		//Sets all of players pieces in the same position except the moved piece
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
			if (!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		//Sets all of opponents pieces in the same position
		for (final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		//Sets the moved piece to its desired destination
		builder.setPiece(this.movedPiece.movePiece(this));
		//Sets the opponent to be the next move maker on the resulting board
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		
		return builder.build();
	}
	
	//A PositioningMove is a Move that does not capture a piece
	public static class PositioningMove extends Move {
		
		public PositioningMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		 @Override
	        public boolean equals(final Object other) {
	            return this == other || other instanceof PositioningMove && super.equals(other);
	        }
		
		@Override
		public String toString() {
			return this.movedPiece.getPieceType().toString() + BoardUtilities.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	//A capturing move is a move where a piece moves onto the tile of an opposing piece
	public static class CapturingMove extends Move {
		final Piece capturedPiece;
		
		public CapturingMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Piece capturedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.capturedPiece = capturedPiece;
		}
		
		@Override
		public boolean isAttack() {
			return true;
		}
		
		@Override
		public Piece getCapturedPiece() {
			return this.capturedPiece;
		}
		
		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof CapturingMove)) {
				return false;
			}
			
			final CapturingMove otherCaputuringMove = (CapturingMove) other;
			return super.equals(otherCaputuringMove) 
					&& getCapturedPiece().equals(otherCaputuringMove.getCapturedPiece());
		}
		
		@Override
		public String toString() {
			return this.movedPiece.getPieceType().toString() + "x" 
					+ BoardUtilities.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	public static final class PawnMove extends Move {
		
		public PawnMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnMove && super.equals(other);
		}
		@Override
		public String toString() {
			return BoardUtilities.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	public static class PawnCapturingMove extends CapturingMove {
		
		public PawnCapturingMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Piece capturedPiece) {
			super(board, movedPiece, destinationCoordinate, capturedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnCapturingMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return BoardUtilities.getPositionAtCoordinate(this.getCurrentCoordinate()).substring(0,  1) 
					+ "x" + BoardUtilities.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	//A PawnPromotionMove is when a Pawn moves onto a promotion Tile and must promote to a piece
	public static class PawnPromotionMove extends Move {
		
		final Move decoratedMove;
		final Pawn promotedPawn;
		
		public PawnPromotionMove(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinates());
			this.decoratedMove = decoratedMove;
			this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
		}
		
		@Override
		public Board execute() {
			
			final Board pawnMoveBoard = this.decoratedMove.execute();
			final Builder builder = new Builder();
			
			for (final Piece piece : pawnMoveBoard.currentPlayer().getActivePieces()) {
				if (!this.promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : pawnMoveBoard.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(pawnMoveBoard.currentPlayer().getAlliance());
			
			return builder.build();
		}
		
		@Override
		public boolean isAttack() {
			return this.decoratedMove.isAttack();
		}
		
		@Override
		public Piece getCapturedPiece() {
			return this.decoratedMove.getCapturedPiece();
		}
		
		@Override
		public String toString() {
			return BoardUtilities.getPositionAtCoordinate(destinationCoordinate) + "=Q";
		}
		
		@Override
		public boolean equals (Object other) {
			return this == other || other instanceof PawnPromotionMove && super.equals(other);
		}
	}
	
	//A PawnEnPassantMove is a pawn move that captures an EnPassant pawn
	public static final class PawnEnPassantMove extends PawnCapturingMove {
		
		public PawnEnPassantMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Piece capturedPiece) {
			super(board, movedPiece, destinationCoordinate, capturedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnEnPassantMove && super.equals(other);
		}
		
		@Override
		public Board execute() {
			final Board.Builder builder = new Builder();
			
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				if (!piece.equals(this.getCapturedPiece())) {
					builder.setPiece(piece);
				}
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			
			return builder.build();
		}
	}
	
	//A PawnJumpMove is a move where a pawn jumps two tiles forwards
	public static final class PawnJumpMove extends Move {
		
		public PawnJumpMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnJumpMove && super.equals(other);
		}
		
		@Override
		public Board execute() {
			final Board.Builder builder = new Board.Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			//When a pawn jumps, it becomes an EnPassantPawn on the resulting board
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			
			return builder.build();
		}
		
		@Override
		public String toString() {
			return BoardUtilities.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	public static abstract class CastlingMove extends Move {
		
		protected final Rook castleRook;
		final int castleRookDestination;
		
		public CastlingMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Rook castleRook,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookDestination = castleRookDestination;
		}
		
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		
		@Override
		public Board execute() {
			final Board.Builder builder = new Board.Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			
			return builder.build();
		}
		
		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof CastlingMove)) {
				return false;
			}
			final CastlingMove otherCastleMove = (CastlingMove) other;
			
			return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
		}
		
	}
	
	public static final class KingSideCastlingMove extends CastlingMove {
		
		public KingSideCastlingMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Rook castleRook,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "0-0";
		}
	}
	
	public static final class QueenSideCastlingMove extends CastlingMove {
		
		public QueenSideCastlingMove(final Board board, final Piece movedPiece, 
				final int destinationCoordinate, final Rook castleRook,
				final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "0-0-0";
		}
	}
	
	public static final class NullMove extends Move {
		
		public NullMove() {
			super(null, null, -1, false);
		}
		
		@Override
		public Board execute() {
			throw new RuntimeException("Cannot execute a null move");
		}
	}
	
	public static class MoveFactory {
		
		private MoveFactory() {
			throw new RuntimeException("Cannot instantiate object of type MoveFactory");
		}
		
		public static Move createMove (final Board board, final int currentCoordinate,
				final int destinationCoordinate) {
			
			for (final Move move : board.getAllLegalMoves()) {
				//if the move is valid return the move
				if (move.getCurrentCoordinate() == currentCoordinate && 
						move.getDestinationCoordinates() == destinationCoordinate) {
					return move;
				}
			}
			//If the move is not legal return a NULL_MOVE
			return NULL_MOVE;
		}
	}
	
}
