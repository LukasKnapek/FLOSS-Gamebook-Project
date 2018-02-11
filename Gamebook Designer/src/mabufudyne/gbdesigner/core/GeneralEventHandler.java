package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.ChoiceWindow;
import mabufudyne.gbdesigner.gui.MainWindow;

/**
 * Handles general application events
 */
public class GeneralEventHandler {
	
	/**
	 * Sets up the environemtn on application launch:</br>
	 * 1) Creates a new StoryPiece (as there must be at least one at all times)
	 */
	public static void performInitialSetup() {
		MainWindow.getInstance().changeApplicationTitle("Unnamed Adventure");
		StoryPieceEventHandler.createNewStoryPieceAndActivate();
		// New Adventure should not be considered a modified file
		FileEventHandler.setDirtyStatus(false);
	}

	/**
	 * Displays choice selection window where the user can add choices to the currently active StoryPiece.
	 * Afterwards, updates the View to reflect the changes for the active StoryPiece and saves the state
	 */
	public static void displayChoiceSelectionWindow() {
		ChoiceWindow.getInstance().open();
		// Update active SP in view (we might have added choices)
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), 0);
		StoryPieceEventHandler.handleActionAftermath(true, true);
	}
	
	/**
	 * Exports the current Adventure to a file.
	 */
	public static void exportAdventure() {
		String exportPath = MainWindow.getInstance().invokeExportDialog();
		ExportManager.getInstance().exportAdventure(exportPath);
	}
	
	/**
	 * Performs the action that has been undone by switching to the next state in History
	 */
	public static void redo() {
		Memento lastState = MementoManager.getInstance().getNextState();
		StoryPieceManager.replaceManager((StoryPieceManager) Utils.deepCopyObject(lastState.getManagerMemento()));
		// If our app state is different from the initial one by having performed a redo, enable the dirty file indicator
		if (lastState != FileEventHandler.getLastFileSavedState()) FileEventHandler.setDirtyStatus(true);
		MainWindow.getInstance().reloadUI();
	}
	
	/**
	 * Reverts the last action by switching to the previous state in History
	 */
	public static void undo() {
		Memento lastState = MementoManager.getInstance().getPreviousState();
		StoryPieceManager.replaceManager((StoryPieceManager) Utils.deepCopyObject(lastState.getManagerMemento()));
		// If we undoed to the state which is identical to the one we last saved/loaded/created anew, reset dirty file indicator
		if (lastState == FileEventHandler.getLastFileSavedState()) FileEventHandler.setDirtyStatus(false);
		MainWindow.getInstance().reloadUI();
	}

	/**
	 * Creates a new Adventure by reverting the Model and the View to their original states
	 */
	public static void createNewAdventure() {
		
		boolean canCreateNew = FileEventHandler.checkForUnsavedFileChanges();
		if (!canCreateNew) return;
		
		MementoManager.getInstance().revertToDefault();
		StoryPieceManager.getInstance().revertToDefault();
		FileEventHandler.resetPaths();
		
		performInitialSetup();
		MainWindow.getInstance().reloadUI();
		MainWindow.getInstance().buttonCheck();
	}

}
