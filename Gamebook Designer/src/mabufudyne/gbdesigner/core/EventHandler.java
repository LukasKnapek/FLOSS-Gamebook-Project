package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	public static void createNewStoryPiece() {
		StoryPiece sp = StoryPieceManager.addNewStoryPiece();
		MainWindow.getInstance().displayStoryPiece(sp);
	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		StoryPieceManager.removeStoryPiece(sp);
		MainWindow.getInstance().reorderStoryPieces(sp);
		performUICheck();
	}
	
	public static void performUICheck() {
		MainWindow.getInstance().buttonCheck();
	}
}
