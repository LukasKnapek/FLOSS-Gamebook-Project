package mabufudyne.gbdesigner.core;

import java.util.ArrayList;
import java.util.UUID;

public class StoryPieceManager {
	
	private static StoryPiece activeStoryPiece;
	private static ArrayList<StoryPiece> allStoryPieces = new ArrayList<StoryPiece>();
	
	public static StoryPiece getActiveStoryPiece() {
		return activeStoryPiece;
	}

	public static void setActiveStoryPiece(StoryPiece sp) {
		activeStoryPiece = sp;
	}

	public static ArrayList<StoryPiece> getAllStoryPieces() {
		return allStoryPieces;
	}

	public static void setAllStoryPieces(ArrayList<StoryPiece> allStoryPieces) {
		StoryPieceManager.allStoryPieces = allStoryPieces;
	}

	public static StoryPiece getStoryPieceByID(UUID id) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getID() == id) return sp;
		}
		return null;
	}
	
	public static StoryPiece addNewStoryPiece() {
		StoryPiece sp = new StoryPiece();
		allStoryPieces.add(sp);
		return sp;
	}
	
	public static void removeStoryPiece(StoryPiece sp) {
		chooseNewActiveStoryPiece(allStoryPieces.indexOf(sp));
		allStoryPieces.remove(sp);
	}
	/** Chooses a new active StoryPiece based on the position of the old one to be deleted */
	private static void chooseNewActiveStoryPiece(int deletedSPPosition) {
		// If we delete the last StoryPiece, we need to set activeStoryPiece to null
		if (allStoryPieces.size() == 1) activeStoryPiece = null;
		// If the deleted StoryPiece is the first one, we will make the next one active
		else if (deletedSPPosition == 0) activeStoryPiece = allStoryPieces.get(1);
		// Otherwise just make active the StoryPiece before the deleted one
		else activeStoryPiece = allStoryPieces.get(deletedSPPosition-1);
	}
	
	public static void saveChanges(String title, String story) {
		activeStoryPiece.setTitle(title);
		activeStoryPiece.setStory(story);
	}
	
	public void addChoice(StoryPiece sp) {
		activeStoryPiece.addChoice(sp);
	}
}
