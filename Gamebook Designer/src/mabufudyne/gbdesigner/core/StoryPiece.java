package mabufudyne.gbdesigner.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoryPiece {
	private UUID storyPieceID;
	private String title;
	private String story;
	private ArrayList<UUID> choices;
	
	/* Constructors */
	
	public StoryPiece() {
		this.storyPieceID = UUID.randomUUID();
		this.title = "Untitled";
		this.story = "";
		this.choices = new ArrayList<UUID>();
	}

	/* Getters and Setters */
	
	public UUID getID() {
		return storyPieceID;
	}
	
	public List<UUID> getChoices() {
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
		this.choices.add(sp.getID());
	}
}
