package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

		@SuppressWarnings("unused")
		private final Board newBoard;
		@SuppressWarnings("unused")
		private final Move move;
		private final MoveStatus moveStatus;
		
		public MoveTransition(final Board newBoard, final Move move, final MoveStatus moveStatus) {
			this.newBoard = newBoard;
			this.move = move;
			this.moveStatus = moveStatus;
		}

		public MoveStatus getMoveStatus() {
			return this.moveStatus;
		}

		public Board getBoard() {
			return newBoard;
		}
		
}
