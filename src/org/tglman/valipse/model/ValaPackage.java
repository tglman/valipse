/* ValaPackage.java
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

import java.io.File;


/**
 * Encapsulate information about a Vala package.
 */
public class ValaPackage {
	private final String name;
	private String       pkgConfigName;
	private File         vapiFile;
	
	
	/**
	 * Create a new instance with the given name.
	 * 
	 * @param source
	 */
	public ValaPackage(String name) {
		super();
		this.name = name;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the pkgConfigName
	 */
	public String getPkgConfigName() {
		return pkgConfigName;
	}


	/**
	 * @param pkgConfigName the pkgConfigName to set
	 */
	public void setPkgConfigName(String pkgConfigName) {
		this.pkgConfigName = pkgConfigName;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + "->" + pkgConfigName;
	}


	/**
	 * @return the vapiFile
	 */
	public File getVapiFile() {
		return vapiFile;
	}


	/**
	 * @param vapiFile the vapiFile to set
	 */
	public void setVapiFile(File vapiFile) {
		this.vapiFile = vapiFile;
	}
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object arg) {
		if (arg == null || !(arg instanceof ValaPackage))
			return false;
		
		ValaPackage other = (ValaPackage)arg;
		return name.equals(other.name) && pkgConfigName.equals(other.pkgConfigName);
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode() + (pkgConfigName != null ? pkgConfigName.hashCode() : 0);
	}
}
