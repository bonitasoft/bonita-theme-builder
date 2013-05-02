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
import java.util.Map;

import org.bonitasoft.theme.model.ThemeDescriptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author qixiang.zhang
 * 
 */
public class TestThemeDescriptorParse {

    @Before
    public void setUp() {
        // clear map before tests
        ThemeDescriptorParse.getInstance().getThemeDescriptors().clear();
    }

    @Test
    public void testParseAll() throws IOException {
        File themeDescriptorFile = getThemeDescriptorFile("themeDescriptor.xml");
        
        Map<String, ThemeDescriptor> themeDescriptors = ThemeDescriptorParse.parseAll(themeDescriptorFile);
        
        Assert.assertTrue(themeDescriptors.size() == 1);
    }

    private File getThemeDescriptorFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        return new File(url.getFile());
    }

}
