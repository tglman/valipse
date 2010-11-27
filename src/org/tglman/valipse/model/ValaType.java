/* ValaType.java
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Encapsulate information about a Vala class.
 */
public class ValaType extends ValaEntity {
	
	/**
	 * Dependencies: build up during analysis.
	 */
	private List<ValaType> dependencies = new ArrayList<ValaType>();
	
	private SortedSet<ValaField>  fields   = new TreeSet<ValaField>(ValaEntity.SOURCE_ORDER);
	private SortedSet<ValaMethod> methods  = new TreeSet<ValaMethod>(ValaEntity.SOURCE_ORDER);
	private Set<ValaType>         inherits = new LinkedHashSet<ValaType>();
	
	
	/**
	 * Create a new instance for the named type.
	 * 
	 * @param source
	 */
	public ValaType(String name) {
		super(name);
	}
	
	
	/**
	 * Find the method which contains the given line number.
	 * 
	 * @param type
	 * @param lineNumber
	 * @return
	 */
	public ValaMethod findMethodForLine(int lineNumber) {
		ValaMethod lastMethod = null;
		for (ValaMethod method : methods) {
			if (lastMethod != null && lineNumber >= lastMethod.getSourceReference().getLine() &&
					lineNumber < method.getSourceReference().getLine())
				return lastMethod;
			
			lastMethod = method;
		}
		
		return lastMethod;
	}


	/**
	 * @return the dependencies
	 */
	public List<ValaType> getDependencies() {
		return dependencies;
	}


	/**
	 * @return the fields
	 */
	public SortedSet<ValaField> getFields() {
		return fields;
	}


	/**
	 * @return the methods
	 */
	public SortedSet<ValaMethod> getMethods() {
		return methods;
	}


	/**
	 * @return the inherits
	 */
	public Set<ValaType> getInherits() {
		return inherits;
	}


	/**
	 * Reset the contents of this class, ready for rebuilding.
	 */
	public void reset() {
		fields.clear();
		methods.clear();
		inherits.clear();
	}
}
