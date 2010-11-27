package org.tglman.valipse.editors;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class ValaConfiguration extends SourceViewerConfiguration {

	private ColorManager colorManager;
	private ValaScanner valaScanner;
	private DocScanner docScanner;

	public ValaConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public ValaScanner getValaScanner() {
		if (valaScanner == null) {
			valaScanner = new ValaScanner(colorManager);
		}
		return valaScanner;
	}

	public DocScanner getDocScanner() {
		if (docScanner == null) {
			docScanner = new DocScanner(colorManager);
		}
		return docScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler pr = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getValaScanner());
		pr.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		pr.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getDocScanner());
		pr.setDamager(dr, ValaPartitionScanner.GTKDOC_COMMENT);
		pr.setRepairer(dr, ValaPartitionScanner.GTKDOC_COMMENT);

		RuleBasedScanner multilineScanner = new RuleBasedScanner();
		multilineScanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(ColorManager.COMMENT))));
		dr = new DefaultDamagerRepairer(multilineScanner);
		pr.setDamager(dr, ValaPartitionScanner.VALA_MULTILINE_COMMENT);
		pr.setRepairer(dr, ValaPartitionScanner.VALA_MULTILINE_COMMENT);
		return pr;
	}

	/*
	 * @Override public IContentAssistant getContentAssistant(ISourceViewer
	 * sourceViewer) { ContentAssistant ca = new ContentAssistant();
	 * IContentAssistProcessor cap = new ValaCompletionProcessor();
	 * ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
	 * ca.setInformationControlCreator
	 * (getInformationControlCreator(sourceViewer)); return ca; }
	 */

	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy[] indent = { new ValaAutoIndentStrategy() };
		return indent;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ValaPartitionScanner.VALA_MULTILINE_COMMENT };
	}
}
