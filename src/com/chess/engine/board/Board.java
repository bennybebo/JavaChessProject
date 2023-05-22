package com.chess.engine.board;

import java.util.Map;
import com.chess.engine.pieces.*;
import com.chess.engine.player.*;
import com.chess.engine.Alliance;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Board {
	
	private final List<Tile> gameBoard; //The board is represented as an ArrayList of Tiles
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final Pawn enPassantPawn;	//Each board tracks whether there is a pawn who PawnJumped last move
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;

	private Board(final Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		final Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
	    	final String tileText = this.gameBoard.get(i).toString();
	        builder.append(String.format("%3s", tileText));
	        if ((i + 1) % 8 == 0) {
	            builder.append("\n");
	        }
	    }
	    return builder.toString();
	}
	
	
	public Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	
	public Collection<Piece> getWhitePieces() {
		return this.whitePieces;
	}
	
	public Collection<Piece> getBlackPieces() {
		return this.blackPieces;
	}

	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	public Piece getPiece(final int coordinate) {
		return this.getTile(coordinate).getPiece();
	}
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> activePieces) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final Piece piece : activePieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return legalMoves;
	}
	
	//Returns all legal moves for both the white and black player in one list
	public Collection<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        
        return Collections.unmodifiableList(allLegalMoves);
    }

	//Finds all pieces a player has remaining the board
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();
		
		for (final Tile tile : gameBoard) {
			if (tile.isTileOccupied() && tile.getPiece().getPieceAlliance() == alliance) {
				activePieces.add(tile.getPiece());
			}
		}
		
		return Collections.unmodifiableList(activePieces);
	}
	
	private static List<Tile> createGameBoard(final Builder builder) {
		final List<Tile> tiles = new ArrayList<>();
		for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
			tiles.add(Tile.createTile(i, builder.boardConfiguration.get(i)));
		}
		
		return Collections.unmodifiableList(tiles);
	}
	
	public static Board createInitialBoard() {
		final Builder builder = new Builder();
		//Sets initial pieces for black player
		builder.setPiece(new Rook(0, Alliance.BLACK));
		builder.setPiece(new Knight(1, Alliance.BLACK));
		builder.setPiece(new Bishop(2, Alliance.BLACK));
		builder.setPiece(new Queen(3, Alliance.BLACK));
		builder.setPiece(new King(4, Alliance.BLACK));
		builder.setPiece(new Bishop(5, Alliance.BLACK));
		builder.setPiece(new Knight(6, Alliance.BLACK));
		builder.setPiece(new Rook(7, Alliance.BLACK));
		//Sets initial pawns for black player
		for (int i = 8; i < 16; i++) {
			builder.setPiece(new Pawn(i, Alliance.BLACK));
		}
			
		//Sets initial pawns for white player
		for (int i = 48; i < 56; i++) {
			builder.setPiece(new Pawn(i, Alliance.WHITE));
		}
		//Sets initial pieces for white player
		builder.setPiece(new Rook(56, Alliance.WHITE));
		builder.setPiece(new Knight(57, Alliance.WHITE));
		builder.setPiece(new Bishop(58, Alliance.WHITE));
		builder.setPiece(new Queen(59, Alliance.WHITE));
		builder.setPiece(new King(60, Alliance.WHITE));
		builder.setPiece(new Bishop(61, Alliance.WHITE));
		builder.setPiece(new Knight(62, Alliance.WHITE));
		builder.setPiece(new Rook(63, Alliance.WHITE));
		//Sets white player to move first
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}
	
	//A builder class is used to create any possible board
	public static class Builder {
		
		Map<Integer, Piece> boardConfiguration;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder() {
			this.boardConfiguration = new HashMap<>();
		}
		
		//Sets a piece on the board
		public Builder setPiece(final Piece piece) {
			this.boardConfiguration.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		
		//Sets what player will make the next move
		public Builder setMoveMaker(final Alliance newMoveMaker) {
			this.nextMoveMaker = newMoveMaker;
			return this;
		}
		
		//If a pawn has just made a pawnJumpMove, set that pawn as an enPassantPawn
		public Builder setEnPassantPawn (Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			return this;
		}
		
		//Creates board
		public Board build() {
			return new Board(this);
		}
	}

}
