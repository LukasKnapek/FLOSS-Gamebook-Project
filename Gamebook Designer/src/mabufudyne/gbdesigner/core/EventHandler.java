package mabufudyne.gbdesigner.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mabufudyne.gbdesigner.gui.ChoiceWindow;
import mabufudyne.gbdesigner.gui.MainWindow;

public class EventHandler {
	
	private static String lastSaveLocation;
	private static String lastFileName;
	private static String lastLoadLocation;
	
	public static void createNewStoryPieceAndActivate() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		changeActiveStoryPiece(sp);
		MainWindow.getInstance().displayStoryPieceItem(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		handleActionAftermath();
	}
	
	public static StoryPiece createNewstoryPiece() {
		saveStoryPieceChanges();
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		return sp;
	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		StoryPieceManager.getInstance().removeStoryPieceLinks(sp);
		StoryPieceManager.getInstance().removeStoryPiece(sp);
		MainWindow.getInstance().deleteStoryPieceItem(sp);
		
		if (StoryPieceManager.getInstance().getActiveStoryPiece() != null) {
			MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece());
		}
		else {
			MainWindow.getInstance().clearFields();
		}
		MainWindow.getInstance().highlightActiveStoryPiece();
		
		handleActionAftermath();
	}
		
	public static void saveStoryPieceChanges() {
		if (StoryPieceManager.getInstance().getActiveStoryPiece() != null) {
			StoryPieceManager.getInstance().saveChanges(MainWindow.getInstance().getTitleStoryFields());
			MainWindow.getInstance().reloadUI();
		}		
	}

	public static void changeActiveStoryPiece(StoryPiece sp) {
		MainWindow.getInstance().updateStoryPieceItemTitle(StoryPieceManager.getInstance().getActiveStoryPiece());
		StoryPieceManager.getInstance().setActiveStoryPiece(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(sp);
	}
	
	public static void displayChoiceSelectionWindow() {
		ChoiceWindow.getInstance().open();
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece());
		handleActionAftermath();
	}
	
	public static void addChoice(StoryPiece choice) {
		StoryPieceManager.getInstance().getActiveStoryPiece().addChoice(choice);
		ChoiceWindow.getInstance().removeChoiceFromView(choice);
	}
	
	public static void removeChoice(StoryPiece choice) {
		StoryPieceManager.getInstance().getActiveStoryPiece().removeChoice(choice);
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece());
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void saveAdventure() {
		String savePath = MainWindow.getInstance().invokeSaveDialog(lastSaveLocation);
		if (savePath != null) {
			try {
				FileOutputStream fOut = new FileOutputStream(savePath);
				ObjectOutputStream out = new ObjectOutputStream(fOut);
				out.writeObject(StoryPieceManager.getInstance());
				out.close();
				fOut.close();
				
				lastSaveLocation = savePath.substring(0, savePath.lastIndexOf("/"));
				lastFileName = savePath.substring(savePath.lastIndexOf("/"));
				
				System.out.println(lastSaveLocation + " " + lastFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadAdventure() { 
		String loadPath = MainWindow.getInstance().invokeLoadDialog(lastLoadLocation);
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
				
				lastLoadLocation = loadPath.substring(0, loadPath.lastIndexOf("/"));

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		StoryPieceManager.getInstance().setAllStoryPieces(lastState.getAllStoryPieces());
		StoryPieceManager.getInstance().setActiveStoryPiece(lastState.getActiveStoryPiece());
		MainWindow.getInstance().reloadUI();
	}
	
	public static void undo() {
		Memento lastState = MementoManager.getInstance().getPreviousState();
		StoryPieceManager.getInstance().setAllStoryPieces(lastState.getAllStoryPieces());
		StoryPieceManager.getInstance().setActiveStoryPiece(lastState.getActiveStoryPiece());
		MainWindow.getInstance().reloadUI();
		
		//System.out.println(StoryPieceManager.getInstance().getActiveStoryPiece());
		//System.out.println(StoryPieceManager.getInstance().getAllStoryPieces().contains(StoryPieceManager.getInstance().getActiveStoryPiece()));
	}
	
	public static void handleActionAftermath() {
		saveState();
		MainWindow.getInstance().buttonCheck();
	}
	
	public static void changeFixedProperty(StoryPiece sp) {
		sp.setFixed(!sp.isFixed());
	}
	
	public static void changeStoryPieceOrder(StoryPiece sp, int order) {
		sp.setOrder(order);
		MainWindow.getInstance().reloadUI();
	}

	public static void changeStoryPieceTitle(StoryPiece sp, String title) {
		sp.setTitle(title);
		MainWindow.getInstance().reloadUI();
	}

	public static void changeStoryPieceStory(StoryPiece sp, String story) {
		sp.setStory(story);
	}
}
