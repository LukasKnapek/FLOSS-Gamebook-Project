package mabufudyne.gbdesigner.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import mabufudyne.gbdesigner.core.FileEventHandler;
import mabufudyne.gbdesigner.core.GeneralEventHandler;
import mabufudyne.gbdesigner.core.Settings;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Spinner;

public class SettingsDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text textAdventureTitle;
	private Text textChoiceNumberSeparator;
	private Text textStoryPieceTitle;
	private Text textChoiceText;
	private Spinner spinStatusTimeout;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SettingsDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		populateFields();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(544, 317);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Group grpAdventure = new Group(shell, SWT.NONE);
		GridLayout gl_grpAdventure = new GridLayout(2, false);
		gl_grpAdventure.horizontalSpacing = 50;
		grpAdventure.setLayout(gl_grpAdventure);
		grpAdventure.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAdventure.setText("Adventure");
		
		Label lblDefaultStorypieceTitle = new Label(grpAdventure, SWT.NONE);
		lblDefaultStorypieceTitle.setText("Default StoryPiece Title");
		
		textStoryPieceTitle = new Text(grpAdventure, SWT.BORDER);
		textStoryPieceTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDefaultChoiceText = new Label(grpAdventure, SWT.NONE);
		lblDefaultChoiceText.setText("Default Choice Text");
		
		textChoiceText = new Text(grpAdventure, SWT.BORDER);
		textChoiceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStatusMessageTimeout = new Label(grpAdventure, SWT.NONE);
		lblStatusMessageTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatusMessageTimeout.setToolTipText("");
		lblStatusMessageTimeout.setText("Status Message Timeout (seconds)");
		
		spinStatusTimeout = new Spinner(grpAdventure, SWT.BORDER);
		spinStatusTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		spinStatusTimeout.setMaximum(300);
		spinStatusTimeout.setMinimum(5);
		
		Group grpExport = new Group(shell, SWT.NONE);
		GridLayout gl_grpExport = new GridLayout(2, false);
		gl_grpExport.horizontalSpacing = 50;
		grpExport.setLayout(gl_grpExport);
		grpExport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpExport.setText("Export");
		
		Label lblNewLabel = new Label(grpExport, SWT.NONE);
		lblNewLabel.setText("Adventure Title");
		
		textAdventureTitle = new Text(grpExport, SWT.BORDER);
		textAdventureTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(grpExport, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Choice-Number Separator");
		
		textChoiceNumberSeparator = new Text(grpExport, SWT.BORDER);
		GridData gd_textChoiceNumberSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textChoiceNumberSeparator.heightHint = 14;
		textChoiceNumberSeparator.setLayoutData(gd_textChoiceNumberSeparator);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		Button btnApply = new Button(composite, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.saveSettings(getFieldsContents());
				FileEventHandler.saveSettingsToFile();
			}
		});
		btnApply.setText("Apply");
		
		Button btnApplyQuit = new Button(composite, SWT.NONE);
		btnApplyQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.saveSettings(getFieldsContents());
				FileEventHandler.saveSettingsToFile();
				shell.dispose();
			}
		});
		btnApplyQuit.setText("Apply and Quit");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
	}
	
	private HashMap<String, String> getFieldsContents() {
		HashMap<String, String> fieldsContents = new HashMap<String, String>();
		
		fieldsContents.put("Adventure title", textAdventureTitle.getText());
		fieldsContents.put("Choice number separator", textChoiceNumberSeparator.getText());
		fieldsContents.put("Choice text", textChoiceText.getText());
		fieldsContents.put("Status timeout", spinStatusTimeout.getText());
		fieldsContents.put("StoryPiece title", textStoryPieceTitle.getText());
		
		return fieldsContents;
	}
	
	private void populateFields() {
		textAdventureTitle.setText(Settings.getInstance().getAdventureTitle());
		textChoiceNumberSeparator.setText(Settings.getInstance().getChoiceNumberSeparator());
		textChoiceText.setText(Settings.getInstance().getDefaultChoiceText());
		textStoryPieceTitle.setText(Settings.getInstance().getDefaultStoryPieceName());
		spinStatusTimeout.setSelection(Settings.getInstance().getStatusMessageTimeout());
	}

}
