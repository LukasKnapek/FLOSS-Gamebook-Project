package mabufudyne.gbdesigner.core;

import java.util.ArrayList;

public class StoryPieceManager {
	
	private static ArrayList<StoryPiece> allStoryPieces = new ArrayList<StoryPiece>();
	
	public static StoryPiece addNewStoryPiece() {
		StoryPiece sp = new StoryPiece();
		allStoryPieces.add(sp);
		return sp;
	}
}
