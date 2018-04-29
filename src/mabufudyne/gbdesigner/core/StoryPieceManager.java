package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Keeps track of currently used {@link StoryPiece} instances and the order numbers that they use
 */
public class StoryPieceManager implements Serializable {
	
	private static final long serialVersionUID = 7140106431174459518L;
	private transient static StoryPieceManager instance = new StoryPieceManager();
	private int maxOrder = 0;
	private StoryPiece activeStoryPiece;
	private ArrayList<StoryPiece> allStoryPieces = new ArrayList<StoryPiece>();
	private ArrayList<Integer> storyPieceOrders = new ArrayList<Integer>();
	
	/* Getters and setters */

	public static StoryPieceManager getInstance() {
		return instance;
	}
	
	public int getMaxOrder() {
		return maxOrder;
	}

	public void setMaxOrder(int maxOrder) {
		this.maxOrder = maxOrder;
	}

	public ArrayList<Integer> getStoryPieceOrders() {
		return storyPieceOrders;
	}

	public void setStoryPieceOrders(ArrayList<Integer> storyPieceOrders) {
		this.storyPieceOrders = storyPieceOrders;
	}
	
	public StoryPiece getActiveStoryPiece() {
		return activeStoryPiece;
	}
	
	public void setActiveStoryPiece(StoryPiece sp) {
		activeStoryPiece = sp;
	}

	public ArrayList<StoryPiece> getAllStoryPieces() {
		return allStoryPieces;
	}

	public void setAllStoryPieces(ArrayList<StoryPiece> allStoryPieces) {
		this.allStoryPieces = allStoryPieces;
	}
	
	/* Methods */
	
	/**
	 * Creates a new {@link Storypiece} instance. 
	 * Then adds it to the list of used StoryPieces and also adds its order to the list of used orders.
	 */
	public StoryPiece addNewStoryPiece() {
		StoryPiece sp = new StoryPiece();
		allStoryPieces.add(sp);
		storyPieceOrders.add(sp.getOrder());
		return sp;
	}
	
	/**
	 * First activates some other than the current StoryPiece. Then:
	 * 1) Removes the given StoryPiece from list of all StoryPieces
	 * 2) Also removes the StoryPiece order from the list of all used orders
	 * 3) If the used order was the maximum order, decrements the maximum order
	 * @param sp - The StoryPiece instance that should be removed
	 */
	public void removeStoryPiece(StoryPiece sp) {
		chooseNewActiveStoryPiece(allStoryPieces.indexOf(sp));
		allStoryPieces.remove(sp);
		storyPieceOrders.remove((Object) sp.getOrder());
		if (sp.getOrder() == maxOrder) decrementOrder();
	}
	
	/**
	 * Makes a StoryPiece active based on the position of the old active StoryPiece
	 * @param oldActiveSPPosition - The position of the old active StoryPiece in the list of all StoryPieces
	 */
	private void chooseNewActiveStoryPiece(int oldActiveSPPosition) {
		// If the deleted StoryPiece is the first one, we will make the next one active
		if (oldActiveSPPosition == 0) activeStoryPiece = allStoryPieces.get(1);
		// If the deleted StoryPiece is the last one, make the one before it active
		else if (oldActiveSPPosition == allStoryPieces.size()-1) activeStoryPiece = allStoryPieces.get(oldActiveSPPosition-1);
		// Otherwise just make active the StoryPiece that follows the deleted StoryPiece
		else activeStoryPiece = allStoryPieces.get(oldActiveSPPosition+1);
	}
	
	/**
	 * Adds the StoryPiece selected in ChoiceWindow as a choice for the currently active StoryPiece
	 * @param sp - The StoryPiece that should be added as a new choice to the active StoryPiece
	 */
	public void addChoice(StoryPiece sp) {
		activeStoryPiece.addChoice(sp);
	}
	
	/**
	 * Removes the StoryPiece selected in ChoiceWindow from the currently active StoryPiece choices
	 * @param sp - The StoryPiece that should be removed from the active StoryPiece choices
	 */
	public void removeChoice(StoryPiece sp) {
		activeStoryPiece.removeChoice(sp);	
	}

	/**
	 * Removes the given StoryPiece as a choice from all StoryPieces where applicable
	 * @param choice - The StoryPiece which should be removed from the choices of all StoryPieces
	 */
	public void removeStoryPieceLinks(StoryPiece choice) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getChoicesTexts().containsKey(choice)) sp.removeChoice(choice);
		}
	}
	
	/**
	 * Returns true if we are allowed to remove StoryPieces (in case there is still more than one), false otherwise
	 */
	public boolean canRemoveStoryPieces() {
		return allStoryPieces.size() > 1;
	}
	
	/**
	 * Returns an integer representing the order the next created StoryPiece should use.
	 * If N represents the currently used maximum order, the function will allocate all order numbers between 1-N first.
	 * If all order numbers 1-N are in use, return N+1, N+2,...
	 */
	public int getNextAvailableOrder() {
		for (int i=1; i<=maxOrder; i++) {
			if (!storyPieceOrders.contains(i)) {
				return i;
			}
		}
		incrementOrder();
		return maxOrder;
	}
	
	/**
	 * Increments the maximum order number currently used by the StoryPieces
	 */
	public void incrementOrder() { 
		maxOrder++;
	}
	
	/**
	 * Decrements the maximum order number currently used by the StoryPieces
	 */
	public void decrementOrder() {
		maxOrder--;
	}

	/**
	 * If a StoryPiece is assigned an order number (by user) that is already being used by another StoryPiece,
	 * switch the order numbers of the two StoryPieces.
	 * @param SPNew - The StoryPiece which has been assigned an order that is already in use by other StoryPiece
	 * @param order - The assigned order number that is disputed
	 */
	public void resolveOrder(StoryPiece SPNew, int order) {
		for (StoryPiece SPOld : allStoryPieces) {
			if (SPOld.getOrder() == order) {
				SPOld.setOrder(SPNew.getOrder());
				SPNew.setOrder(order);
			}
		}
	}
	
	/**
	 * Sorts the list of all StoryPieces by their order number (in Model)
	 */
	public void sortStoryPieces() {
		Collections.sort(allStoryPieces);
	}

	/**
	 * Randomizes order of all StoryPieces.
	 * StoryPieces whose fixedOrder field is set to TRUE are not affected.
	 */
	public void randomizeOrder() {
		Random rnd = new Random();
		ArrayList<Integer> orders = new ArrayList<Integer>();
		for (StoryPiece sp : allStoryPieces) {
			if (!sp.isFixed()) orders.add(sp.getOrder());
		}
		
		for (StoryPiece sp : allStoryPieces) {
			if (!sp.isFixed()) {
				int randChoice = rnd.nextInt(orders.size());
				sp.setOrder(orders.get(randChoice));
				orders.remove(randChoice);
			}
		}
	}

	/**
	 * Resets the StoryPieceManager to its default state: </br>
	 * 1) Clear the list of all StoryPieces </br>
	 * 2) Clear the list of all order numbers used by StoryPieces </br>
	 * 3) Reset maximum order to 0
	 */
	public void revertToDefault() {
		allStoryPieces.clear();
		storyPieceOrders.clear();
		maxOrder = 0;
	}

	/**
	 * Replace the current instance of StoryPieceManager with another one.
	 * StoryPieceManager instance is the main part of the {@link Memento}.
	 * This method is used for undo/redo/load purposes
	 * @param manager - An instance of StoryPieceManager that should be used by the application from now on
	 */
	public static void replaceManager(StoryPieceManager manager) {
		instance = manager;
	}

}
