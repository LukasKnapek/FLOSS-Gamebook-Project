package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	public static void createNewStoryPiece() {
		StoryPiece sp = StoryPieceManager.addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		changeActiveStoryPiece(sp);

	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		StoryPieceManager.removeStoryPiece(sp);
		MainWindow.getInstance().reorderStoryPieces(sp);
		
		if (StoryPieceManager.getActiveStoryPiece() != null) {
			MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getActiveStoryPiece());
		}
		else {
			MainWindow.getInstance().clearFields();
		}
		performUICheck();
	}
	
	public static void performUICheck() {
		MainWindow.getInstance().buttonCheck();
	}

	public static void saveStoryPieceChanges(String title, String story) {
		if (StoryPieceManager.getActiveStoryPiece() != null) {
			StoryPieceManager.saveChanges(title, story);
		}
		
	}

	public static void changeActiveStoryPiece(StoryPiece sp) {
		MainWindow.getInstance().updateStoryPieceItemTitle(StoryPieceManager.getActiveStoryPiece());
		StoryPieceManager.setActiveStoryPiece(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(sp);
	}
}
