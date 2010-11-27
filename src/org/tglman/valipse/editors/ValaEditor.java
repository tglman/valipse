package org.tglman.valipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class ValaEditor extends TextEditor {

	private ColorManager colorManager;

	public ValaEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ValaConfiguration(colorManager));
		//setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		//setDocumentProvider(new XMLDocumentProvider());
		setDocumentProvider(new ValaDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
