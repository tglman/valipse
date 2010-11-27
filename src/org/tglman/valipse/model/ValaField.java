/* ValaField.java
 *
 * Copyright (C) 2008  Andrew Flegg <andrew@bleb.org>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.tglman.valipse.model;

import java.util.HashSet;
import java.util.Set;


/**
 * Encapsulate information about a field in a {@link ValaType}.
 */
public class ValaField extends ValaEntity implements HasModifiers {
	private Set<String>  modifiers = new HashSet<String>();
	private String       type;
	
	
	/**
	 * Create a new instance with the given name.
	 * 
	 * @param source
	 */
	public ValaField(String name) {
		super(name);
	}
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the modifiers
	 */
	public Set<String> getModifiers() {
		return modifiers;
	}
	
	
	/**
	 * @return the visibility contained in {@link #modifiers}.
	 */
	public Visibility getVisibility() {
		for (Visibility v : Visibility.values())
			if (modifiers.contains(v.toString().toLowerCase()))
				return v;
		
		return Visibility.DEFAULT;
	}
}
