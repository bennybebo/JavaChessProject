package com.chess.engine.board;

import java.util.Map;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//Class to store common constants and helper functions
public class BoardUtilities {
	
	public static final int NUM_TILES = 64;
	public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
	public static final Map<String, Integer> AN_TO_COORDINATE = initializeNotationMap();
	
	public static boolean isValidBoardCoordinate(final int coordinate) {
		return coordinate > -1 && coordinate < 64;
	}
	
	private static Map<String, Integer> initializeNotationMap() {
		final Map<String, Integer> notationMap = new HashMap<>();
		
		for (int i = 0; i < NUM_TILES; i++) {
			notationMap.put(ALGEBRAIC_NOTATION.get(i), i);
		}
		
		return Collections.unmodifiableMap(notationMap);
	}

	private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }
	
	public static int getCoordinateAtPosition(final String position) {
		return AN_TO_COORDINATE.get(position);
	}
	
	public static String getPositionAtCoordinate(final int coordinate) {
		return ALGEBRAIC_NOTATION.get(coordinate);
	}
}
