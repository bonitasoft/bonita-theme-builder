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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bonitasoft.theme.ThemeDescriptorManager;
import org.bonitasoft.theme.constants.XMLThemeDescriptor;
import org.bonitasoft.theme.model.Binding;
import org.bonitasoft.theme.model.ThemeDescriptor;
import org.bonitasoft.theme.model.ThemeType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse Theme descriptor
 * 
 * @author Qixiang Zhang , haoran chen, Anthony Birembaut
 * 
 */
public class ThemeDescriptorParse {

    /**
     * Logger
     */
    protected static Logger LOGGER = Logger.getLogger(ThemeDescriptorParse.class.getName());

    /**
     * Cache all theme descriptor in a map
     */
    private static Map<String, ThemeDescriptor> themeDescriptors = new HashMap<String, ThemeDescriptor>();

    /**
     * Instance attribute
     */
    private static ThemeDescriptorParse INSTANCE = null;

    /**
     * @return the ThemeDescriptorManagerImpl instance
     */
    public static synchronized ThemeDescriptorParse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThemeDescriptorParse();
            if (themeDescriptors.size() == 0) {
                try {
                    themeDescriptors = parseAll(new File(System.getProperty("bonita.home") + File.separator + "client" + File.separator + "web" + File.separator + "XP" + File.separator + "themes"));
                } catch (final Exception e) {
                    LOGGER.log(Level.SEVERE, "Invalid theme descriptor parser configuration", e);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * parse theme descriptor file to ThemeDescriptor POJO
     * 
     * @param themeDescriptorFile
     * @return ThemeDescriptor
     */
    public static ThemeDescriptor parse(final File themeDescriptorFile) {
        ThemeDescriptor themeDescriptor = null;
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document document = builder.parse(themeDescriptorFile);
            final Element rootElement = document.getDocumentElement();
            themeDescriptor = new ThemeDescriptor();
            themeDescriptor.setName(rootElement.getAttribute(XMLThemeDescriptor.NAME));
            themeDescriptor.setDescription(rootElement.getAttribute(XMLThemeDescriptor.DESCRIPTION));
            themeDescriptor.setAuthor(rootElement.getAttribute(XMLThemeDescriptor.AUTHOR));
            themeDescriptor.setImagePreview(rootElement.getAttribute(XMLThemeDescriptor.IMAGE_PREVIEW));
            themeDescriptor.setProvided("true".equals(rootElement.getAttribute(XMLThemeDescriptor.PROVIDED)));
            themeDescriptor.setType(ThemeType.valueOf(rootElement.getAttribute(XMLThemeDescriptor.THEMETYPE)));
            final String dateStr = rootElement.getAttribute(XMLThemeDescriptor.CREATION_DATE);
            if (dateStr != null && dateStr.length() > 0) {
                final long createDate = Long.parseLong(dateStr);
                themeDescriptor.setCreationDate(createDate);
            }
            final String updateStr = rootElement.getAttribute(XMLThemeDescriptor.UPDATE_DATE);
            if (updateStr != null && updateStr.length() > 0) {
                final long updateDate = Long.parseLong(updateStr);
                themeDescriptor.setUpdateDate(updateDate);
            }
            themeDescriptor.setThemeDescriptor(themeDescriptorFile);
            final NodeList bindingNodes = rootElement.getElementsByTagName(XMLThemeDescriptor.BINDING);
            for (int i = 0; i < bindingNodes.getLength(); i++) {
                final Binding binding = new Binding();
                final Element bindingElement = (Element) bindingNodes.item(i);
                binding.setName(bindingElement.getAttribute(XMLThemeDescriptor.NAME));
                binding.setDescription(bindingElement.getAttribute(XMLThemeDescriptor.DESCRIPTION));
                final NodeList childNodes = bindingElement.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    final Node childNode = childNodes.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (childNode.getNodeName().equals(XMLThemeDescriptor.CSS_RULE)) {
                            binding.setCssRule(childNode.getTextContent());
                        } else if (childNode.getNodeName().equals(XMLThemeDescriptor.CSS_FILE)) {
                            binding.setCssFile(childNode.getTextContent());
                        }
                    }
                }
                themeDescriptor.addBinding(binding);
            }
        } catch (final ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Invalid parser configuration", e);
        } catch (final SAXException e) {
            LOGGER.log(Level.SEVERE, "Invalid parser configuration", e);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Invalid parser configuration", e);
        }

        return themeDescriptor;
    }

    /**
     * parse all theme descriptor file in to HashMap
     * 
     * @param themeDescriptorFile
     * @return
     */
    public static Map<String, ThemeDescriptor> parseAll(final File themeDescriptorFile) {
        if (themeDescriptorFile.isDirectory()) {
            final File[] files = themeDescriptorFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                parseAll(files[i]);
            }
        } else {
            if (ThemeDescriptorManager.THEME_DESCRIPTOR_NAME.equals(themeDescriptorFile.getName())) {
                final ThemeDescriptor themeDescriptor = parse(themeDescriptorFile);
                themeDescriptors.put(themeDescriptorFile.getPath(), themeDescriptor);
            }

        }
        return themeDescriptors;
    }

    public Map<String, ThemeDescriptor> getThemeDescriptors() {
        return themeDescriptors;
    }
}
