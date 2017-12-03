package mabufudyne.gbdesigner.core;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.UUID;

public class StoryPieceManager implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7140106431174459518L;
	private transient static StoryPieceManager instance = new StoryPieceManager();
	private StoryPiece activeStoryPiece;
	private ArrayList<StoryPiece> allStoryPieces = new ArrayList<StoryPiece>();
	
	public static StoryPieceManager getInstance() {
		return instance;
	}
	
	public static void replaceManager(StoryPieceManager manager) {
		instance = manager;
	}
	
	public StoryPiece getActiveStoryPiece() {
		return activeStoryPiece;
	}

	public void setActiveStoryPiece(StoryPiece sp) {
		activeStoryPiece = sp;
	}

	public ArrayList<StoryPiece> getAllStoryPieces() {
		return allStoryPieces;
	}

	public void setAllStoryPieces(ArrayList<StoryPiece> allStoryPieces) {
		this.allStoryPieces = allStoryPieces;
	}

	public StoryPiece getStoryPieceByID(UUID id) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getID() == id) return sp;
		}
		return null;
	}
	
	public StoryPiece addNewStoryPiece() {
		StoryPiece sp = new StoryPiece();
		allStoryPieces.add(sp);
		return sp;
	}
	
	public void removeStoryPiece(StoryPiece sp) {
		chooseNewActiveStoryPiece(allStoryPieces.indexOf(sp));
		allStoryPieces.remove(sp);
	}
	/** Chooses a new active StoryPiece based on the position of the old one to be deleted */
	private void chooseNewActiveStoryPiece(int deletedSPPosition) {
		// If we delete the last StoryPiece, we need to set activeStoryPiece to null
		if (allStoryPieces.size() == 1) activeStoryPiece = null;
		// If the deleted StoryPiece is the first one, we will make the next one active
		else if (deletedSPPosition == 0) activeStoryPiece = allStoryPieces.get(1);
		// Otherwise just make active the StoryPiece before the deleted one
		else activeStoryPiece = allStoryPieces.get(deletedSPPosition-1);
	}
	
	public void saveChanges(String title, String story) {
		activeStoryPiece.setTitle(title);
		activeStoryPiece.setStory(story);
	}
	
	public void addChoice(StoryPiece sp) {
		activeStoryPiece.addChoice(sp);
	}

	public void removeStoryPieceLinks(StoryPiece choice) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getChoices().contains(choice.getID())) sp.removeChoice(choice);
		}
	}
	
	protected Object readResolve() {
		getInstance().setActiveStoryPiece(getActiveStoryPiece());
		getInstance().setAllStoryPieces(getAllStoryPieces());	
	    return getInstance();
	}
}
