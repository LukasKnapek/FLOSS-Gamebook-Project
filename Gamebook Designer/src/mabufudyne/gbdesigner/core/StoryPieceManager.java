package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.ArrayList;

public class StoryPieceManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7140106431174459518L;
	private transient static StoryPieceManager instance = new StoryPieceManager();
	private int maxAvailableOrder = 1;
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
		// If the deleted StoryPiece is the first one, we will make the next one active
		if (deletedSPPosition == 0) activeStoryPiece = allStoryPieces.get(1);
		// Otherwise just make active the StoryPiece in front of the deleted one
		else activeStoryPiece = allStoryPieces.get(deletedSPPosition-1);
	}
	
	public void saveChanges(String[] titleStory) {
		activeStoryPiece.setTitle(titleStory[0]);
		activeStoryPiece.setStory(titleStory[1]);
	}
	
	public void addChoice(StoryPiece sp) {
		activeStoryPiece.addChoice(sp);
	}

	public void removeStoryPieceLinks(StoryPiece choice) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getChoices().contains(choice)) sp.removeChoice(choice);
		}
	}
	
	protected Object readResolve() {
		getInstance().setActiveStoryPiece(getActiveStoryPiece());
		getInstance().setAllStoryPieces(getAllStoryPieces());	
	    return getInstance();
	}
	
	public boolean canRemoveStoryPieces() {
		return allStoryPieces.size() > 1;
	}
	
	public int getNextAvailableOrder() {
		return maxAvailableOrder;
	}
	
	public void incrementOrder() { 
		maxAvailableOrder++;
	}
	
	public void decrementOrder() {
		maxAvailableOrder--;
	}
}
