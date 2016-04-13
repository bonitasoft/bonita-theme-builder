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
package org.bonitasoft.theme.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.theme.builder.ThemeDescriptorBuilder;
import org.bonitasoft.theme.builder.impl.ThemeDescriptorBuilderImpl;
import org.bonitasoft.theme.css.CSSManager;
import org.bonitasoft.theme.css.CSSProperties;
import org.bonitasoft.theme.exception.CSSFileIsEmpty;
import org.bonitasoft.theme.exception.CSSFileNotFoundException;
import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.model.ThemeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author haoran.chen
 * 
 */
public class TestLookAndFeels {

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing " + "bonita.home" + " to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita");
        } else {
            System.err.println("\n\n*** " + "bonita.home" + " already set to: " + bonitaHome + " \n\n\n");
        }
    }

    public Set<String> getTagSet(File parent) throws IOException {
        final Set<String> tagSet = new HashSet<String>();
        final File[] files = getHTMLFiles(parent);
        if (files != null) {
            tagSet.add("html");
            for (final File file : files) {
                final Document doc = Jsoup.parse(file, "UTF-8");
                Elements elements = doc.getElementsByTag("body");
                if (elements.size() > 0) {
                    final Element bodyElement = elements.get(0);
                    elements = bodyElement.getAllElements();
                    for (final Element e : elements) {
                        tagSet.add(e.nodeName());
                    }
                }
            }
        }
        return tagSet;
    }

    public Set<String> getIdSet(final File parent) throws IOException {
        final Set<String> idSet = new HashSet<String>();
        final File[] files = getHTMLFiles(parent);
        if (files != null) {
            for (final File file : files) {
                final Document doc = Jsoup.parse(file, "UTF-8");
                Elements elements = doc.getElementsByTag("body");
                if (elements.size() > 0) {
                    final Element bodyElement = elements.get(0);
                    elements = bodyElement.getAllElements();
                    for (final Element e : elements) {
                        final String id = e.id();
                        if (!id.equals("")) {
                            idSet.add(e.id());
                        }
                    }
                }
            }
        }
        return idSet;
    }

    public Set<String> getClassSet(final File parent) throws IOException {
        final Set<String> classSet = new HashSet<String>();
        final File[] files = getHTMLFiles(parent);
        for (final File file : files) {
            final Document doc = Jsoup.parse(file, "UTF-8");
            Elements elements = doc.getElementsByTag("body");
            if (elements.size() > 0) {
                final Element element = elements.get(0);
                elements = element.getAllElements();
                for (final Element el : elements) {
                    final Set<String> tempSet = el.classNames();
                    final int size = tempSet.size();
                    if (size > 0) {
                        for (final String str : tempSet) {
                            if (!str.equals("")) {
                                classSet.add(str);
                            }
                        }
                    }
                }
            }
        }
        return classSet;
    }

    private File[] getHTMLFiles(final File parent) {
        File[] files = null;
        if (parent != null) {
            if (parent.isDirectory()) {
                files = parent.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        final String fileName = file.getName();
                        if (file.isFile() && (fileName.endsWith(".html") || fileName.endsWith(".htm"))) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        }

        return files;
    }

    @Test
    public void testLookAndFeels() throws IOException, CSSFileNotFoundException, InvalidThemeDescriptorDefinitionException, CSSFileIsEmpty {
        final String cssTemp = "application/application/css/bonita_form_default.css";
        final String cssPath = Thread.currentThread().getContextClassLoader().getResource("grey/application/application/css/bonita_form_default.css").getFile();
        final String htmlParentPath = Thread.currentThread().getContextClassLoader().getResource("grey/html").getFile();
        final String xmlParentPath = Thread.currentThread().getContextClassLoader().getResource("grey").getFile();
        System.out.println(xmlParentPath);
        final Set<String> resultSet = new HashSet<String>();
        List<String> cssRulesList = null;
        Set<String> tempSet = null;
        File tempFile = null;
        boolean result = true;

        final ThemeDescriptorBuilder tdBuilder = ThemeDescriptorBuilderImpl.getInstance();
        tempFile = new File(cssPath);
        final CSSProperties cssProperties = CSSManager.getInstance().createCSSPropertyFromFile(tempFile) ;
        cssRulesList = cssProperties.getAllRules() ;

        tempFile = new File(htmlParentPath);
        // get tags's names from file named ".html"
        tempSet = getTagSet(tempFile);
        for (final String tag : tempSet) {
            if (cssRulesList.contains(tag)) {
                resultSet.add(tag);
            }
        }
        // get classes from file named ".html"
        tempSet = getClassSet(tempFile);
        for (final String className : tempSet) {
            if (cssRulesList.contains("." + className)) {
                resultSet.add("." + className);
            }
        }
        // get Ids from file named ".html"
        tempSet = getIdSet(tempFile);
        for (final String idName : tempSet) {
            if (cssRulesList.contains("#" + idName)) {
                resultSet.add("#" + idName);
            }
        }

        // write tags classes and ids to a file named "CssHtmlToXml.xml"
        final File xmlFile = new File(xmlParentPath, "themeDescriptor.xml");
        if (xmlFile.exists()) {
            xmlFile.delete();
        }
        tdBuilder.createTheme("theme", "", 0, "haoran chen", "", 0, false, ThemeType.application);
        for (final String s : resultSet) {
            if (s.startsWith(".") || s.startsWith("#")) {
                final String temp = s.substring(1);
                tdBuilder.addBinding(temp, "dsc" + temp);
                tdBuilder.addCSSRule(s);
                tdBuilder.addCSSFile(cssTemp);
            } else {
                tdBuilder.addBinding(s, "dsc" + s);
                tdBuilder.addCSSRule(s);
                tdBuilder.addCSSFile(cssTemp);
            }
        }
        tdBuilder.done(xmlFile);

        // parse xml and Check the correctness
        final File insiteXML = new File(xmlParentPath, "themeDescriptor.xml");
        final StringBuilder file = new StringBuilder();
        try {
            final InputStream ips = new FileInputStream(insiteXML);
            final InputStreamReader ipsr = new InputStreamReader(ips);
            final BufferedReader br = new BufferedReader(ipsr);
            String line;
            while ((line = br.readLine()) != null) {
                file.append(line + "\n");
            }
            br.close();
        } catch (final Exception e) {
            System.out.println(e.toString());
        }

        for (final String st : resultSet) {
            if (!file.toString().contains(st)) {
                result = false;
                break;
            }
        }
        Assert.assertTrue(result);
    }

}
