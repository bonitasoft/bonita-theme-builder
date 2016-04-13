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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bonitasoft.theme.builder.ThemeDescriptorBuilder;
import org.bonitasoft.theme.model.ThemeType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author qixiang.zhang , haoran chen
 * 
 */
public class TestThemeDescriptorBuilderImpl {
    private ThemeDescriptorBuilder themeDescriptorBuilder;

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing BONITA_HOME to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita");
        } else {
            System.err.println("\n\n*** BONITA_HOME already set to: " + bonitaHome + " \n\n\n");
        }
    }

    @Before
    public void setUp() throws Exception {
        themeDescriptorBuilder = ThemeDescriptorBuilderImpl.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerateSimpleThemeDescriptorXML() throws Exception {

        themeDescriptorBuilder.createTheme("Mountain", "", 0, "", "", 0, false, ThemeType.application);
        final File descriptor = themeDescriptorBuilder.done(File.createTempFile("testDesc", ".xml"));
        // read themeConstructor.xml to find Mountain
        final StringBuilder file = new StringBuilder();
        try {
            final InputStream ips = new FileInputStream(descriptor);
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
        Assert.assertEquals(true, file.toString().contains("Mountain"));
    }

    @Test
    public void testGenerateThemeDescriptorXML() throws Exception {

        themeDescriptorBuilder.createTheme("Mountain", "", 20120906, "Qixiang Zhang", "", 0, false, ThemeType.application);

        themeDescriptorBuilder.addBinding("logo", "");
        themeDescriptorBuilder.addCSSRule("#logo");
        themeDescriptorBuilder.addCSSFile("bonita.css");

        themeDescriptorBuilder.addBinding("blue color", "text in blue");
        themeDescriptorBuilder.addCSSRule("body");
        themeDescriptorBuilder.addCSSFile("bonita.css");

        final File descriptor = themeDescriptorBuilder.done(File.createTempFile("testDesc", ".xml"));
        // read themeConstructor.xml to find "Qixiang Zhang"
        final StringBuilder file = new StringBuilder();
        try {
            final InputStream ips = new FileInputStream(descriptor);
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
        Assert.assertEquals(true, file.toString().contains("Qixiang Zhang"));
    }

}
