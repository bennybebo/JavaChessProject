package com.chess.engine.player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

public abstract class Player {
	
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private  final boolean isInCheck;
	
	Player(final Board board, final Collection<Move> legalMoves, 
			final Collection<Move> opponentMoves) {
		this.board = board;
		this.playerKing = establishKing();
		this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
		legalMoves.addAll(calculateKingCastles(legalMoves, opponentMoves));
		this.legalMoves = Collections.unmodifiableCollection(legalMoves);
	}

	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("A king is required to create a valid board");
	}
	
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}
	
	//Calculates opponents attacks on a given tile. Used for determining check and castling rights
	protected static Collection<Move> calculateAttacksOnTile(final int piecePosition, final Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		
		for (final Move move : moves) {
			if (move.getDestinationCoordinates() == piecePosition) {
				attackMoves.add(move);
			}
		}		
		return Collections.unmodifiableList(attackMoves);
	}
	
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}

	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;
	}
	
	public MoveTransition makeMove(final Move move) {
		
		if (!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board newBoard = move.execute();
		final Collection<Move> opponentsAttacksOnKing = Player.calculateAttacksOnTile(newBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				newBoard.currentPlayer().getLegalMoves());
		//If player is in check
		if (!opponentsAttacksOnKing.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}	
		return new MoveTransition(newBoard, move, MoveStatus.DONE);
	}

	//Method determines if a player is able to make a legal move that does not leave them in check
	protected boolean hasEscapeMoves() {	
		for (final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves);
}
