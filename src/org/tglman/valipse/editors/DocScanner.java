package org.tglman.valipse.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
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

public class DocScanner extends RuleBasedScanner {

	String[] section = { "short_description", "see_also", "stability", "include" };

	String[] function = { "Returns", "Deprecated", "Since" };

	String[] docbook = { "link", "function", "example", "title", "programlisting", "informalexample", "itemizedlist", "listitem", "para", "note", "type",
			"structname", "structfield", "classname", "emphasis", "filename" };

	public DocScanner(ColorManager manager) {
		IToken def = new Token(new TextAttribute(manager.getColor(ColorManager.GTKDOC_DEFAULT)));
		IToken tag = new Token(new TextAttribute(manager.getColor(ColorManager.GTKDOC_TAG), null, SWT.BOLD));
		IToken xml = new Token(new TextAttribute(manager.getColor(ColorManager.GTKDOC_DOCBOOK), null, SWT.BOLD));
		IToken other = new Token(new TextAttribute(manager.getColor(ColorManager.GTKDOC_OTHER), null, SWT.BOLD));

		setDefaultReturnToken(def);

		ArrayList<IRule> rules = new ArrayList<IRule>();

		// Rule for whitespaces
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
			@Override
			public boolean isWhitespace(char c) {
				return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
			}
		}));

		// Rule for tags
		WordRule tagWordRule = new WordRule(new GTKDocWordDetector('@'), tag);
		for (String s : section)
			tagWordRule.addWord(s, tag);
		rules.add(tagWordRule);

		// Rule for constants
		WordRule constantWordRule = new WordRule(new GTKDocWordDetector('%'), tag);
		rules.add(constantWordRule);

		// Rule for symbols
		WordRule symbolWordRule = new WordRule(new GTKDocWordDetector('#'), tag);
		rules.add(symbolWordRule);

		// Rules for docbook tags
		for (String d : docbook) {
			rules.add(new SingleLineRule(String.format("<%s", d), ">", xml));
			rules.add(new SingleLineRule(String.format("</%s", d), ">", xml));
		}

		// Rules for function tags
		for (String f : function)
			rules.add(new SingleLineRule(String.format("%s:", f), " ", other));

		rules.add(new SingleLineRule(" ", "()", tag));

		IRule[] r = new IRule[rules.size()];
		rules.toArray(r);
		setRules(r);
	}

	class GTKDocWordDetector implements IWordDetector {

		private char begin;

		public GTKDocWordDetector(char begin) {
			this.begin = begin;
		}

		@Override
		public boolean isWordPart(char c) {
			return (new Character(c)).toString().matches("\\w");
		}

		@Override
		public boolean isWordStart(char c) {
			return (c == begin);
		}
	}

}
