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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.exception.InvalidXMLDefinitionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author qixiang.zhang
 * 
 */
public abstract class AbstractXMLBuilder {
    /**
     * XML tansformer factory
     */
    protected TransformerFactory transformerFactory = TransformerFactory.newInstance();

    /**
     * document buildeer factory
     */
    protected DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    /**
     * DOM representation of the XML file to create
     */
    protected Document document;

    /**
     * the root element
     */
    protected Element rootElement;

    /**
     * The current element
     */
    protected Element currentElement;

    /**
     * Logger
     */
    protected static Logger LOGGER = Logger.getLogger(AbstractXMLBuilder.class.getName());

    public final File done(File targetFile) throws IOException {
        final File themeDescriptorDefinitionFile = createTempXMLFile();
        document.appendChild(rootElement);
        final Source source = new DOMSource(document);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Result resultat = new StreamResult(new OutputStreamWriter(outputStream, "UTF-8"));
        try {
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);

            final byte[] xmlContent = outputStream.toByteArray();
            outputStream.close();

            final FileOutputStream fileOutputStream = new FileOutputStream(themeDescriptorDefinitionFile);
            try {
                fileOutputStream.write(xmlContent);
                fileOutputStream.flush();
            } finally {
                fileOutputStream.close();
            }
        } catch (final TransformerException e) {
            LOGGER.log(Level.SEVERE, "Error while generating the forms definition file.", e);
        }
        FileUtils.copyFile(themeDescriptorDefinitionFile, targetFile);
        if (!targetFile.exists()) {
            throw new IOException(String.format("Failed to copy %s to %s", themeDescriptorDefinitionFile.getAbsolutePath(), targetFile.getAbsolutePath()));
        }
        return targetFile;
    }

    public void updateThemeDescriptorFile(File oldFile, File newFile) throws IOException{        
        final String filePath = oldFile.getAbsolutePath();
        if (oldFile.delete()){
            final File updatedFile = new File(filePath);
            copy(newFile,updatedFile);
        }        
    }
    
    public abstract File createTempXMLFile() throws IOException;

    /**
     * Add a child element
     * 
     * @param parentElement
     * @param childName
     * @param childValue
     * @param isMandatory
     * @throws InvalidThemeDescriptorDefinitionException
     */
    protected void addChild(final Element parentElement, final String childName, final String childValue, final boolean isMandatory, final boolean isEmptyForbidden) throws InvalidXMLDefinitionException {
        if (isMandatory) {
            checkArgNotNull(childName, childValue);
        }
        if (isEmptyForbidden) {
            checkStringNotEmpty(childName, childValue);
        }
        if (childValue != null) {
            final Element element = document.createElement(childName);
            element.setTextContent(childValue);
            parentElement.appendChild(element);
        }
    }

    /**
     * Find the first element with the given tag name among an element children
     * 
     * @param parent the parent element
     * @param childName the tag name
     * @return an {@link Element} or null if there are no elements with thegiven tag name among the element's children
     */
    protected Element findChildElement(final Element parent, final String childName) {
        final NodeList nodeList = parent.getElementsByTagName(childName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element element = (Element) nodeList.item(i);
            if (element.getParentNode().isSameNode(parent)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Add an element to the stack
     * 
     * @param element
     */
    protected void push(final Element element) {
        if (element.getParentNode() == null) {
            currentElement.appendChild(element);
        }
        currentElement = element;
    }

    /**
     * Retrieve the first element in the DOM whose type is among the element types provided
     * 
     * @param elementTypes array of required element types
     * @return the first {@link Element} in the stack whose type is among the element types provided
     * @throws Exception
     * @throws InvalidThemeDescriptorDefinitionException if no element among the current element's parents has one of the
     *             required type
     */
    protected Element peek(final String[] elementTypes) throws InvalidXMLDefinitionException {
        final List<String> elementTypesList = Arrays.asList(elementTypes);
        while (currentElement.getParentNode() != null && currentElement.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
            if (elementTypesList.contains(currentElement.getNodeName())) {
                return currentElement;
            }
            currentElement = (Element) currentElement.getParentNode();
        }
        if (elementTypesList.contains(currentElement.getNodeName())) {
            return currentElement;
        } else {
            throw new InvalidXMLDefinitionException("No required element present among the parents of the current element.");
        }
    }

    /**
     * Verify that an element/attribute value is not null
     * 
     * @param name
     * @param value
     * @throws InvalidXMLDefinitionException
     */
    protected void checkArgNotNull(final String name, final Object value) throws InvalidXMLDefinitionException {
        if (value == null) {
            final String errorMessage = "The property " + name + " shouldn't be null.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new InvalidXMLDefinitionException(errorMessage);
        }
    }

    /**
     * Verify that an element/attribute value is not an empty string
     * 
     * @param name
     * @param value
     * @throws InvalidXMLDefinitionException
     */
    protected void checkStringNotEmpty(final String name, final String value) throws InvalidXMLDefinitionException {
        if (value != null && value.length() == 0) {
            final String errorMessage = "The property " + name + " shouldn't be empty.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new InvalidXMLDefinitionException(errorMessage);
        }
    }
    
    private void copy(File src, File dst) throws IOException {
        final InputStream in = new FileInputStream(src);
        final OutputStream out = new FileOutputStream(dst);
   
        // Transfer bytes from in to out
        final byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }      

}
