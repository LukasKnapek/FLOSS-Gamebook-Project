package mabufudyne.gbdesigner.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mabufudyne.gbdesigner.gui.SaveDirtyConfirmDialog;
import mabufudyne.gbdesigner.gui.MainWindow;
/**
 * Handles events related to storage handling such as saving and laoding
 */
public class FileEventHandler {
	
	private static String lastFileLocation;
	private static String lastFileName;
	private static boolean isFileDirty;
	private static Operation operationState;
	private static Memento lastFileSavedState;
	
	/**
	 * Saves the current Adventure as an *.adv file to the location given by user in selection dialog
	 * @param quickSave - if true, the method doesn't ask for the file destination but rather uses the one that has been used before
	 */
	public static boolean saveAdventure(boolean quickSave) {
		String savePath = "";
		if (quickSave) savePath = getLastFileLocation() + lastFileName;
		else savePath = MainWindow.getInstance().invokeSaveDialog(getLastFileLocation());

		if (savePath != null) {
			try {
				FileOutputStream fOut = new FileOutputStream(savePath);
				ObjectOutputStream out = new ObjectOutputStream(fOut);
				out.writeObject(StoryPieceManager.getInstance());
				out.close();
				fOut.close();
				
				setLastFileLocation(savePath.substring(0, savePath.lastIndexOf(File.separator)));
				lastFileName = savePath.substring(savePath.lastIndexOf(File.separator));
				
				MainWindow.getInstance().changeApplicationTitle(lastFileName.substring(1) + " - " + getLastFileLocation());
				MainWindow.getInstance().buttonCheck();
				setDirtyStatus(false);
				
				System.out.println("Saved to: " + getLastFileLocation() + lastFileName);
				
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * Loads an Adventure from an existing *.adv file selected by user in selection dialog.
	 */
	public static boolean loadAdventure() { 
		
		boolean canLoad = checkForUnsavedFileChanges();
		if (!canLoad) return false;

		String loadPath = MainWindow.getInstance().invokeLoadDialog(getLastFileLocation());
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
				
				MementoManager.getInstance().revertToDefault();
				StoryPieceEventHandler.handleActionAftermath(true, true);
				
				setLastFileLocation(loadPath.substring(0, loadPath.lastIndexOf(File.separator)));
				lastFileName = loadPath.substring(loadPath.lastIndexOf(File.separator));

				System.out.println("Loaded from: " + getLastFileLocation() + lastFileName);
				
				MainWindow.getInstance().changeApplicationTitle(lastFileName.substring(1) + " - " + getLastFileLocation());
				MainWindow.getInstance().buttonCheck();
				setDirtyStatus(false);
				
				return true;
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static boolean checkForUnsavedFileChanges() {
		if (isFileDirty) {
			SaveDirtyConfirmDialog warningDialog = new SaveDirtyConfirmDialog(MainWindow.getInstance().getShell());
			warningDialog.open();
			
			switch (getOperationState()) {
				case SAVE:
					boolean saveSuccess = saveAdventure(false);
					if (saveSuccess) return true;
					else return false;
				case DISCARD:
					return true;
				case CANCEL:
					return false;
				default:
					return false;
			}
		}
		else return true;
	}
	
	public static void resetPaths() {
		setLastFileLocation(lastFileName = null);
	}
	
	public static void setDirtyStatus(boolean isDirty) {
		isFileDirty = isDirty;
		// If we reset the dirty status (save/load/new Adventure), fetch the current state for future comparison
		if (!isDirty) setLastFileSavedState(MementoManager.getInstance().getcurrentState());
		
		MainWindow.getInstance().showFileDirtyStatus(isDirty);
	}

	public static String getLastFileLocation() {
		return lastFileLocation;
	}

	public static void setLastFileLocation(String lastFileLocation) {
		FileEventHandler.lastFileLocation = lastFileLocation;
	}

	public static Operation getOperationState() {
		return operationState;
	}

	public static void setOperationState(Operation operationState) {
		FileEventHandler.operationState = operationState;
	}

	public static Memento getLastFileSavedState() {
		return lastFileSavedState;
	}

	public static void setLastFileSavedState(Memento lastSavedState) {
		FileEventHandler.lastFileSavedState = lastSavedState;
	}

}
