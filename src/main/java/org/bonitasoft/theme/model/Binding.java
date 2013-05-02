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

/**
 * @author Christophe Leroy
 * 
 */
public class Binding {
    private String name;
    private String description;
    private String cssRule;
    private String cssFile;

    /**
     * Default constructor.
     * 
     * @param name
     * @param cssRule
     * @param cssFile
     */
    public Binding(String name, String cssRule, String cssFile) {
        super();
        this.name = name;
        this.cssRule = cssRule;
        this.cssFile = cssFile;
    }

    /**
     * 
     * Default constructor.
     * 
     * @param name
     * @param description
     * @param cssRule
     * @param cssFile
     */
    public Binding(String name, String description, String cssRule, String cssFile) {
        super();
        this.name = name;
        this.description = description;
        this.cssRule = cssRule;
        this.cssFile = cssFile;
    }

    /**
     * Default constructor.
     */
    public Binding() {
        // TODO Auto-generated constructor stub
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
     * @return the cssRule
     */
    public String getCssRule() {
        return cssRule;
    }

    /**
     * @param cssRule the cssRule to set
     */
    public void setCssRule(String cssRule) {
        this.cssRule = cssRule;
    }

    /**
     * @return the cssFile
     */
    public String getCssFile() {
        return cssFile;
    }

    /**
     * @param cssFile the cssFile to set
     */
    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cssFile == null) ? 0 : cssFile.hashCode());
        result = prime * result + ((cssRule == null) ? 0 : cssRule.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Binding other = (Binding) obj;
        if (cssFile == null) {
            if (other.cssFile != null)
                return false;
        } else if (!cssFile.equals(other.cssFile))
            return false;
        if (cssRule == null) {
            if (other.cssRule != null)
                return false;
        } else if (!cssRule.equals(other.cssRule))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Binding [name=" + name + ", description=" + description + ", cssRule=" + cssRule + ", cssFile=" + cssFile + "]";
    }

}
