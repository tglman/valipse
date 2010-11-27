/* ValaProject.java
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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;

/**
 * Encapsulate information about a Vala project. A project has a number
 * of source files, and indexes to other information contained therein.
 */
public class ValaProject {
	private static final Pattern NAMESPACE = Pattern.compile("^\\s*namespace (\\S+).*$");
	
	private final String name;
	private Set<ValaSource>       sources = new HashSet<ValaSource>();
	private Map<String, ValaType> types   = new HashMap<String, ValaType>();
	
	private static Map<String, Set<ValaPackage>> knownPackages = null;
	private static Map<String, ValaProject> projects = new HashMap<String, ValaProject>();
	
	
	/**
	 * Create a new instance containing no sources.
	 * 
	 * @param name Name of the project.
	 */
	public ValaProject(String name) {
		super();
		this.name = name;
		projects.put(name, this);
	}
	
	
	/**
	 * Get a previously created project, or create a new one. 
	 * 
	 * @param name Name of the project, corresponding to the name of the
	 *     project in the workspace.
	 * @return An existing, or new project.
	 */
	public synchronized static ValaProject getProject(String name) {
		if (projects.containsKey(name))
			return projects.get(name);
		
		return new ValaProject(name);
	}
	
	
	/**
	 * Get the project for the given file.
	 * 
	 * @param file
	 * @return
	 */
	public static ValaProject getProject(IFile file) {
		return getProject(file.getProject().getName());
	}
	

	/**
	 * Return the map of packages. The key corresponds to
	 * {@code using {@var Name};} in Vala source files, and the
	 * {@link ValaPackage} provides information on the VAPI
	 * file required.
	 * 
	 * @return
	 */
	public synchronized static Map<String, Set<ValaPackage>> getAvailablePackages() {
		if (knownPackages != null)
			return knownPackages;
		
		// -- Try the known locations as a fallback...
		//
		String vapiDir = "/usr/local/share/vala/vapi";
		if (!new File(vapiDir).exists())
			vapiDir = "/usr/share/vala/vapi";
		
		// -- Use the configuration if possible...
		//
		/*
		if (ValaPlugin.getDefault() != null) {
			IPreferenceStore store = ValaPlugin.getDefault().getPreferenceStore();
			vapiDir = store.getString(PreferenceConstants.P_VAPI_PATH);
		}*/
		
		// -- Read a list of all the VAPI files...
		//
		File[] vapis = new File(vapiDir).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".vapi");
			}
		});
		
		// -- Scan each one for namespaces...
		//
		knownPackages = new HashMap<String, Set<ValaPackage>>();
		for (File vapi : vapis) {
			Scanner scanner;
			try {
				scanner = new Scanner(vapi);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				continue;
			}
			
			while (scanner.hasNextLine()) {
				Matcher matcher = NAMESPACE.matcher(scanner.nextLine());
				if (matcher.matches()) {
					ValaPackage pkg = new ValaPackage(matcher.group(1));
					pkg.setPkgConfigName(vapi.getName().replaceFirst("\\.vapi$", ""));
					pkg.setVapiFile(vapi);
					
					Set<ValaPackage> providers = knownPackages.get(pkg.getName());
					if (providers == null) {
						providers = new HashSet<ValaPackage>();
						knownPackages.put(pkg.getName(), providers);
					}
					
					providers.add(pkg);
				}
			}
			scanner.close();
		}
		
		return knownPackages;
	}
	
	
	/**
	 * Does this project contain a class with the given name?
	 * 
	 * @param name Name of the class.
	 * @return true if {@link #getType(String)} has been called for
	 *       <var>name</var> previously.
	 */
	public synchronized boolean hasType(String name) {
		return types.containsKey(name);
	}
	
	
	/**
	 * Return the type definition for the given class name.
	 * This should be used to ensure that all references to a given
	 * class use the same instance. If the class is not known,
	 * an empty result is returned.
	 * 
	 * @param name Name of the class.
	 * @return Existing, or new, instance of the class.
	 */
	public synchronized ValaType getType(String name) {
		ValaType result = types.get(name);
		if (result == null) {
			result = new ValaType(name);
			types.put(name, result);
		}
		
		return result;
	}
	
	
	/**
	 * Get the {@link ValaSource} representation for the given
	 * workspace file, creating a new one if necessary.
	 * 
	 * @param file File corresponding to {@link ValaSource#getSource()}
	 * @return New or existing {@linkplain ValaSource} in this project.
	 */
	public ValaSource getSource(IFile file) {
		for (ValaSource source : sources)
			if (source.getSource().equals(file))
				return source;
		
		return new ValaSource(this, file);
	}


	/**
	 * Get the {@link ValaSource} representation containing the
	 * given type.
	 * 
	 * @param type Class to find.
	 * @return Existing {@link ValaSource} or <var>null</var> if none.
	 */
	public ValaSource getSource(ValaType type) {
		for (ValaSource source : sources) 
			if (source.getTypes().containsValue(type))
				return source;

		return null;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the sources
	 */
	public Set<ValaSource> getSources() {
		return sources;
	}


	/**
	 * @return the known global types
	 */
	public Collection<ValaType> getTypes() {
		return types.values();
	}
	
	
	/**
	 * @return a list of all packages used in this project. 
	 */
	public Set<ValaPackage> getUsedPackages() {
		Set<ValaPackage> result = new HashSet<ValaPackage>();
		
		for (ValaSource source : sources)
			result.addAll(source.getUses());

		// The GLib package is now implicit
		ValaPackage glibPackage = new ValaPackage("GLib");
		glibPackage.setPkgConfigName("gobject-2.0");
		result.add(glibPackage);
		
		return result;
	}
}
