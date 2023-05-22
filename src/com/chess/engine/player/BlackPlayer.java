package com.chess.engine.player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, final Collection<Move> whiteLegalMoves, final Collection<Move> blackLegalMoves) {
		super(board, blackLegalMoves, whiteLegalMoves);
	}
	
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves,
			final Collection<Move> opponentLegalMoves) {
		final List<Move> castlingMoves = new ArrayList<>();
		
		//If the king has not moved and is not in check
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			//King side castling
			//If there is no piece in between the king and the king side rook
			if (!(this.board.getTile(5).isTileOccupied() || this.board.getTile(6).isTileOccupied())) {
				final Piece kingSideRook = this.board.getTile(7).getPiece();
				//If the piece on the king side rook tile is a rook and has not moved yet
				if(kingSideRook.isRook() && kingSideRook.isFirstMove()) {
					//If the king is not castling through an attacked tile
					if (calculateAttacksOnTile(5, opponentLegalMoves).isEmpty() &&
						calculateAttacksOnTile(6, opponentLegalMoves).isEmpty()) {
							castlingMoves.add(new Move.KingSideCastlingMove(board, playerKing, 6, (Rook)kingSideRook, 5));
					}
				}
			}			
			//Queen side castling
			//If there is no piece in between the king and the queen side rook
			if (!(this.board.getTile(1).isTileOccupied() || 
				this.board.getTile(2).isTileOccupied() || this.board.getTile(3).isTileOccupied())) {
					final Piece queenSideRook = this.board.getTile(0).getPiece();
					//If the piece on the queen side rook tile is a rook and has not moved yet
					if(queenSideRook.isRook() && queenSideRook.isFirstMove()) {
						//If the king is not castling through an attacked tile
						if (calculateAttacksOnTile(1, opponentLegalMoves).isEmpty() &&
							calculateAttacksOnTile(2, opponentLegalMoves).isEmpty() &&
							calculateAttacksOnTile (3, opponentLegalMoves).isEmpty()) {
								castlingMoves.add(new Move.QueenSideCastlingMove(board, playerKing, 2, (Rook)queenSideRook, 3));
					}
				}
			}
		}	
		return Collections.unmodifiableList(castlingMoves);
	}

}
