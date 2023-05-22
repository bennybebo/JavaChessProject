package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
	WHITE
	{
		@Override
		public int getDirection() {
			return -1;
		}
		
		@Override
		public int getOppositeDirection() {
			return 1;
		}

		@Override
		public boolean isWhite() {
			return true;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return whitePlayer;
		}
		
		@Override
		public boolean isPawnPromotionSquare(final int position) {
			if (position < 8) {
				return true;
			}
			return false;
		}
	},	
	BLACK
	{
		@Override
		public int getDirection() {
			return 1;
		}
		
		@Override
		public int getOppositeDirection() {
			return -1;
		}

		@Override
		public boolean isWhite() {
			return false;
		}
		
		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public boolean isPawnPromotionSquare(final int position) {
			if (position > 55) {
				return true;
			}
			return false;
		}
	};
	
	public abstract int getDirection();
	public abstract boolean isWhite();
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
	public abstract boolean isBlack();
	public abstract int getOppositeDirection();
	public abstract boolean isPawnPromotionSquare(int position);
}
