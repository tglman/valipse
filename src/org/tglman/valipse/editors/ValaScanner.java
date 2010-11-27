package org.tglman.valipse.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class ValaScanner extends RuleBasedScanner {

	String[] keywords = { "abstract", "base", "break", "case", "catch", "class", "const", "construct", "continue", "default", "do", "else", "enum", "finally",
			"for", "foreach", "get", "if", "in", "interface", "is", "lock", "namespace", "new", "out", "override", "private", "protected", "public", "ref",
			"return", "set", "sizeof", "static", "struct", "switch", "this", "throw", "throws", "try", "typeof", "using", "var", "virtual", "weak", "while" };

	String[] types = { "char", "delegate", "double", "float", "int", "signal", "string", "uchar", "uint", "unichar", "void" };

	String[] constants = { "false", "null", "true" };

	public ValaScanner(ColorManager manager) {
		IToken keyword = new Token(new TextAttribute(manager.getColor(ColorManager.KEYWORD), null, SWT.BOLD));
		IToken type = new Token(new TextAttribute(manager.getColor(ColorManager.TYPE)));
		IToken string = new Token(new TextAttribute(manager.getColor(ColorManager.STRING)));
		IToken comment = new Token(new TextAttribute(manager.getColor(ColorManager.COMMENT)));
		IToken other = new Token(new TextAttribute(manager.getColor(ColorManager.DEFAULT)));

		setDefaultReturnToken(other);

		ArrayList<IRule> rules = new ArrayList<IRule>();

		// Rule for single line comment
		rules.add(new EndOfLineRule("//", comment));

		// Rule for strings
		rules.add(new SingleLineRule("\"", "\"", string));

		// Rule for whitespaces
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
			@Override
			public boolean isWhitespace(char c) {
				return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
			}
		}));

		// Rule for keywords, types and constants
		WordRule wordRule = new WordRule(new IWordDetector() {

			@Override
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierPart(c);
			}

			@Override
			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierStart(c) || c == '@';
			}
		}, other);

		for (String kw : keywords)
			wordRule.addWord(kw, keyword);
		for (String t : types)
			wordRule.addWord(t, type);
		for (String c : constants)
			wordRule.addWord(c, type);
		rules.add(wordRule);

		IRule[] r = new IRule[rules.size()];
		rules.toArray(r);
		setRules(r);
	}

}
