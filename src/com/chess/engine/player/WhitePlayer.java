package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
		super(board, whiteLegalMoves, blackLegalMoves);
	}
	
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}
	
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves,
			final Collection<Move> opponentLegalMoves) {
		final List<Move> castlingMoves = new ArrayList<>();
		
		//If the king has not moved and is not in check
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			//King side castling
			//If there is no piece in between the king and the king side rook
			if (!(this.board.getTile(61).isTileOccupied() || this.board.getTile(62).isTileOccupied())) {
				final Piece kingSideRook = this.board.getTile(63).getPiece();
				//If the piece on the king side rook tile is a rook and has not moved yet
				if(kingSideRook.isRook() &&kingSideRook.isFirstMove()) {
					//If the king is not castling through an attacked tile
					if (calculateAttacksOnTile(61, opponentLegalMoves).isEmpty() &&
							calculateAttacksOnTile(62, opponentLegalMoves).isEmpty()) {
						castlingMoves.add(new Move.KingSideCastlingMove(board, playerKing, 62, (Rook)kingSideRook, 61));
					}
				}
			}	
			//Queen side castling
			//If there is no piece in between the king and the queen side rook
			if (!(this.board.getTile(57).isTileOccupied() || 
					this.board.getTile(58).isTileOccupied() || this.board.getTile(59).isTileOccupied())) {
				final Piece queenSideRook = this.board.getTile(56).getPiece();
				//If the piece on the queen side rook tile is a rook and has not moved yet
				if(queenSideRook.isRook() && queenSideRook.isFirstMove()) {
					//If the king is not castling through an attacked tile
					if (calculateAttacksOnTile(57, opponentLegalMoves).isEmpty() &&
							calculateAttacksOnTile(58, opponentLegalMoves).isEmpty() &&
							calculateAttacksOnTile (59, opponentLegalMoves).isEmpty()) {
						castlingMoves.add(new Move.QueenSideCastlingMove(board, playerKing, 58, (Rook)queenSideRook, 59));
					}
				}
			}
		}	
		return Collections.unmodifiableList(castlingMoves);
	}
	
}
