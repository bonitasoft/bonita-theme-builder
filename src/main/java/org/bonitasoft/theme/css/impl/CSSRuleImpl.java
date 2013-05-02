/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.theme.css.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.theme.css.CSSRule;

/**
 * @author Romain Bioteau
 *
 */
public class CSSRuleImpl implements CSSRule {

	private String name;
	private String comment;
	

	private Map<String, String> properties;


	public CSSRuleImpl(String rule, String comment) {
		this.name = rule ;
		this.comment = comment ;
		this.properties = new HashMap<String, String>() ;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void put(String propertyName, String propertieValue) {
		this.properties.put(propertyName, propertieValue) ;
	}
	

	@Override
	public void remove(String propertyName) {
		this.properties.remove(propertyName) ;
	}


	@Override
	public String get(String propertyName) {
		return this.properties.get(propertyName) ;
	}

	@Override
	public Map<String, String> getAllProperties() {
		return Collections.unmodifiableMap(properties);
	}


	@Override
	public String getComment() {
		return comment;
	}


	@Override
	public void setComment(String comment) {
		this.comment = comment ;
	}
	
	
}
