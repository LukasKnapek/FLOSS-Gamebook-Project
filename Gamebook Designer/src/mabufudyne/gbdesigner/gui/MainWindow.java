package mabufudyne.gbdesigner.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;

public class MainWindow {

	protected Shell shell;
	private Text textTitle;
	private Text textStory;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
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
		shell.setMinimumSize(new Point(700, 550));
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));
		
		ToolBar mainToolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		mainToolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tItemNew = new ToolItem(mainToolBar, SWT.NONE);
		tItemNew.setText("New");
		
		ToolItem tItemQuickSave = new ToolItem(mainToolBar, SWT.NONE);
		tItemQuickSave.setText("Save");
		
		SashForm sashMain = new SashForm(shell, SWT.NONE);
		sashMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashFields = new SashForm(sashMain, SWT.VERTICAL);
		
		Composite cTitle = new Composite(sashFields, SWT.NONE);
		cTitle.setLayout(new GridLayout(1, false));
		
		Label lblTitle = new Label(cTitle, SWT.NONE);
		lblTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblTitle.setText("Title");
		
		textTitle = new Text(cTitle, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Composite cStory = new Composite(sashFields, SWT.NONE);
		cStory.setLayout(new GridLayout(1, false));
		
		Label lblStory = new Label(cStory, SWT.NONE);
		lblStory.setText("Story");
		
		textStory = new Text(cStory, SWT.BORDER | SWT.MULTI);
		textStory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite cChoices = new Composite(sashFields, SWT.NONE);
		cChoices.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel = new Label(cChoices, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("Choices");
		
		List listChoices = new List(cChoices, SWT.BORDER);
		listChoices.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
		lblOverview.setText("Overview");
		
		List listStoryPieces = new List(cOverview, SWT.BORDER);
		listStoryPieces.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ToolBar sideToolBar = new ToolBar(cViews, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		sideToolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		ToolItem tItemAddStoryPiece = new ToolItem(sideToolBar, SWT.NONE);
		tItemAddStoryPiece.setText("+");
		
		ToolItem tItemRemoveStoryPiece = new ToolItem(sideToolBar, SWT.NONE);
		tItemRemoveStoryPiece.setText("-");
		sashMain.setWeights(new int[] {1, 1});

	}
}
