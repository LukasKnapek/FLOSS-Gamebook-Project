package mabufudyne.gbdesigner.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class MementoManager {
	private ArrayList<Memento> history;
	private static MementoManager instance = new MementoManager();
	private static int iteratorPosition = 0;
	
	public MementoManager() {
		history = new ArrayList<Memento>();
	}

	public void saveState() {
		Memento newState = new Memento();
		
		// If we are not at the end of the history, cut off the rest of it since new action
		// performed while browsing history will disable redo
		if (iteratorPosition != history.size()-1) {
			history = new ArrayList<Memento> (history.subList(0, iteratorPosition));
			history.add(newState);
		}
		else if (history.size() != 0) {
			iteratorPosition++;
			history.add(newState);
		}
		//System.out.println("Index - " + iteratorPosition + ": Array indices - 0-" + (history.size()-1));
	}
	
	public void saveInitialState() {
		Memento newState = new Memento();
		history.add(newState);
		//System.out.println("Index - " + iteratorPosition + ": Array indices - 0-" + (history.size()-1));
	}
	
	public Memento getPreviousState() {
		iteratorPosition--;
		//System.out.println("Index - " + iteratorPosition + ": Array indices - 0-" + (history.size()-1));
		return history.get(iteratorPosition);
	}
	
	public Memento getNextState() {
		iteratorPosition++;
		return history.get(iteratorPosition); 
	}
	
	public static MementoManager getInstance() {
		return instance;
	}
	
	public boolean canUndo() {
		return iteratorPosition != 0;
	}
	
	public boolean canRedo() {
		return iteratorPosition != history.size()-1;

	}
	
	
}
