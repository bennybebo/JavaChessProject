package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

public abstract class Tile {
	
	protected final int tileCoordinate;	 //Tile coordinates are stored as an integer 0-63
	
	private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmptyTiles();
	
	private static Map<Integer, EmptyTile> createEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<Integer, EmptyTile>();
		
		for (int i = 0; i < 64; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}	
		return Collections.unmodifiableMap(emptyTileMap);
	}
	
	//Method creates an empty/occupied tile depending on if there is a piece on the tile
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
	}
	
	private Tile(int tileCoordiante) {
		this.tileCoordinate = tileCoordiante;
	}
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
	//An EmptyTile is a tile with no piece on it (represented as a null piece)
	public static final class EmptyTile extends Tile {
		
		private EmptyTile(final int tileCoordinate) {
			super(tileCoordinate);
		}
		
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		@Override
		public String toString() {
			return "-";
		}
		
		@Override
		public Piece getPiece() {
			return null;
		}
	}
	
	//An OccupiedTile is a tile with a piece on it
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOnTile;
		
		private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public boolean isTileOccupied() {
			return true;
		}
		
		@Override
		public String toString() {
			return !getPiece().getPieceAlliance().isWhite() ?
	                   getPiece().toString().toLowerCase() : getPiece().toString();
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
	}

}

