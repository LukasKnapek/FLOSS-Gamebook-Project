package mabufudyne.gbdesigner.core;

import java.util.ArrayList;

public class Memento {
	
	private ArrayList<StoryPiece> allStoryPieces;
	private StoryPiece activeStoryPiece;
	private int nextAvailableOrder;
	
	public Memento() {
		StoryPieceManager currentManager = StoryPieceManager.getInstance();
		ArrayList<StoryPiece> storyPieces = (ArrayList<StoryPiece>) Utils.deepCopyObject(currentManager.getAllStoryPieces());
		int activeStoryPiecePosition = currentManager.getAllStoryPieces().indexOf(currentManager.getActiveStoryPiece());
		nextAvailableOrder = currentManager.getNextAvailableOrder();
		try {
			this.activeStoryPiece = storyPieces.get(activeStoryPiecePosition);
		}
		catch (Exception e) {
			this.activeStoryPiece = null;
		}
		
		this.setAllStoryPieces(storyPieces);
	}

	public StoryPiece getActiveStoryPiece() {
		return activeStoryPiece;
	}

	public void setActiveStoryPiece(StoryPiece activeStoryPiece) {
		this.activeStoryPiece = activeStoryPiece;
	}

	public ArrayList<StoryPiece> getAllStoryPieces() {
		return allStoryPieces;
	}

	public void setAllStoryPieces(ArrayList<StoryPiece> allStoryPieces) {
		this.allStoryPieces = allStoryPieces;
	}
	
	public int getNextAvailableOrder() {
		return nextAvailableOrder;
	}
}
