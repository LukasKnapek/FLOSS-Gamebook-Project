package mabufudyne.gbdesigner.core;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class ExportManager {
	
	private StringBuilder HTMLContents;
	private static ExportManager instance = new ExportManager();
	
	private ExportManager () {
		this.HTMLContents = new StringBuilder();
	}

	public static ExportManager getInstance() {
		return instance;
	}
	
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
			} catch (Exception e) {
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
		this.HTMLContents.append("\n<h1>New Adventure</h1>\n");
		addNewLines(2);
	}
	
	private void addNewLines(int count) {
		for (int i = 0; i<count; i++) {
			this.HTMLContents.append("</br>\n");
		}
	}
	// TODO: Make ExportManager take SP order from Model, not View
	
	private void listStoryPieces() {
		StoryPieceManager.getInstance().sortStoryPieces();
		//<a name="anchor"></a>
		for (StoryPiece sp : StoryPieceManager.getInstance().getAllStoryPieces()) {
			this.HTMLContents.append(String.format("<b><a name=\"%s\">%s.</a></b>", sp.getOrder(), sp.getOrder()));
			addNewLines(1);
			this.HTMLContents.append(sp.getStory());
			addNewLines(2);
			
			for (StoryPiece choice : sp.getChoicesTexts().keySet()) {
				String choiceText = sp.getChoicesTexts().get(choice);
				this.HTMLContents.append(String.format("%s ... <i><a href=#%s>%s</a></i>", choiceText, choice.getOrder(), choice.getOrder()));
				addNewLines(1);
			}
			addNewLines(1);

		}
	}
}
