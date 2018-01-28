package mabufudyne.gbdesigner.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import mabufudyne.gbdesigner.core.EventHandler;
import mabufudyne.gbdesigner.core.StoryPiece;
import mabufudyne.gbdesigner.core.StoryPieceManager;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ChoiceWindow {

	private static ChoiceWindow instance = new ChoiceWindow();
	protected Shell shell;
	private Table tableChoiceSelections;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));
		
		tableChoiceSelections = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tableChoiceSelections.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableChoiceSelections.setHeaderVisible(true);
		tableChoiceSelections.setLinesVisible(true);
		
		TableColumn tblclmnChoiceNumber = new TableColumn(tableChoiceSelections, SWT.NONE);
		tblclmnChoiceNumber.setWidth(30);
		tblclmnChoiceNumber.setText("#");
		
		TableColumn tblclmnChoiceTitle = new TableColumn(tableChoiceSelections, SWT.NONE);
		tblclmnChoiceTitle.setWidth(100);
		tblclmnChoiceTitle.setText("StoryPiece Title");
		
		Composite cMain = new Composite(shell, SWT.NONE);
		cMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		cMain.setLayout(new GridLayout(2, false));
		
		Button btnAddSelectedChoice = new Button(cMain, SWT.NONE);
		btnAddSelectedChoice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPiece chosenSP = null;
				Object choice = tableChoiceSelections.getSelection()[0].getData();
				if (choice instanceof StoryPiece) {
					chosenSP = (StoryPiece) choice;
					EventHandler.addChoice(chosenSP);
				}
				else {
					chosenSP = EventHandler.createNewStoryPiece();
					EventHandler.addChoice(chosenSP);
					shell.close();
				}
			}
		});
		btnAddSelectedChoice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddSelectedChoice.setBounds(0, 0, 79, 28);
		btnAddSelectedChoice.setText("Add Choice");
		
		Button btnCancelSelection = new Button(cMain, SWT.NONE);
		btnCancelSelection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			} 
		});
		btnCancelSelection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCancelSelection.setBounds(0, 0, 79, 28);
		btnCancelSelection.setText("Cancel");
		
		displayPossibleChoices();

	}
	
	public void removeChoiceFromView(StoryPiece sp) {
		for (TableItem item : tableChoiceSelections.getItems()) {
			if (item.getData() == sp) tableChoiceSelections.remove(tableChoiceSelections.indexOf(item));
		}
	}
	
	public void displayPossibleChoices() {
		StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
		ArrayList<StoryPiece> allStoryPieces = StoryPieceManager.getInstance().getAllStoryPieces();
		
		// Possible choices include SPs that are not already choices or equal to the active SP
		for (StoryPiece sp : allStoryPieces) {
			if (!activeSP.getChoicesTexts().containsKey(sp) && sp != activeSP) {
				TableItem choiceItem = new TableItem(tableChoiceSelections, SWT.CENTER);
				choiceItem.setData(sp);
				choiceItem.setText(0, "" + sp.getOrder());
				choiceItem.setText(1, sp.getTitle());
			}
			
		}
		
		// Possibility to create a new StoryPiece and add it as a choice
		TableItem item = new TableItem(tableChoiceSelections, SWT.CENTER);
		item.setData(null);
		item.setText(0, "");
		item.setText(1, "--- New StoryPiece ---");
	}
	
	public static ChoiceWindow getInstance() {	
		return instance;
	}
}
