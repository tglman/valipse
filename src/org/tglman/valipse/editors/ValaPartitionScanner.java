package org.tglman.valipse.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class ValaPartitionScanner extends RuleBasedPartitionScanner {

	public final static String VALA_MULTILINE_COMMENT = "__vala_multiline_comment";
	public final static String GTKDOC_COMMENT = "__gtkdoc_comment";

	public ValaPartitionScanner() {
		IToken valaMultilineComment = new Token(VALA_MULTILINE_COMMENT);
		IToken gtkdocComment = new Token(GTKDOC_COMMENT);

		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		// Rule for gtk-doc comments
		rules.add(new MultiLineRule("/**", "*/", gtkdocComment));

		// Rule for multi line comments
		rules.add(new MultiLineRule("/*", "*/", valaMultilineComment));

		// Rule for single line comments
		rules.add(new EndOfLineRule("//", Token.UNDEFINED));

		IPredicateRule[] r = new IPredicateRule[rules.size()];
		rules.toArray(r);
		setPredicateRules(r);
	}
}