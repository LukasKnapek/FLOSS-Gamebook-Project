package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StoryPieceManager implements Serializable {
	
	private static final long serialVersionUID = 7140106431174459518L;
	private transient static StoryPieceManager instance = new StoryPieceManager();
	private int maxOrder = 0;
	private StoryPiece activeStoryPiece;
	private ArrayList<StoryPiece> allStoryPieces = new ArrayList<StoryPiece>();
	private ArrayList<Integer> storyPieceOrders = new ArrayList<Integer>();
	
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

	public static void replaceManager(StoryPieceManager manager) {
		instance = manager;
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
	
	public StoryPiece addNewStoryPiece() {
		StoryPiece sp = new StoryPiece();
		allStoryPieces.add(sp);
		storyPieceOrders.add(sp.getOrder());
		return sp;
	}
	
	public void removeStoryPiece(StoryPiece sp) {
		chooseNewActiveStoryPiece(allStoryPieces.indexOf(sp));
		allStoryPieces.remove(sp);
		storyPieceOrders.remove((Object) sp.getOrder());
		if (sp.getOrder() == maxOrder) decrementOrder();
	}
	/** Chooses a new active StoryPiece based on the position of the old one to be deleted */
	private void chooseNewActiveStoryPiece(int deletedSPPosition) {
		// If the deleted StoryPiece is the first one, we will make the next one active
		if (deletedSPPosition == 0) activeStoryPiece = allStoryPieces.get(1);
		// If the deleted StoryPiece is the last one, make the one before it active
		else if (deletedSPPosition == allStoryPieces.size()-1) activeStoryPiece = allStoryPieces.get(deletedSPPosition-1);
		// Otherwise just make active the StoryPiece that follows the deleted StoryPiece
		else activeStoryPiece = allStoryPieces.get(deletedSPPosition+1);
	}
	
	public void addChoice(StoryPiece sp) {
		activeStoryPiece.addChoice(sp);
	}
	
	public void removeChoice(StoryPiece sp) {
		activeStoryPiece.removeChoice(sp);	
	}

	public void removeStoryPieceLinks(StoryPiece choice) {
		for (StoryPiece sp : allStoryPieces) {
			if (sp.getChoicesTexts().containsKey(choice)) sp.removeChoice(choice);
		}
	}
	
	public boolean canRemoveStoryPieces() {
		return allStoryPieces.size() > 1;
	}
	
	public int getNextAvailableOrder() {
		for (int i=1; i<=maxOrder; i++) {
			if (!storyPieceOrders.contains(i)) {
				return i;
			}
		}
		incrementOrder();
		return maxOrder;
	}
	
	
	public void incrementOrder() { 
		maxOrder++;
	}
	
	public void decrementOrder() {
		maxOrder--;
	}

	// If an SP is set to have order another SP has, switches the order of these SPs
	public void resolveOrder(StoryPiece SPNew, int order) {
		for (StoryPiece SPOld : allStoryPieces) {
			if (SPOld.getOrder() == order) {
				SPOld.setOrder(SPNew.getOrder());
				SPNew.setOrder(order);
			}
		}
	}
	
	public void sortStoryPieces() {
		Collections.sort(allStoryPieces);
	}

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


}
