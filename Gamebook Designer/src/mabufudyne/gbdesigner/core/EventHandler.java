package mabufudyne.gbdesigner.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mabufudyne.gbdesigner.gui.ChoiceWindow;
import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	
	public static String lastFileLocation;
	public static String lastFileName;
	
	// Create new SP, save state and return the SP
	public static StoryPiece createNewStoryPieceAndActivate() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		changeActiveStoryPiece(sp);
		MainWindow.getInstance().displayStoryPieceItem(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		handleActionAftermath();
		return sp;
	}
	
	
	// Create new SP, dont immediately save state
	public static StoryPiece createNewStoryPiece() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		return sp;
	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		// Remove the SP, its references in any other SP choices and its item
		StoryPieceManager.getInstance().removeStoryPieceLinks(sp);
		StoryPieceManager.getInstance().removeStoryPiece(sp);
		MainWindow.getInstance().removeStoryPieceItem(sp);
		
		// Highlight the new active SP and display its contents
		MainWindow.getInstance().clearFields();
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), 0);
		
		handleActionAftermath();
	}

	public static void changeActiveStoryPiece(StoryPiece sp) {
		// Change active SP, highlight it in the grid and display its contents
		StoryPieceManager.getInstance().setActiveStoryPiece(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(sp, 0);
	}
	
	// Display choice selection window, save state once user finished working with it
	public static void displayChoiceSelectionWindow() {
		ChoiceWindow.getInstance().open();
		// Update active SP in view (we might have added choices)
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), 0);
		handleActionAftermath();
	}
	
	public static void addChoice(StoryPiece choice) {
		StoryPieceManager.getInstance().getActiveStoryPiece().addChoice(choice);
		// Remove the added choice from the view
		ChoiceWindow.getInstance().removeChoiceFromView(choice);
	}
	
	public static void removeChoice(StoryPiece choice, int choiceIndex) {
		StoryPieceManager.getInstance().removeChoice(choice);
		// Update active SP in view (we removed a choice)
		if (choiceIndex != 0) choiceIndex = choiceIndex - 1;
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), choiceIndex);
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void saveAdventure(boolean quickSave) {
		String savePath = "";
		if (quickSave) savePath = lastFileLocation + lastFileName;
		else savePath = MainWindow.getInstance().invokeSaveDialog(lastFileLocation);

		if (savePath != null) {
			try {
				FileOutputStream fOut = new FileOutputStream(savePath);
				ObjectOutputStream out = new ObjectOutputStream(fOut);
				out.writeObject(StoryPieceManager.getInstance());
				out.close();
				fOut.close();
				
				lastFileLocation = savePath.substring(0, savePath.lastIndexOf(File.separator));
				lastFileName = savePath.substring(savePath.lastIndexOf(File.separator));
				
				System.out.println("Saved to: " + lastFileLocation + lastFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void loadAdventure() { 
		String loadPath = MainWindow.getInstance().invokeLoadDialog(lastFileLocation);
		if (loadPath != null) {
			try {
				FileInputStream fIn = new FileInputStream(loadPath);
				ObjectInputStream in = new ObjectInputStream(fIn);
				Object loadedObject = in.readObject();
				in.close();
				fIn.close();
				
				if (loadedObject instanceof StoryPieceManager) {
					StoryPieceManager loadedManager = (StoryPieceManager) loadedObject;
					StoryPieceManager.replaceManager(loadedManager);
					MainWindow.getInstance().reloadUI();
				}
								
				lastFileLocation = loadPath.substring(0, loadPath.lastIndexOf(File.separator));
				lastFileName = loadPath.substring(loadPath.lastIndexOf(File.separator));

				System.out.println("Loaded from: " + lastFileLocation + lastFileName);

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void exportAdventure() {
		String exportPath = MainWindow.getInstance().invokeExportDialog();
		ExportManager.getInstance().exportAdventure(exportPath);
	}

	public static void saveState() {
		MementoManager.getInstance().saveState();
	}
	
	public static void redo() {
		Memento lastState = MementoManager.getInstance().getNextState();
		StoryPieceManager.replaceManager(lastState.getManagerMemento());
		MainWindow.getInstance().reloadUI();
	}
	
	public static void undo() {
		Memento lastState = MementoManager.getInstance().getPreviousState();
		StoryPieceManager.replaceManager(lastState.getManagerMemento());
		MainWindow.getInstance().reloadUI();
	}
	
	public static void handleActionAftermath() {
		saveState();
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void changeFixedProperty(StoryPiece sp) {
		sp.setFixed(!sp.isFixed());
		handleActionAftermath();
	}
	
	public static void changeStoryPieceOrder(StoryPiece sp, int order) {
		//  The new order for the SP must not be larger than the greatest current order
		if (order > 0 && order <= StoryPieceManager.getInstance().getNextAvailableOrder()-1) {
			StoryPieceManager.getInstance().resolveOrder(sp, order);
			StoryPieceManager.getInstance().sortStoryPieces();
			handleActionAftermath();
		}
		MainWindow.getInstance().reloadUI();
	}

	public static void changeStoryPieceTitle(StoryPiece sp, String title) {
		sp.setTitle(title);
		MainWindow.getInstance().reloadUI();
		handleActionAftermath();
	}

	public static void changeStoryPieceStory(StoryPiece sp, String story) {
		sp.setStory(story);
		handleActionAftermath();
	}
	
	public static void performInitialSetup() {
		EventHandler.createNewStoryPieceAndActivate();
	}

	public static void randomizeStoryPieceOrder() {
		StoryPieceManager.getInstance().randomizeOrder();
		MainWindow.getInstance().reloadUI();
		
	}
	
	public static void sortStoryPieces() {
		StoryPieceManager.getInstance().sortStoryPieces();
		MainWindow.getInstance().reloadUI();
	}
	
	public static void changeChoiceText(StoryPiece choice, String text, int choiceIndex) {
		StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
		activeSP.getChoicesTexts().replace(choice, text);
		MainWindow.getInstance().displayStoryPieceContents(activeSP, choiceIndex);
		handleActionAftermath();
	}


	public static void createNewAdventure() {
		MementoManager.getInstance().revertToDefault();
		StoryPieceManager.getInstance().revertToDefault();
		lastFileLocation = lastFileName = null;
		
		createNewStoryPieceAndActivate();
		MainWindow.getInstance().reloadUI();
		MainWindow.getInstance().buttonCheck();
	}

}
