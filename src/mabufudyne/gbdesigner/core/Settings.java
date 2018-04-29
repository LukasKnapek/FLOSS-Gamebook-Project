package mabufudyne.gbdesigner.core;

import java.io.Serializable;

public class Settings implements Serializable {
	
	private static Settings instance = new Settings();
	
	private String defaultStoryPieceName;
	private String defaultChoiceText;
	private int statusMessageTimeout;
	private String adventureTitle;
	private String choiceNumberSeparator;

	private Settings() {
		defaultStoryPieceName = "Untitled";
		defaultChoiceText = "Go to";
		statusMessageTimeout = 5;
		adventureTitle = "New Adventure";
		choiceNumberSeparator = "...";
	}
	
	public static Settings getInstance() {
		return instance;
	}
	
	public static void setInstance(Settings s) {
		instance = s;
	}

	public String getDefaultStoryPieceName() {
		return defaultStoryPieceName;
	}

	public void setDefaultStoryPieceName(String defaultStoryPieceName) {
		this.defaultStoryPieceName = defaultStoryPieceName;
	}

	public String getDefaultChoiceText() {
		return defaultChoiceText;
	}

	public void setDefaultChoiceText(String defaultChoiceText) {
		this.defaultChoiceText = defaultChoiceText;
	}

	public int getStatusMessageTimeout() {
		return statusMessageTimeout;
	}

	public void setStatusMessageTimeout(int statusMessageTimeout) {
		this.statusMessageTimeout = statusMessageTimeout;
	}

	public String getAdventureTitle() {
		return adventureTitle;
	}

	public void setAdventureTitle(String adventureTitle) {
		this.adventureTitle = adventureTitle;
	}

	public String getChoiceNumberSeparator() {
		return choiceNumberSeparator;
	}

	public void setChoiceNumberSeparator(String choiceNumberSeparator) {
		this.choiceNumberSeparator = choiceNumberSeparator;
	}
	
	
	
}
