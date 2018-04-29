package mabufudyne.gbdesigner.core;

import java.io.BufferedWriter;
import java.io.FileWriter;

import mabufudyne.gbdesigner.gui.MainWindow;

/**
 * This class is used to export an Adventure to an output file (HTML)
 */
public class ExportManager {
	
	private StringBuilder HTMLContents;
	private static ExportManager instance = new ExportManager();
	
	/* Constructors */
	
	private ExportManager () {
		this.HTMLContents = new StringBuilder();
	}

	/* Getters and setters */
	
	public static ExportManager getInstance() {
		return instance;
	}
	
	/**
	 * The main export method which exports an Adventure to a file by doing the following steps</br>
	 * 1) Create the first half of basic HTML document structure
	 * 2) List the Adventure title
	 * 3) List the StoryPieces themselvs and their choices
	 * 4) Create the second half of the basic HTML structure
	 * @param exportPath - The location of the exported HTML file
	 */
	public void exportAdventure(String exportPath) {
		createBasicStructure();
		addAdventureTitle();
		listStoryPieces();
		endBasicStructure();
		
		if (exportPath != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(exportPath));
				writer.write(this.HTMLContents.toString());
				writer.close();
				MainWindow.getInstance().showStatusMessage(Status.INFO, "Adventure successfully exported to: " + exportPath);
			} catch (Exception e) {
				MainWindow.getInstance().showStatusMessage(Status.ERROR, "Error while exporting Adventure: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		this.HTMLContents.setLength(0);
	}
	
	private void createBasicStructure() {
		this.HTMLContents.append("<html>\n"
								+ "<head>\n"
								+ "<title>New Adventure</title>\n"
								+ "</head>\n"
								+ "<body>\n");
	}
	
	private void endBasicStructure() {
		this.HTMLContents.append("</body>\n"
				+ "</html>\n");
	}
	
	private void addAdventureTitle() {
		this.HTMLContents.append("\n<h1>" + Settings.getInstance().getAdventureTitle() + "</h1>\n");
	}
	
	private void addNewLines(int count) {
		for (int i = 0; i<count; i++) {
			this.HTMLContents.append("</br>\n");
		}
	}
	
	private void listStoryPieces() {
		StoryPieceManager.getInstance().sortStoryPieces();
		for (StoryPiece sp : StoryPieceManager.getInstance().getAllStoryPieces()) {
			this.HTMLContents.append(String.format("<b><a name=\"%s\">%s.</a></b>", sp.getOrder(), sp.getOrder()));
			addNewLines(1);
			this.HTMLContents.append(sp.getStory());
			addNewLines(2);
			
			for (StoryPiece choice : sp.getChoicesTexts().keySet()) {
				String choiceText = sp.getChoicesTexts().get(choice);
				this.HTMLContents.append(String.format("%s %s <i><a href=#%s>%s</a></i>", choiceText, Settings.getInstance().getChoiceNumberSeparator(),
						choice.getOrder(), choice.getOrder()));
				addNewLines(1);
			}
			// Additional space after the list of choice
			if (sp.getChoicesTexts().size() != 0) {
				addNewLines(1);
			}

		}
	}
}
