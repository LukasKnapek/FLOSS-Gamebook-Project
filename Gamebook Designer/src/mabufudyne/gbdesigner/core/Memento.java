package mabufudyne.gbdesigner.core;

/** 
 * Represents a single application state, used for undo/redo operations
 */
public class Memento {
	
	private StoryPieceManager managerMemento;
	private String actionDescription;
	
	/* Constructors */
	
	public Memento(String description) {
		managerMemento = (StoryPieceManager) Utils.deepCopyObject(StoryPieceManager.getInstance());
		actionDescription = description;
	}

	/* Getters */
	
	public StoryPieceManager getManagerMemento() {
		return managerMemento;
	}
	
	public String getActionDescription() {
		return actionDescription;
	}
}
