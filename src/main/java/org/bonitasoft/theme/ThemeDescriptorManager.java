/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

package org.bonitasoft.theme;

import java.io.File;
import java.io.IOException;

import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.model.ThemeDescriptor;

/**
 * 
 * It can operator the theme descriptor.
 * 
 * @author Christophe Leroy, Qixiang Zhang , haoran chen
 * @version 1.0
 */
public interface ThemeDescriptorManager {

    public final static String THEME_DESCRIPTOR_NAME = "themeDescriptor.xml";

    /**
     * Create the theme descriptor file. The name of the theme descriptor is themeDescriptor.xml
     * 
     * @param name
     * @param themeDescriptor : absolute path about the themeDescriptor.xml
     * @return ThemeDescriptor
     * @throws IOException
     */
    ThemeDescriptor createThemeDescriptor(String name, File themeDescriptorFile) throws IOException;

    /**
     * Update the theme descriptor file. The name of the theme descriptor is themeDescriptor.xml
     * 
     * @param themeDescriptor
     * @return ThemeDescriptor
     * @throws ThemeDescriptorNotFoundException
     * @throws InvalidThemeDescriptorDefinitionException
     * @throws IOException
     */
    ThemeDescriptor updateThemeDescriptor(ThemeDescriptor themeDescriptor) throws ThemeDescriptorNotFoundException, InvalidThemeDescriptorDefinitionException, IOException;

    /**
     * Get the theme descriptor bean from the theme descriptor file
     * 
     * @return ThemeDescriptor
     * @throws ThemeDescriptorNotFoundException
     */
    ThemeDescriptor getThemeDescriptor(File themeDescriptor) throws ThemeDescriptorNotFoundException;

    /**
     * Delete the theme descriptor file
     * 
     * @return true if and only if the file is successfully deleted; false otherwise
     * @throws ThemeDescriptorNotFoundException
     */
    boolean deleteThemeDescriptor(File themeDescriptor) throws ThemeDescriptorNotFoundException;
}
