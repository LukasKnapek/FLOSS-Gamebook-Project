package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.HashMap;

public class StoryPiece implements Serializable, Comparable<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4835564426765463L;
	private int order;
	private boolean fixedOrder;
	private String title;
	private String story;
	private HashMap<StoryPiece, String> choicesTexts;
	
	/* Constructors */
	
	public StoryPiece() {
		this.title = "Untitled";
		this.story = "";
		this.order = StoryPieceManager.getInstance().getNextAvailableOrder();
		this.fixedOrder = false;
		this.choicesTexts = new HashMap<StoryPiece, String>();
		
	}

	/* Getters and Setters */
	
	

	public String getTitle() {
		return title;
	}

	public HashMap<StoryPiece, String> getChoicesTexts() {
		return choicesTexts;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
	
	public void addChoice(StoryPiece sp) {
		this.choicesTexts.put(sp, "Go to");
	}

	public void removeChoice(StoryPiece sp) {
		this.choicesTexts.remove((Object) sp); 
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isFixed() {
		return fixedOrder;
	}

	public void setFixed(boolean fixedOrder) {
		this.fixedOrder = fixedOrder;
	}

	@Override
	public int compareTo(Object other) {
		StoryPiece otherSP = (StoryPiece) other;
		int sp1Order = this.getOrder();
		int sp2Order = otherSP.getOrder();
		if (sp1Order == sp2Order) return 0;
		else if (sp1Order < sp2Order) return -1;
		else return 1;
	}
}
