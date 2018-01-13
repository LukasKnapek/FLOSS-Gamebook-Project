package mabufudyne.gbdesigner.core;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.eclipse.swt.widgets.TableItem;

import mabufudyne.gbdesigner.gui.MainWindow;

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
	
	private void listStoryPieces() {
		for (TableItem item : MainWindow.getInstance().getStoryPieceTableItems()) {
			StoryPiece sp = (StoryPiece) item.getData();
			String storyPieceOrder = item.getText(0);
			this.HTMLContents.append(String.format("<b>%s</b>.", storyPieceOrder));
			addNewLines(1);
			this.HTMLContents.append(sp.getStory());
			addNewLines(2);
			for (StoryPiece choiceSP : sp.getChoices()) {
				this.HTMLContents.append(String.format("<i>   %s ... %s</i>", choiceSP.getTitle(), MainWindow.getInstance().getStoryPieceViewOrder(choiceSP)));
				addNewLines(1);
			}
			addNewLines(1);
		}
	}
}
