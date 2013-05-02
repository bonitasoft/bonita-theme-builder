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
package org.bonitasoft.theme.css;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Romain Bioteau
 *
 */
public interface CSSProperties {

	/**
     * Load a css content
     * 
     * @param input - an InputStream to load
     * @throws IOException
     * 
     */
	void load(InputStream input) throws IOException;
	
    /**
     * List all rules of a CSSProperties. The result should be something like: div#loading, div#WidgetContainerHeaderTop,
     * .user_experience, td, .inline_block, ...
     * 
     * @return List<String>
     */
    List<String> getAllRules() ;

    /**
     * Create a rule if it does not exist. If the rule exist, update the comment only
     * 
     * @param rule the name of the rule. For example .user_experience or div#loading
     * @param comment a comment for this new rule
     */
    void addRule(String rule, String comment) ;

    /**
     * Put a key-value pair the rule 
     * 
     * @param rule the name of the rule. For example .user_experience or div#loading
     * @param key the name of the property like display, font or color
     * @param value the value of the property like none;, #FFFFFF or bold
     */
    void put(String rule, String key, String value);

    /**
     * Get a value of a css property
     * 
     * @param rule
     * @param key
     * @return the property value (like #FFFFFF or bold) or null if the propertieName does not exist
     */
    String get(String rule, String key);

    /**
     * Delete a rule
     * 
     * @param rule the rule to remove
     */
    void removeRule(String rule) ;

    /**
     * save CSSProperties in an OutputStream
     * 
     * @param output
     * @throws IOException
     */
    void save(OutputStream output) throws  IOException;
    
    /**
     * get comment of a css rule
     * 
     * @param rule
     */
    String getComment(String rule) ;
	
}
