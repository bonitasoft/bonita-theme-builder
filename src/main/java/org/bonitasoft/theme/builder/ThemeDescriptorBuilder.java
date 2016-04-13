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
package org.bonitasoft.theme.builder;

import java.io.File;
import java.io.IOException;

import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.model.ThemeType;

/**
 * @author Qixiang Zhang
 * 
 */
public interface ThemeDescriptorBuilder {

    /**
     * 
     * Create theme of themeDescriptor.xml
     * 
     * @param author
     * @param date
     * @param description
     * @param name
     * @param themeDescriptor
     * @return ThemeDescriptorBuilder
     */
    // changed by haoran chen
    ThemeDescriptorBuilder createTheme(String name, String description, long creationDate, String author, String imagePreview, long updateDate, boolean provided, ThemeType type);

    /**
     * @return
     * @throws IOException
     */
    File done(File targetFile) throws IOException;

    /**
     * Update the theme file
     * @param filePath
     * @param tmpFile
     */
    void updateThemeDescriptorFile(File oldFile, File newFile) throws IOException;
    
    /**
     * Create binding in the themeDescriptor.xml file
     * 
     * @param name
     * @param description
     * @throws InvalidThemeDescriptorDefinitionException
     */
    ThemeDescriptorBuilder addBinding(String name, String description) throws InvalidThemeDescriptorDefinitionException;

    /**
     * Create css-rule in the themeDescriptor.xml file
     * 
     * @param cssRule
     * @throws InvalidThemeDescriptorDefinitionException
     */
    ThemeDescriptorBuilder addCSSRule(String cssRule) throws InvalidThemeDescriptorDefinitionException;

    /**
     * Create css-file in the themeDescriptor.xml file
     * 
     * @param cssFile
     * @throws InvalidThemeDescriptorDefinitionException
     */
    ThemeDescriptorBuilder addCSSFile(String cssFile) throws InvalidThemeDescriptorDefinitionException;

}
