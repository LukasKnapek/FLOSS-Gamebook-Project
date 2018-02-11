package mabufudyne.gbdesigner.gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

import mabufudyne.gbdesigner.core.FileEventHandler;
import mabufudyne.gbdesigner.core.Operation;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SaveDirtyConfirmDialog extends Dialog {

	protected Object result;
	protected Shell shlUnsavedChanges;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SaveDirtyConfirmDialog(Shell parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Unsaved Changes");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlUnsavedChanges.open();
		shlUnsavedChanges.layout();
		Display display = getParent().getDisplay();
		while (!shlUnsavedChanges.isDisposed()) {
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
		shlUnsavedChanges = new Shell(getParent(), getStyle());
		shlUnsavedChanges.setImage(SWTResourceManager.getImage(SaveDirtyConfirmDialog.class, "/javax/swing/plaf/metal/icons/Error.gif"));
		shlUnsavedChanges.setSize(399, 175);
		shlUnsavedChanges.setText("Unsaved Changes");
		shlUnsavedChanges.setLayout(new GridLayout(1, false));
		
		Label lblMessageText = new Label(shlUnsavedChanges, SWT.WRAP);
		lblMessageText.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		lblMessageText.setText("Your Adventure has been modified. Do you want to save the changes before you proceed?");
		
		Composite composite = new Composite(shlUnsavedChanges, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.horizontalSpacing = 10;
		composite.setLayout(gl_composite);
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.operationState = Operation.CANCEL;
				shlUnsavedChanges.close();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 100;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnDiscard = new Button(composite, SWT.NONE);
		btnDiscard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.operationState = Operation.DISCARD;
				shlUnsavedChanges.close();
			}
		});
		GridData gd_btnDiscard = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDiscard.widthHint = 100;
		btnDiscard.setLayoutData(gd_btnDiscard);
		btnDiscard.setText("Discard");
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileEventHandler.operationState = Operation.SAVE;
				shlUnsavedChanges.close();
			}
		});
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 100;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("Save");

	}
}
