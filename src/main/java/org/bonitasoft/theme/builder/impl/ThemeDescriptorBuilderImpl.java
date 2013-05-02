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
package org.bonitasoft.theme.builder.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.bonitasoft.theme.builder.ThemeDescriptorBuilder;
import org.bonitasoft.theme.constants.XMLThemeDescriptor;
import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.exception.InvalidXMLDefinitionException;
import org.bonitasoft.theme.model.ThemeType;
import org.w3c.dom.Element;

/**
 * @author Qixiang Zhang , haoran chen
 * 
 */
public class ThemeDescriptorBuilderImpl extends AbstractXMLBuilder implements ThemeDescriptorBuilder {

    /**
     * Instance attribute
     */
    private static ThemeDescriptorBuilderImpl INSTANCE = null;

    /**
     * @return the FormExpressionsAPI instance
     */
    public static synchronized ThemeDescriptorBuilderImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeDescriptorBuilderImpl();
        }
        return INSTANCE;
    }

    /**
     * Private constructor to prevent instantiation
     */
    protected ThemeDescriptorBuilderImpl() {

        documentBuilderFactory.setValidating(true);

        // ignore white space can only be set if parser is validating
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        // select xml schema as the schema language (a.o.t. DTD)
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        final URL xsdURL = getClass().getResource("/themeDescriptor.xsd");
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsdURL.toExternalForm());

        try {
            transformerFactory.setAttribute("indent-number", Integer.valueOf(2));
        } catch (final Exception e) {
            // Nothing to do: indent-number is not supported
        }
    }

    /**
     * Create temp themeDescriptor.xml
     */
    public File createTempXMLFile() throws IOException {
        final String theBonitaHome = System.getProperty("bonita.home");
        final File theTempFolder = new File(theBonitaHome, File.separator + "client" + File.separator + "tmp");
        if (!theTempFolder.exists()) {
            theTempFolder.mkdirs();
        }
        final File themeDescriptorDefinitionFile = File.createTempFile("themeDescriptor", ".xml", theTempFolder);
        themeDescriptorDefinitionFile.deleteOnExit();
        return themeDescriptorDefinitionFile;
    }

    /**
     * {@inheritDoc}
     */
    public ThemeDescriptorBuilder createTheme(final String name, final String description, final long creationDate, final String author, final String imagePreview, final long updateDate, final boolean provided, final ThemeType type) {

        DocumentBuilder builder;
        try {
            builder = documentBuilderFactory.newDocumentBuilder();

            document = builder.newDocument();
            document.setXmlVersion("1.0");

            rootElement = document.createElement(XMLThemeDescriptor.THEME);
            rootElement.setAttribute(XMLThemeDescriptor.NAME, name);
            rootElement.setAttribute(XMLThemeDescriptor.DESCRIPTION, description);
            rootElement.setAttribute(XMLThemeDescriptor.CREATION_DATE, "" + creationDate);
            rootElement.setAttribute(XMLThemeDescriptor.UPDATE_DATE, "" + updateDate);
            rootElement.setAttribute(XMLThemeDescriptor.AUTHOR, author);
            rootElement.setAttribute(XMLThemeDescriptor.IMAGE_PREVIEW, imagePreview);
            rootElement.setAttribute(XMLThemeDescriptor.PROVIDED, String.valueOf(provided));
            if (type == null) {
                // set default value for type
                final ThemeType typeTemp = ThemeType.application;
                rootElement.setAttribute(XMLThemeDescriptor.THEMETYPE, typeTemp.toString());
            } else {
                rootElement.setAttribute(XMLThemeDescriptor.THEMETYPE, type.toString());
            }
            currentElement = rootElement;
        } catch (final ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Invalid parser configuration", e);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ThemeDescriptorBuilder addBinding(final String name, final String description) throws InvalidThemeDescriptorDefinitionException {
        final String[] bindingParentsNames = { XMLThemeDescriptor.THEME };
        try {
            peek(bindingParentsNames);
            checkStringNotEmpty(XMLThemeDescriptor.NAME, name);
        } catch (final InvalidXMLDefinitionException e) {
            final String errorMessage = "The addition of an binding is only supported on elements of type " + Arrays.asList(bindingParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidThemeDescriptorDefinitionException(errorMessage, e);
        }

        Element bindingsElement = findChildElement(currentElement, XMLThemeDescriptor.BINDINGS);
        if (bindingsElement == null) {
            bindingsElement = document.createElement(XMLThemeDescriptor.BINDINGS);
        }
        push(bindingsElement);
        final Element bindingElement = document.createElement(XMLThemeDescriptor.BINDING);
        bindingElement.setAttribute(XMLThemeDescriptor.NAME, name);
        bindingElement.setAttribute(XMLThemeDescriptor.DESCRIPTION, description);

        push(bindingElement);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ThemeDescriptorBuilder addCSSRule(final String cssRule) throws InvalidThemeDescriptorDefinitionException {
        final String[] cssRuleParentsNames = { XMLThemeDescriptor.BINDING };
        try {
            peek(cssRuleParentsNames);
        } catch (final InvalidXMLDefinitionException e) {
            final String errorMessage = "The addition of css-rule property is only supported on elements of type " + Arrays.asList(cssRuleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidThemeDescriptorDefinitionException(errorMessage, e);
        }
        final Element cssRuleElement = document.createElement(XMLThemeDescriptor.CSS_RULE);
        cssRuleElement.setTextContent(cssRule);
        push(cssRuleElement);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ThemeDescriptorBuilder addCSSFile(final String cssFile) throws InvalidThemeDescriptorDefinitionException {
        final String[] cssFileParentsNames = { XMLThemeDescriptor.BINDING };
        try {
            peek(cssFileParentsNames);
        } catch (final InvalidXMLDefinitionException e) {
            final String errorMessage = "The addition of css-file property is only supported on elements of type " + Arrays.asList(cssFileParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidThemeDescriptorDefinitionException(errorMessage, e);
        }
        final Element cssFileElement = document.createElement(XMLThemeDescriptor.CSS_FILE);
        cssFileElement.setTextContent(cssFile);
        push(cssFileElement);
        return this;
    }
}
