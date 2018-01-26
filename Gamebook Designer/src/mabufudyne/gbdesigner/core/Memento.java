package mabufudyne.gbdesigner.core;

import java.util.ArrayList;

public class Memento {
	
	private StoryPieceManager managerMemento;
	
	public Memento() {
		managerMemento = (StoryPieceManager) Utils.deepCopyObject(StoryPieceManager.getInstance());
	}

	public StoryPieceManager getManagerMemento() {
		return managerMemento;
	}
}
