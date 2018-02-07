package mabufudyne.gbdesigner.core;

/** 
 * Represents a single application state, used for undo/redo operations
 */
public class Memento {
	
	private StoryPieceManager managerMemento;
	
	/* Constructors */
	
	public Memento() {
		managerMemento = (StoryPieceManager) Utils.deepCopyObject(StoryPieceManager.getInstance());
	}

	/* Getters */
	
	public StoryPieceManager getManagerMemento() {
		return managerMemento;
	}
}
