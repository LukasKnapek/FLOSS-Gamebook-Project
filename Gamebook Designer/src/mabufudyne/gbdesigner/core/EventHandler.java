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
		MainWindow.getInstance().buttonCheck();
	}
	
	public static StoryPiece createNewstoryPiece() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		return sp;
	}
	
	public static void deleteStoryPiece(StoryPiece sp) {
		StoryPieceManager.getInstance().removeStoryPieceLinks(sp);
		StoryPieceManager.getInstance().removeStoryPiece(sp);
		MainWindow.getInstance().reorderStoryPieces(sp);
		
		if (StoryPieceManager.getInstance().getActiveStoryPiece() != null) {
			MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece());
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
		if (StoryPieceManager.getInstance().getActiveStoryPiece() != null) {
			StoryPieceManager.getInstance().saveChanges(title, story);
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

}
