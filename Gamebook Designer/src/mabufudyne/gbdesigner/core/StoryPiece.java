package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StoryPiece implements Serializable, Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4835564426765463L;
	private int order;
	private boolean fixedOrder;
	private String title;
	private String story;
	private ArrayList<StoryPiece> choices;
	
	/* Constructors */
	
	public StoryPiece() {
		this.title = "Untitled";
		this.story = "";
		this.order = StoryPieceManager.getInstance().getNextAvailableOrder();
		StoryPieceManager.getInstance().incrementOrder();
		this.fixedOrder = false;
		this.choices = new ArrayList<StoryPiece>();
	}

	/* Getters and Setters */
	
	
	public List<StoryPiece> getChoices() {
		return choices;
	}

	public String getTitle() {
		return title;
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
		this.choices.add(sp);
	}

	public void removeChoice(StoryPiece sp) {
		this.choices.remove(sp);
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
