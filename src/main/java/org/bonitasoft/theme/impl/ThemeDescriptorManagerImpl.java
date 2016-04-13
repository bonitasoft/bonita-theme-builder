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
package org.bonitasoft.theme.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bonitasoft.theme.ThemeDescriptorManager;
import org.bonitasoft.theme.builder.ThemeDescriptorBuilder;
import org.bonitasoft.theme.builder.impl.ThemeDescriptorBuilderImpl;
import org.bonitasoft.theme.builder.impl.ThemeDescriptorParse;
import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.model.Binding;
import org.bonitasoft.theme.model.ThemeDescriptor;
import org.bonitasoft.theme.model.ThemeType;

/**
 * @author Christophe Leroy, Qixiang Zhang ,haoran chen
 * 
 */
public class ThemeDescriptorManagerImpl implements ThemeDescriptorManager {

    /**
     * Instance attribute
     */
    private static ThemeDescriptorManagerImpl INSTANCE = null;

    private static ThemeDescriptorParse themeDescriptorParse = null;

    /**
     * @return the ThemeDescriptorManagerImpl instance
     */
    public static synchronized ThemeDescriptorManagerImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeDescriptorManagerImpl();
            if (themeDescriptorParse == null) {
                themeDescriptorParse = ThemeDescriptorParse.getInstance();
            }
        }

        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThemeDescriptor createThemeDescriptor(final String name, final File themeDescriptorFile) throws IOException {
        final ThemeDescriptorBuilder themeDescriptorBuilder = ThemeDescriptorBuilderImpl.getInstance();
        final ThemeDescriptor themeDescriptor = new ThemeDescriptor(name, themeDescriptorFile);
        themeDescriptorBuilder.createTheme(name, "", 0, "", "", 0, false, ThemeType.application).done(themeDescriptorFile);
        themeDescriptorParse.getThemeDescriptors().put(themeDescriptorFile.getPath(), themeDescriptor);
        return themeDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThemeDescriptor updateThemeDescriptor(final ThemeDescriptor themeDescriptor) throws ThemeDescriptorNotFoundException, InvalidThemeDescriptorDefinitionException, IOException {
        final File themeDescriptorFile = themeDescriptor.getThemeDescriptor();
        if (!themeDescriptorFile.exists()) {
            throw new ThemeDescriptorNotFoundException("ThemeDescriptor file not found in the " + themeDescriptorFile.getPath());
        }
        final ThemeDescriptorBuilder themeDescriptorBuilder = ThemeDescriptorBuilderImpl.getInstance();
        themeDescriptorBuilder.createTheme(themeDescriptor.getName(), themeDescriptor.getDescription(), themeDescriptor.getCreationDate(), themeDescriptor.getAuthor(), themeDescriptor.getImagePreview(), themeDescriptor.getUpdateDate(),
                themeDescriptor.isProvided(), themeDescriptor.getType());
        for (final Map.Entry<String, Binding> entry : themeDescriptor.getBindings().entrySet()) {
            final Binding binding = entry.getValue();
            themeDescriptorBuilder.addBinding(binding.getName(), binding.getDescription());
            themeDescriptorBuilder.addCSSRule(binding.getCssRule());
            themeDescriptorBuilder.addCSSFile(binding.getCssFile());
        }        
        if (themeDescriptorFile.delete()) {
            themeDescriptorBuilder.done(themeDescriptorFile);
        }
        //  themeDescriptorBuilder.updateThemeDescriptorFile(themeDescriptorFile, themeDescriptorBuilder.done());        
        themeDescriptorParse.getThemeDescriptors().put(themeDescriptorFile.getPath(), themeDescriptor);
        return themeDescriptor;
    }  
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ThemeDescriptor getThemeDescriptor(final File themeDescriptorFile) throws ThemeDescriptorNotFoundException {
        if (!themeDescriptorFile.exists()) {
            throw new ThemeDescriptorNotFoundException("ThemeDescriptor file was not found from the path of" + themeDescriptorFile.getPath());
        }
        ThemeDescriptor themeDescriptor = null;        
        final String path = themeDescriptorFile.getPath();
        if (!themeDescriptorParse.getThemeDescriptors().containsKey(path)) {
            themeDescriptor = ThemeDescriptorParse.parse(themeDescriptorFile);
            themeDescriptorParse.getThemeDescriptors().put(themeDescriptorFile.getPath(), themeDescriptor);
        } else {
            themeDescriptor = themeDescriptorParse.getThemeDescriptors().get(themeDescriptorFile.getPath());
        }
        
        return themeDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteThemeDescriptor(final File themeDescriptorFile) throws ThemeDescriptorNotFoundException {
        if (!themeDescriptorParse.getThemeDescriptors().containsKey(themeDescriptorFile.getPath())) {
            throw new ThemeDescriptorNotFoundException(themeDescriptorFile.getPath() + "was not found");
        }
        final boolean fileExists = themeDescriptorFile.exists();
        //Not using getThemeDescriptor sinc we already checked that the map contains our theme descriptor
        final ThemeDescriptor themeDescriptor = themeDescriptorParse.getThemeDescriptors().get(themeDescriptorFile.getPath());
        final boolean provided = themeDescriptor.isProvided();
        if (!provided) {
            themeDescriptorParse.getThemeDescriptors().remove(themeDescriptorFile.getPath());
			if (fileExists) {
            	return themeDescriptorFile.delete();
            }
        }
        return false;
    }

}
