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
package org.bonitasoft.theme.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christophe Leroy , haoran chen
 * 
 */
public class ThemeDescriptor {
    private String author;
    private String name;
    private String description;

    private long creationDate;
    private long updateDate;
    private String imagePreview;
    private File themeDescriptor;
    private boolean provided;
    private ThemeType type;

    /**
     * @return the type
     */
    public ThemeType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ThemeType type) {
        this.type = type;
    }

    /**
     * @return the provided
     */
    public boolean isProvided() {
        return provided;
    }

    /**
     * @param provided the provided to set
     */
    public void setProvided(boolean provided) {
        this.provided = provided;
    }

    private Map<String, Binding> bindings = new HashMap<String, Binding>();

    public ThemeDescriptor() {
        super();
    }

    /**
     * @param name
     * @param themeDescriptor
     */
    public ThemeDescriptor(String name, File themeDescriptor) {
        super();
        this.name = name;
        this.themeDescriptor = themeDescriptor;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the creationDate
     */
    public long getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the updateDate
     */
    public long getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the bindings
     */
    public Map<String, Binding> getBindings() {
        return bindings;
    }

    /**
     * Add a binding element to the themeDescriptor
     * 
     * @param name
     * @param cssRule
     * @param cssFile
     */
    public void addBinding(Binding binding) {
        this.bindings.put(binding.getName(), binding);
    }

    /**
     * Update a binding element of the themeDescriptor
     * 
     * @param name
     * @param cssRule
     * @param cssFile
     */
    public void updateBinding(Binding binding) {
        this.bindings.put(binding.getName(), binding);
    }

    /**
     * Delete a binding element of the themeDescriptor
     * 
     * @param bindingName
     */
    public void deleteBinding(String bindingName) {
        this.bindings.remove(bindingName);
    }

    /**
     * @return the imagePreview
     */
    public String getImagePreview() {
        return imagePreview;
    }

    /**
     * @param imagePreview the imagePreview to set
     */
    public void setImagePreview(String imagePreview) {
        this.imagePreview = imagePreview;
    }

    /**
     * @return the themeDescriptor
     */
    public File getThemeDescriptor() {
        return themeDescriptor;
    }

    /**
     * @param themeDescriptor the themeDescriptor to set
     */
    public void setThemeDescriptor(File themeDescriptor) {
        this.themeDescriptor = themeDescriptor;
    }

}
