package mabufudyne.gbdesigner.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoryPiece implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4835564426765463L;
	private String title;
	private String story;
	private ArrayList<StoryPiece> choices;
	
	/* Constructors */
	
	public StoryPiece() {
		this.title = "Untitled";
		this.story = "";
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
}
