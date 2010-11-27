package org.tglman.valipse.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	public static final RGB DEFAULT = new RGB(0, 0, 0);
	public static final RGB KEYWORD = new RGB(127, 0, 85);
	public static final RGB TYPE = new RGB(64, 0, 200);
	public static final RGB STRING = new RGB(42, 0, 255);
	
	public static final RGB COMMENT = new RGB(63, 127, 95);
	
	public static final RGB GTKDOC_DEFAULT = new RGB(63, 127, 95);
	public static final RGB GTKDOC_TAG = new RGB(127, 159, 191);
	public static final RGB GTKDOC_DOCBOOK = new RGB(127, 127, 159);
	public static final RGB GTKDOC_OTHER = new RGB(63, 95, 191);

	
	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);

	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			e.next().dispose();
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
}
