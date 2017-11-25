package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	public static void createNewStoryPiece() {
		StoryPiece sp = StoryPieceManager.addNewStoryPiece();
		MainWindow.getInstance().displayStoryPiece(sp);
	}
}
