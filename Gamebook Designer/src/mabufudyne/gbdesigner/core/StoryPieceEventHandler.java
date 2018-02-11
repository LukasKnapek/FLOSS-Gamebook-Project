package mabufudyne.gbdesigner.core;

import mabufudyne.gbdesigner.gui.ChoiceWindow;
import mabufudyne.gbdesigner.gui.MainWindow;

/**
 * Handles events related to, or operating on, StoryPieces
 */
public class StoryPieceEventHandler {
	
	/**
	 * Creates a new StoryPiece instance, makes it active and updates the UI
	 * @return The newly created StoryPiece instance
	 */
	public static StoryPiece createNewStoryPieceAndActivate() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		changeActiveStoryPiece(sp, false);
		MainWindow.getInstance().displayStoryPieceItem(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		handleActionAftermath(true, true);
		return sp;
	}
	
	/**
	 * Creates a new StoryPiece instance without making it active and updates the UI
	 * @return The newly created StoryPiece instance
	 */
	public static StoryPiece createNewStoryPiece() {
		StoryPiece sp = StoryPieceManager.getInstance().addNewStoryPiece();
		MainWindow.getInstance().displayStoryPieceItem(sp);
		return sp;
	}
	
	/**
	 * Removes the given StoryPiece instance from all places, i.e. StoryPieceManager records and choices of other StoryPieces.
	 * Then, updates the UI.
	 * @param sp - The StoryPiece to be removes
	 */
	public static void deleteStoryPiece(StoryPiece sp) {
		// Remove the SP, its references in any other SP choices and its View TableItem
		StoryPieceManager.getInstance().removeStoryPieceLinks(sp);
		StoryPieceManager.getInstance().removeStoryPiece(sp);
		MainWindow.getInstance().removeStoryPieceItem(sp);
		
		// Highlight the new active SP and display its contents
		MainWindow.getInstance().clearFields();
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), 0);
		
		handleActionAftermath(true, true);
	}

	/**
	 * Changes the active StoryPiece to the one that has been selected by user.
	 * @param sp - The StoryPiece instance whose TableItem has been selected.
	 */
	public static void changeActiveStoryPiece(StoryPiece sp, boolean saveState) {
		// Change active SP, highlight it in the grid and display its contents
		StoryPieceManager.getInstance().setActiveStoryPiece(sp);
		MainWindow.getInstance().highlightActiveStoryPiece();
		MainWindow.getInstance().displayStoryPieceContents(sp, 0);
		MainWindow.getInstance().buttonCheck();
		handleActionAftermath(saveState, false);
	}
	
	/**
	 * Saves the current state of the application and performs a button check, enabling/disabling buttons as 
	 * necessary to reflect the new state of the application.
	 */
	public static void handleActionAftermath(boolean saveState, boolean modifiesFile) {
		if (saveState) MementoManager.getInstance().saveState();
		if (modifiesFile) FileEventHandler.setDirtyStatus(true);
		
		MainWindow.getInstance().buttonCheck();

	}
	
	/** 
	 * Switches the 'fixed' boolean field of the given StoryPiece
	 * @param sp - The StoryPiece whose fixed property should be switched
	 */
	public static void changeFixedProperty(StoryPiece sp) {
		sp.setFixed(!sp.isFixed());
		handleActionAftermath(true, true);
	}
	
	/**
	 * Assigns the given StoryPiece a new order number. If there is an order conflict, it is resolved by calling the resolveOrder method.
	 * @param sp - The StoryPiece whose order number should be changed.
	 * @param order - The order number that should be assigned to the StoryPiece.
	 */
	public static void changeStoryPieceOrder(StoryPiece sp, int order) {
		//  The new order for the SP must not be larger than the greatest current order
		if (order > 0 && order <= StoryPieceManager.getInstance().getNextAvailableOrder()-1) {
			StoryPieceManager.getInstance().resolveOrder(sp, order);
			StoryPieceManager.getInstance().sortStoryPieces();
			handleActionAftermath(true, true);
		}
		MainWindow.getInstance().reloadUI();
	}

	/**
	 * Changes the 'title' field of the StoryPiece to the given title.
	 * @param sp - The StoryPiece whose title should be changed.
	 * @param title - The new title that should be used.
	 */
	public static void changeStoryPieceTitle(StoryPiece sp, String title) {
		sp.setTitle(title);
		MainWindow.getInstance().reloadUI();
		handleActionAftermath(true, true);
	}

	/**
	 * Changes the 'story' field of the StoryPiece to the given story.
	 * @param sp - The StoryPiece whose story should be changed.
	 * @param story - The new story that should be used.
	 */
	public static void changeStoryPieceStory(StoryPiece sp, String story) {
		sp.setStory(story);
		handleActionAftermath(true, true);
	}
	
	/**
	 * Adds the given StoryPiece as a choice to the current StoryPiece and refreshes UI.
	 * @param choice - The StoryPiece who should be the choice of the current StoryPiece
	 */
	public static void addChoice(StoryPiece choice) {
		StoryPieceManager.getInstance().getActiveStoryPiece().addChoice(choice);
		// Remove the added choice from the view
		ChoiceWindow.getInstance().removeChoiceFromView(choice);
	}
	
	/**
	 * Removes the given choice from the current StoryPiece choices and updates the UI.
	 * @param choice - The choice StoryPiece to be removed
	 * @param choiceIndex - The index of the TableItem of the given choice in the Choices table
	 */
	public static void removeChoice(StoryPiece choice, int choiceIndex) {
		StoryPieceManager.getInstance().removeChoice(choice);
		// Update active SP in View (we removed a choice)
		if (choiceIndex != 0) choiceIndex = choiceIndex - 1;
		MainWindow.getInstance().displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), choiceIndex);
		MainWindow.getInstance().buttonCheck();
		
		handleActionAftermath(true, true);
	}
	
	/**
	 * Calls the StoryPieceManager to randomize the order of StoryPieces and updates the UI afterwards.
	 */
	public static void randomizeStoryPieceOrder() {
		StoryPieceManager.getInstance().randomizeOrder();
		MainWindow.getInstance().reloadUI();
		handleActionAftermath(true, true);

		
	}
	
	/**
	 * Calls the StoryPieceManager to sort StoryPieces by order and updates the UI afterwards.
	 */
	public static void sortStoryPieces() {
		StoryPieceManager.getInstance().sortStoryPieces();
		MainWindow.getInstance().reloadUI();
		handleActionAftermath(true, true);
	}
	
	/**
	 * Updates the description text of the choice of the current StoryPiece. Updates the changes in UI afterwards.
	 * The choiceIndex argument is used to select the choice in the choices Table after this operation is done (as the selection is otherwise lost).
	 * @param choice - The StoryPiece who is the choice to the current StoryPiece
	 * @param text - The new choice text that should be used
	 * @param choiceIndex - The index of the TableItem of the given choice in the Choices table
	 */
	public static void changeChoiceText(StoryPiece choice, String text, int choiceIndex) {
		StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
		activeSP.getChoicesTexts().replace(choice, text);
		MainWindow.getInstance().displayStoryPieceContents(activeSP, choiceIndex);
		handleActionAftermath(true, true);
	}
	
	

	
}
