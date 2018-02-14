package mabufudyne.gbdesigner.core;

import java.util.ArrayList;
/** 
 * Stores and operates on a collection of Mementos, application states, representing the application state history
 */
public class MementoManager {
	private ArrayList<Memento> history;
	private static MementoManager instance = new MementoManager();
	private static int currentStatePosition = 0;
	
	/* Constructors */
	
	public MementoManager() {
		history = new ArrayList<Memento>();
	}
	
	/* Getters */
	
	public Memento getPreviousState() {
		currentStatePosition--;
		return history.get(currentStatePosition);
	}
	
	public Memento getNextState() {
		currentStatePosition++;
		return history.get(currentStatePosition);
	}
	
	public Memento getcurrentState() {
		return history.get(currentStatePosition);
	}
	
	public static MementoManager getInstance() {
		return instance;
	}
	
	/**
	 * Creates a new state (Memento instance) and stores it in the history (Memento collection).
	 * If the redo option was available and the user instead performed a new action, cuts off all the possible redo states.
	 */
	public void saveState(String actionDescription) {
		Memento newState = new Memento(actionDescription);
		
		// If the history is empty, do nothing here
		if (history.size() == 0) { }
		// If we are not at the end of history (we have performed undo), cut off the redo part of the history
		else if (currentStatePosition != history.size()-1) {
			history = new ArrayList<Memento> (history.subList(0, currentStatePosition+1));
		}
		// If the history is not empty, increment position before saving the new state
		if (history.size() != 0) {
			currentStatePosition++;
		}
		history.add(newState);
	}

	/**
	 * Determines whether user can perform an undo operation at present time.
	 */
	public boolean canUndo() {
		return currentStatePosition != 0;
	}
	
	/**
	 * Determines whether user can perform a redo operation at present time.
	 */
	public boolean canRedo() {
		return currentStatePosition != history.size()-1;

	}
	
	/**
	 * Clears history and sets the current state pointer back to 0
	 */
	public void revertToDefault() {
		history.clear();
		currentStatePosition = 0;
	}
	
}
