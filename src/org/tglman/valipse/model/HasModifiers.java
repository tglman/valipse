/* HasModifiers.java
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

import java.util.Set;

import org.tglman.valipse.model.ValaEntity.Visibility;

/**
 * Common methods on {@link ValaEntity}s which have visibility, and
 * other modifier, information.
 */
public interface HasModifiers {
	
	/**
	 * @return a list of all the modifiers on this entity.
	 */
	public Set<String> getModifiers();
	

	/**
	 * @return the visibility of this entity, or {@link Visibility#DEFAULT} if
	 *     unknown. 
	 */
	public Visibility getVisibility();
}
