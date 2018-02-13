package mabufudyne.gbdesigner.core;

public enum Status {
	SAVE, // Save changes to modified file before performing operation
	CANCEL, // Cancel the operation
	DISCARD, // Discard changes of modified file and perform operation
	
	INFO, // Information message in Status Bar
	WARNING, // Warning message in Status Bar
	ERROR, // Error message in Status Bar
}
