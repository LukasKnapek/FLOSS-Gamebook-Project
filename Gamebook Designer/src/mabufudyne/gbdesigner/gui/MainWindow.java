package mabufudyne.gbdesigner.gui;


import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import mabufudyne.gbdesigner.core.GeneralEventHandler;
import mabufudyne.gbdesigner.core.FileEventHandler;
import mabufudyne.gbdesigner.core.MementoManager;
import mabufudyne.gbdesigner.core.StoryPiece;
import mabufudyne.gbdesigner.core.StoryPieceEventHandler;
import mabufudyne.gbdesigner.core.StoryPieceManager;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class MainWindow {

	private static MainWindow instance = new MainWindow();
	protected Shell shlGamebookDesigner;
	private Text textTitle;
	private Text textStory;
	private Grid gridStoryPieces;
	private ToolItem tItemAddStoryPiece;
	private ToolItem tItemRemoveStoryPiece;
	private ToolItem tItemAddChoice;
	private ToolItem tItemRemoveChoice;
	private ToolItem tItemUndo;
	private ToolItem tItemRedo;
	private Display display;
	private GridColumn gColFixed;
	private GridColumn gColOrder;
	private GridColumn gColTitle;
	private Spinner spinOrder;
	private Text textChoiceText;
	private Table tableChoices;
	private Button btnSaveChoiceText;
	private ToolItem tItemQuickSave;
	private TableColumn tColOrder;
	private TableColumn tColTitle;
	private TableColumn tColChoiceText; 

	/**
	 * Launch the application.
	 * @param args
	 */
	
	public static MainWindow getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			MainWindow.getInstance().open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		
		// Create an initial StoryPiece
		GeneralEventHandler.performInitialSetup();
		
		shlGamebookDesigner.open();
		shlGamebookDesigner.layout();
		
		// WINDOWS: By default, mainToolBar has focus on launch, make it lose the focus
		shlGamebookDesigner.forceFocus();
		
		while (!shlGamebookDesigner.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shlGamebookDesigner = new Shell();
		shlGamebookDesigner.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				boolean canClose = FileEventHandler.checkForUnsavedFileChanges();
				if (!canClose) e.doit = false;
			}
		});
		shlGamebookDesigner.setImage(SWTResourceManager.getImage(MainWindow.class, "/mabufudyne/gbdesigner/resources/appLogo_64x64.png"));
		shlGamebookDesigner.setMinimumSize(new Point(700, 550));
		shlGamebookDesigner.setSize(450, 300);
		shlGamebookDesigner.setText("Gamebook Designer");
		shlGamebookDesigner.setLayout(new GridLayout(1, false));
		
		
		ToolBar mainToolBar = new ToolBar(shlGamebookDesigner, SWT.FLAT | SWT.RIGHT);
		mainToolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		ToolItem tItemNew = new ToolItem(mainToolBar, SWT.NONE);
		tItemNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.createNewAdventure();
			}
		});
		tItemNew.setToolTipText("Create a new Adventure");
		tItemNew.setWidth(35);
		tItemNew.setText("New");
		
		tItemQuickSave = new ToolItem(mainToolBar, SWT.NONE);
		tItemQuickSave.setToolTipText("Save to the last loaded/saved file");
		tItemQuickSave.setWidth(35);
		tItemQuickSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.saveAdventure(true);
			}
		});
		tItemQuickSave.setText("Save");
		
		ToolItem tltmSaveAs = new ToolItem(mainToolBar, SWT.NONE);
		tltmSaveAs.setToolTipText("Save as a new file");
		tltmSaveAs.setWidth(35);
		tltmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.saveAdventure(false);
			}
		});
		tltmSaveAs.setText("Save As");
		
		ToolItem tltmLoad = new ToolItem(mainToolBar, SWT.NONE);
		tltmLoad.setToolTipText("Load Adventure from a file");
		tltmLoad.setWidth(35);
		tltmLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.loadAdventure();
			}
		});
		tltmLoad.setText("Load");
		
		tItemUndo = new ToolItem(mainToolBar, SWT.NONE);
		tItemUndo.setToolTipText("Undo action");
		tItemUndo.setWidth(35);
		tItemUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.undo();
				buttonCheck();
			}
		});
		tItemUndo.setText("Undo");
		
		tItemRedo = new ToolItem(mainToolBar, SWT.NONE);
		tItemRedo.setToolTipText("Redo action");
		tItemRedo.setWidth(35);
		tItemRedo.setSelection(true);
		tItemRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.redo();
				buttonCheck();
			}
		});
		tItemRedo.setText("Redo");
		
		ToolItem tItemExport = new ToolItem(mainToolBar, SWT.NONE);
		tItemExport.setToolTipText("Export Adventure to an HTML file");
		tItemExport.setWidth(35);
		tItemExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.exportAdventure();
			}
		});
		tItemExport.setText("Export");
		
		SashForm sashMain = new SashForm(shlGamebookDesigner, SWT.NONE);
		sashMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashFields = new SashForm(sashMain, SWT.VERTICAL);
		
		Composite cTitle = new Composite(sashFields, SWT.NONE);
		cTitle.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_1 = new Label(cTitle, SWT.NONE);
		lblNewLabel_1.setText("Order");
		
		Label lblTitle = new Label(cTitle, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblTitle.setText("Title");
		
		spinOrder = new Spinner(cTitle, SWT.NONE);
		spinOrder.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Get the new order number and the currently active SP
				int newOrder = Integer.valueOf(spinOrder.getText());
				StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
				
				StoryPieceEventHandler.changeStoryPieceOrder(activeSP, newOrder);
			}
		});
		spinOrder.setTextLimit(3);
		spinOrder.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		spinOrder.setMaximum(500);
		
		textTitle = new Text(cTitle, SWT.BORDER);
		textTitle.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String newTitle = textTitle.getText();
				StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
				StoryPieceEventHandler.changeStoryPieceTitle(activeSP, newTitle);
			}
		});
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Composite cStory = new Composite(sashFields, SWT.NONE);
		cStory.setLayout(new GridLayout(1, false));
		
		Label lblStory = new Label(cStory, SWT.NONE);
		lblStory.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblStory.setText("Story");
		
		textStory = new Text(cStory, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textStory.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String newStory = textStory.getText();
				StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
				
				StoryPieceEventHandler.changeStoryPieceStory(activeSP, newStory);
			}
		});
		textStory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite cChoices = new Composite(sashFields, SWT.NONE);
		cChoices.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(cChoices, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		lblNewLabel.setText("Choices");
		
		tableChoices = new Table(cChoices, SWT.BORDER | SWT.FULL_SELECTION);
		tableChoices.setLinesVisible(true);
		tableChoices.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				// WINDOWS: Resize columns automatically, -5 or less needed to not exceed table size
				int titleColSize = tableChoices.getSize().x - tColOrder.getWidth() - tColTitle.getWidth() - 5;
				tColChoiceText.setWidth(titleColSize);
				
			}
		});
		tableChoices.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPiece selectedChoice = (StoryPiece) tableChoices.getSelection()[0].getData();
				displayChoiceText(selectedChoice);
				buttonCheck();
			}
		});
		tableChoices.setHeaderVisible(true);
		tableChoices.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		tColOrder = new TableColumn(tableChoices, SWT.CENTER);
		tColOrder.setMoveable(true);
		tColOrder.setWidth(30);
		tColOrder.setText("#");
		
		tColTitle = new TableColumn(tableChoices, SWT.NONE);
		tColTitle.setMoveable(true);
		tColTitle.setWidth(100);
		tColTitle.setText("StoryPiece");
		
		tColChoiceText = new TableColumn(tableChoices, SWT.NONE);
		tColChoiceText.setMoveable(true);
		tColChoiceText.setWidth(100);
		tColChoiceText.setText("Choice Text");
		
		ToolBar choiceToolBar = new ToolBar(cChoices, SWT.FLAT | SWT.VERTICAL);
		choiceToolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		tItemAddChoice = new ToolItem(choiceToolBar, SWT.NONE);
		tItemAddChoice.setToolTipText("Add a new choice");
		tItemAddChoice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GeneralEventHandler.displayChoiceSelectionWindow();
			}
		});
		tItemAddChoice.setText("+");
		
		tItemRemoveChoice = new ToolItem(choiceToolBar, SWT.NONE);
		tItemRemoveChoice.setToolTipText("Remove the selected choice");
		tItemRemoveChoice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPiece choice = (StoryPiece) tableChoices.getSelection()[0].getData();
				int choiceIndex = tableChoices.getSelectionIndex();
				StoryPieceEventHandler.removeChoice(choice, choiceIndex);
			}
		});
		tItemRemoveChoice.setText("-");
		
		Label lblNewLabel_2 = new Label(cChoices, SWT.NONE);
		lblNewLabel_2.setText("Choice Text");
		
		textChoiceText = new Text(cChoices, SWT.BORDER);
		textChoiceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnSaveChoiceText = new Button(cChoices, SWT.NONE);
		btnSaveChoiceText.setToolTipText("Save as the current choice text");
		btnSaveChoiceText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPiece selectedChoice = (StoryPiece) tableChoices.getSelection()[0].getData();
				String newChoiceText = textChoiceText.getText();
				StoryPieceEventHandler.changeChoiceText(selectedChoice, newChoiceText, tableChoices.getSelectionIndex());
				tableChoices.select(tableChoices.getSelectionIndex());
			}
		});
		btnSaveChoiceText.setText("Save");
		sashFields.setWeights(new int[] {1, 3, 3});
		
		Composite cViews = new Composite(sashMain, SWT.NONE);
		GridLayout gl_cViews = new GridLayout(2, false);
		gl_cViews.marginHeight = 0;
		gl_cViews.horizontalSpacing = 0;
		cViews.setLayout(gl_cViews);
		
		Composite cOverview = new Composite(cViews, SWT.NONE);
		cOverview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cOverview.setLayout(new GridLayout(1, false));
		
		Label lblOverview = new Label(cOverview, SWT.NONE);
		lblOverview.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblOverview.setText("StoryPiece Overview");
		
		gridStoryPieces = new Grid(cOverview, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		gridStoryPieces.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail == SWT.CHECK) {
					GridItem checkedItem = (GridItem) e.item;
					StoryPieceEventHandler.changeFixedProperty((StoryPiece) checkedItem.getData());
				}
				else {
					StoryPiece sp = (StoryPiece) e.item.getData();
					StoryPieceEventHandler.changeActiveStoryPiece(sp, true);
					displayStoryPieceContents(StoryPieceManager.getInstance().getActiveStoryPiece(), 0);
				}
			}
		});
		gridStoryPieces.setAutoWidth(false);
		gridStoryPieces.addControlListener(new ControlAdapter() {
			// Resizing column width whenever the grid is resized
			@Override
			public void controlResized(ControlEvent e) {
				int titleColSize = gridStoryPieces.getSize().x - gColFixed.getWidth() - gColOrder.getWidth();
				gColTitle.setWidth(titleColSize);
			}
		});
		gridStoryPieces.setRowsResizeable(true);
		gridStoryPieces.setTreeLinesVisible(false);
		gridStoryPieces.setHeaderVisible(true);
		gridStoryPieces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		gColOrder = new GridColumn(gridStoryPieces, SWT.CENTER);
		gColOrder.setHeaderTooltip("Order of the StoryPiece");
		gColOrder.setSummary(false);
		gColOrder.setDetail(false);
		gColOrder.setMoveable(true);
		gColOrder.setText("#");
		gColOrder.setWidth(30);
		
		gColTitle = new GridColumn(gridStoryPieces, SWT.CENTER);
		gColTitle.setHeaderTooltip("StoryPiece title");
		gColTitle.setDetail(false);
		gColTitle.setSummary(false);
		gColTitle.setMoveable(true);
		gColTitle.setText("Title");
		gColTitle.setWidth(100);
		
		gColFixed = new GridColumn(gridStoryPieces, SWT.CHECK | SWT.CENTER);
		gColFixed.setHeaderTooltip("Determines if the StoryPiece order should be randomized when the randomization function is used");
		gColFixed.setMinimumWidth(50);
		gColFixed.setDetail(false);
		gColFixed.setSummary(false);
		gColFixed.setText("Fixed");
		gColFixed.setWidth(50);
		
		ToolBar sideToolBar = new ToolBar(cViews, SWT.FLAT | SWT.VERTICAL);
		sideToolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		tItemAddStoryPiece = new ToolItem(sideToolBar, SWT.NONE);
		tItemAddStoryPiece.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Lose data fields focus to trigger save of SP details
				shlGamebookDesigner.forceFocus();
				StoryPieceEventHandler.createNewStoryPieceAndActivate();
			}
		});
		tItemAddStoryPiece.setToolTipText("Create a new StoryPiece");
		tItemAddStoryPiece.setText("+");
		
		tItemRemoveStoryPiece = new ToolItem(sideToolBar, SWT.NONE);
		tItemRemoveStoryPiece.setToolTipText("Remove the selected StoryPiece");
		tItemRemoveStoryPiece.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GridItem item = gridStoryPieces.getSelection()[0];
				if (item.getData() instanceof StoryPiece) {
					StoryPieceEventHandler.deleteStoryPiece((StoryPiece) item.getData());
				}
			}
		});
		tItemRemoveStoryPiece.setText("-");
		
		ToolItem tItemRandomize = new ToolItem(sideToolBar, SWT.NONE);
		tItemRandomize.setToolTipText("Randomize StoryPiece order (except for fixed StoryPieces)");
		tItemRandomize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPieceEventHandler.randomizeStoryPieceOrder();
			}
		});
		tItemRandomize.setText("R");
		
		ToolItem tItemSort = new ToolItem(sideToolBar, SWT.NONE);
		tItemSort.setToolTipText("Sort StoryPieces by order");
		tItemSort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StoryPieceEventHandler.sortStoryPieces();
			}
		});
		tItemSort.setText("S");
		sashMain.setWeights(new int[] {2, 1});

	}

	/**
	 * Creates and displays a GridItem representing a single existing StoryPiece.
	 * @param displayedSP - The StoryPiece that should be represented by the GridItem
	 */
	public void displayStoryPieceItem(StoryPiece displayedSP) {
		GridItem item = new GridItem(gridStoryPieces, SWT.CENTER);
		
		item.setData(displayedSP);
		// Column 0 - order, column 1 - StoryPiece title, column 2 - whether the order of the StoryPiece is fixed
		item.setText(0, String.valueOf(displayedSP.getOrder()));
		item.setText(1, displayedSP.getTitle());
		item.setChecked(2, displayedSP.isFixed());
		item.setBackground(gridStoryPieces.getBackground());
	}
	
	/**
	 * Displays the fields data of the given StoryPiece
	 * @param displayedSP - The StoryPiece whose data should be displayed
	 * @param activeChoiceIndex - The index of the selected TableItem in the table of Choices of the StoryPiece
	 */
	public void displayStoryPieceContents(StoryPiece displayedSP, int activeChoiceIndex) {
		spinOrder.setSelection(displayedSP.getOrder());
		textTitle.setText(displayedSP.getTitle());
		textStory.setText(displayedSP.getStory());
		
		tableChoices.removeAll();
		displayStoryPieceChoices(displayedSP, activeChoiceIndex);
	}
	
	/**
	 * Displays all choices (StoryPieces) which can be reached from the given StoryPiece
	 * @param displayedSP - The StoryPiece whose choices should be displayd
	 * @param activeChoiceIndex - The selected choice TableItem index
	 */
	public void displayStoryPieceChoices(StoryPiece displayedSP, int activeChoiceIndex) {
		for (StoryPiece choice : displayedSP.getChoicesTexts().keySet()) {
			TableItem choiceItem = new TableItem(tableChoices, SWT.CENTER);
			String choiceText = displayedSP.getChoicesTexts().get(choice);
			// Column 0 - order, column 1 - StoryPiece title, column 2 - Choice description text
			choiceItem.setData(choice);
			choiceItem.setText(0, "" + choice.getOrder());
			choiceItem.setText(1, choice.getTitle());
			choiceItem.setText(2, choiceText);
		}
		
		if (tableChoices.getItemCount() > 0) {
			tableChoices.select(activeChoiceIndex);
			tableChoices.notifyListeners(SWT.Selection, new Event());
		}
	}

	public void removeStoryPieceItem(StoryPiece sp) {
		for (GridItem item : gridStoryPieces.getItems()) {
			if (item.getData() == sp) {
				gridStoryPieces.remove(gridStoryPieces.indexOf(item));
			}
		}
	}
	
	/**
	 * Performs a check to see which buttons/widgets should be enabled/disabled based on the current model/application state.
	 */
	public void buttonCheck() {

		if (StoryPieceManager.getInstance().canRemoveStoryPieces() && gridStoryPieces.getSelection().length != 0) {
			tItemRemoveStoryPiece.setEnabled(true);
		}
		else {
			tItemRemoveStoryPiece.setEnabled(false);
		}
		
		if (tableChoices.getItemCount() > 0 && tableChoices.getSelection().length != 0) {
			tItemRemoveChoice.setEnabled(true);
			btnSaveChoiceText.setEnabled(true);
			textChoiceText.setEnabled(true);
		}
		else {
			tItemRemoveChoice.setEnabled(false);
			btnSaveChoiceText.setEnabled(false);
			textChoiceText.setEnabled(false);
			textChoiceText.setText("");
		}
		
		tItemQuickSave.setEnabled(FileEventHandler.lastFileLocation != null);
		tItemUndo.setEnabled(MementoManager.getInstance().canUndo());
		tItemRedo.setEnabled(MementoManager.getInstance().canRedo());
		
	}

	/** 
	 * Highlights the TableItem of the currently active StoryPiece 
	 */
	public void highlightActiveStoryPiece() {
		StoryPiece activeSP = StoryPieceManager.getInstance().getActiveStoryPiece();
		for (GridItem item : gridStoryPieces.getItems()) {
			if (item.getData() == activeSP) {
				gridStoryPieces.select(gridStoryPieces.indexOf(item));
			}
		}
	}

	public void clearFields() {
		textTitle.setText("");
		textStory.setText("");
	}
	
	public void clearUI() {
		textTitle.setText("");
		textStory.setText("");
		gridStoryPieces.clearItems();
		tableChoices.removeAll();
	}

	public String invokeSaveDialog(String path) {
		FileDialog dialog = new FileDialog(shlGamebookDesigner, SWT.SAVE);
		dialog.setFilterNames(new String[] { "Adventure Files"} );
		dialog.setFilterExtensions(new String[] {"*.adv"} );
		dialog.setFilterPath(path);
		dialog.setFileName("New Adventure.adv");
		dialog.setOverwrite(true);
		
		String savePath = dialog.open();
		return savePath;
	}

	public String invokeLoadDialog(String path) {
		FileDialog dialog = new FileDialog(shlGamebookDesigner, SWT.OPEN);
		dialog.setFilterNames(new String[] { "Adventure Files"} );
		dialog.setFilterExtensions(new String[] {"*.adv"} );
		dialog.setFilterPath(path);
		
		String loadPath = dialog.open();
		return loadPath;
	}
	
	public String invokeExportDialog() {
		FileDialog dialog = new FileDialog(shlGamebookDesigner, SWT.SAVE);
		dialog.setFilterNames(new String[] { "HTML Files"} );
		dialog.setFilterExtensions(new String[] {"*.html"} );
		dialog.setFileName("Adventure.html");
		dialog.setOverwrite(true);
				
		String exportPath = dialog.open();
		return exportPath;
	}

	/**
	 * Clears the UI and then reloads all the Model data
	 */
	public void reloadUI() {
		clearUI();
		for (StoryPiece sp : StoryPieceManager.getInstance().getAllStoryPieces()) {
			displayStoryPieceItem(sp);
			if (sp == StoryPieceManager.getInstance().getActiveStoryPiece()) {
				highlightActiveStoryPiece();
				displayStoryPieceContents(sp, 0);
			}
		}
	}
	
	public void displayChoiceText(StoryPiece choice) {
		String choiceText = StoryPieceManager.getInstance().getActiveStoryPiece().getChoicesTexts().get(choice);
		textChoiceText.setText(choiceText);
	}
	
	public void changeApplicationTitle(String text) {
		shlGamebookDesigner.setText(text + " - Gamebook Designer");
	}
	
	public void showFileDirtyStatus(boolean isDirty) {
		String appTitle = shlGamebookDesigner.getText().replace("*", "");
		shlGamebookDesigner.setText(isDirty ? "*" + appTitle : appTitle);
	}

	public Shell getShell() {
		return shlGamebookDesigner;
	}
}


