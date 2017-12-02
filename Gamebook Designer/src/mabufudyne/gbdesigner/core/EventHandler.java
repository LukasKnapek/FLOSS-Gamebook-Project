package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.ChoiceWindow;
import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	public static void createNewStoryPieceAndActivate() {
		StoryPiece sp = StoryPieceManager.addNewStoryPiece();
		changeActiveStoryPiece(sp);
		MainWindow.getInstance().displayStoryPieceItem(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().buttonCheck();
	}
	
	public static StoryPiece createNewstoryPiece() {
		StoryPiece sp = StoryPieceManager.addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		return sp;
	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		StoryPieceManager.removeStoryPieceLinks(sp);
		StoryPieceManager.removeStoryPiece(sp);
		MainWindow.getInstance().reorderStoryPieces(sp);
		
		if (StoryPieceManager.getActiveStoryPiece() != null) {
			MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getActiveStoryPiece());
		}
		else {
			MainWindow.getInstance().clearFields();
		}
		MainWindow.getInstance().highlightActiveStoryPiece();
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
	
	public static void displayChoiceSelectionWindow() {
		ChoiceWindow.getInstance().open();
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getActiveStoryPiece());
	}
	
	public static void addChoice(StoryPiece choice) {
		StoryPieceManager.getActiveStoryPiece().addChoice(choice);
		ChoiceWindow.getInstance().removeChoiceFromView(choice);
	}
	
	public static void removeChoice(StoryPiece choice) {
		StoryPieceManager.getActiveStoryPiece().removeChoice(choice);
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getActiveStoryPiece());
		MainWindow.getInstance().buttonCheck();
	}
}
