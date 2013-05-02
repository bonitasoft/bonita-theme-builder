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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;

import org.bonitasoft.theme.ThemeDescriptorManager;
import org.bonitasoft.theme.exception.InvalidThemeDescriptorDefinitionException;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.impl.ThemeDescriptorManagerImpl;
import org.bonitasoft.theme.model.Binding;
import org.bonitasoft.theme.model.ThemeDescriptor;
import org.bonitasoft.theme.model.ThemeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Christophe Leroy, Qixiang Zhang , haoran chen
 * 
 */
public class ThemeDescriptorManagerTest {

    private File tempThemeDescriptor;

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing " + "bonita.home" + " to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita");
        } else {
            System.err.println("\n\n*** " + "bonita.home" + " already set to: " + bonitaHome + " \n\n\n");
        }
    }

    @Before
    public void setUp() throws Exception {
        tempThemeDescriptor = new File("themeDescriptor.xml");
        tempThemeDescriptor.createNewFile();
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("themeDescriptor.xml");
        final InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
        final FileOutputStream fileOutputStream = new FileOutputStream(tempThemeDescriptor);
        final OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        try {
            int character = streamReader.read();
            while (character != -1) {
                streamWriter.write(character);
                character = streamReader.read();
            }
        } finally {
            streamWriter.close();
            inputStream.close();
            fileOutputStream.close();
            streamReader.close();
        }
    }

    @Test
    public void testCreateThemeDescriptorFile() throws IOException {

        final ThemeDescriptorManager themeDescriptorManager = ThemeDescriptorManagerImpl.getInstance();
        final File file = new File(ThemeDescriptorManager.THEME_DESCRIPTOR_NAME);
        file.mkdir();
        themeDescriptorManager.createThemeDescriptor("test", file);
        Assert.assertTrue(new File(file.getPath()).exists());
    }

    @Test
    public void testGetThemeDescriptor() throws ThemeDescriptorNotFoundException {

        final ThemeDescriptorManager themeDescriptorManager = ThemeDescriptorManagerImpl.getInstance();
        final URL url = Thread.currentThread().getContextClassLoader().getResource("themeDescriptor.xml");
        final File descriptor = new File(url.getFile());
        Assert.assertTrue(descriptor.exists());
        final ThemeDescriptor themeDescriptor = themeDescriptorManager.getThemeDescriptor(descriptor);
        Assert.assertNotNull(themeDescriptor);
    }

    @Test
    public void testUpdateThemeDescriptorFile() throws ThemeDescriptorNotFoundException, InvalidThemeDescriptorDefinitionException, IOException {
        final URL url = Thread.currentThread().getContextClassLoader().getResource("themeDescriptor.xml");
        final File descriptor = new File(url.getFile());

        final ThemeDescriptor themeDescriptor = new ThemeDescriptor();
        themeDescriptor.setName("myTest");
        themeDescriptor.setDescription("desc");
        themeDescriptor.setAuthor("Qixiang Zhang");
        themeDescriptor.setThemeDescriptor(descriptor);
        themeDescriptor.setProvided(true);
        themeDescriptor.setCreationDate(20150306);
        themeDescriptor.setUpdateDate(20010906);
        themeDescriptor.setType(ThemeType.application);
        final Binding binding = new Binding("logo", "#logo", "css/bonita.css");
        final Binding binding1 = new Binding("blue color", "text in blue", "body", "css/bonita.css");
        themeDescriptor.addBinding(binding);
        themeDescriptor.addBinding(binding1);

        final File file = new File("temp");
        file.mkdir();
        ThemeDescriptor outputDescriptor = new ThemeDescriptor("test", file);
        outputDescriptor = ThemeDescriptorManagerImpl.getInstance().updateThemeDescriptor(themeDescriptor);

        Assert.assertEquals("desc", outputDescriptor.getDescription());
        Assert.assertEquals(20010906, outputDescriptor.getUpdateDate());
        Assert.assertFalse(outputDescriptor.getName().equals("test"));
        Assert.assertEquals(true, outputDescriptor.isProvided());
    }

    @Test
    public void testDeleteThemeDescriptorWithUnexistingFile() throws IOException, ThemeDescriptorNotFoundException, InvalidThemeDescriptorDefinitionException{
    	  final ThemeDescriptorManager themeDescriptorManager = ThemeDescriptorManagerImpl.getInstance();
          ThemeDescriptor themeDescriptor = null;
          final File themeDescriptorFile = new File(ThemeDescriptorManager.THEME_DESCRIPTOR_NAME);
          if (!themeDescriptorFile.exists()) {
              themeDescriptor = themeDescriptorManager.createThemeDescriptor("Mountain2", themeDescriptorFile);
          }
          themeDescriptor = themeDescriptorManager.getThemeDescriptor(themeDescriptorFile);

          themeDescriptorManager.updateThemeDescriptor(themeDescriptor);
          themeDescriptorFile.delete();
          assertFalse(themeDescriptorFile.exists());
          boolean deleted = themeDescriptorManager.deleteThemeDescriptor(themeDescriptorFile);
          assertFalse(deleted);//the file did not exists
          try{
        	  themeDescriptorManager.getThemeDescriptor(themeDescriptorFile);
        	  fail("should not found deleted themedescriptor");
          } catch (ThemeDescriptorNotFoundException e){
        	  //OK
          }
    }
    @After
    public void tearDown() {
        tempThemeDescriptor.delete();
    }

//    @Test
//    public void testCreateTheme() throws Exception {
//    	ThemeDescriptorBuilderImpl themeDescriptorBuilder = ThemeDescriptorBuilderImpl.getInstance();
//    	final ThemeDescriptorManager themeDescriptorManager = ThemeDescriptorManagerImpl.getInstance();
//        themeDescriptorBuilder.createTheme("MyTestTheme01", "", 20120906, "Qixiang Zhang", "preview.jpg", 0, true, ThemeType.userXP);
//        File descriptor = themeDescriptorBuilder.done();
//        ThemeDescriptor themeDescriptor = themeDescriptorManager.getThemeDescriptor(descriptor);
//        assertEquals("Qixiang Zhang",themeDescriptor.getAuthor());
//        assertEquals(20120906l, themeDescriptor.getCreationDate());
//        assertEquals(0l, themeDescriptor.getUpdateDate());
//        assertEquals(true, themeDescriptor.isProvided());
//        assertEquals(ThemeType.userXP, themeDescriptor.getType());
//        assertEquals("MyTestTheme01", themeDescriptor.getName());
//        assertEquals("preview.jpg", themeDescriptor.getImagePreview());
//    }
    
    @Test
    public void testDuplicateTheme() throws ThemeDescriptorNotFoundException, InvalidThemeDescriptorDefinitionException, IOException, URISyntaxException{	
    	URL resource = Thread.currentThread().getContextClassLoader().getResource("duplicated1");
    	File duplicated1Folder = new File(resource.toURI());
    	File duplicated1Descriptor = new File(duplicated1Folder,ThemeDescriptorManager.THEME_DESCRIPTOR_NAME);

    	final ThemeDescriptorManager themeDescriptorManager = ThemeDescriptorManagerImpl.getInstance();
    	ThemeDescriptor themeDescriptor = themeDescriptorManager.getThemeDescriptor(duplicated1Descriptor);
    	assertEquals("duplicated1", themeDescriptor.getName());

    	File duplicated2Folder = new File(duplicated1Folder.getParentFile(),"duplicated2");
    	copyDirectory(duplicated1Folder, duplicated2Folder);
    	
    	File duplicated2Descriptor = new File(duplicated2Folder,ThemeDescriptorManager.THEME_DESCRIPTOR_NAME);
		ThemeDescriptor themeDescriptor2 = themeDescriptorManager.getThemeDescriptor(duplicated2Descriptor);
		themeDescriptor2.setCreationDate(System.currentTimeMillis());
		themeDescriptor2.setName("duplicated2");
		themeDescriptor2.setDescription("duplicated2_desc");
		themeDescriptor2.setProvided(false);
		themeDescriptorManager.updateThemeDescriptor(themeDescriptor2);
		assertEquals("duplicated2", themeDescriptor2.getName());
		FileInputStream fis = new FileInputStream(duplicated2Descriptor);
		String fileContent = getFileContent(fis);
		fis.close();
		assertTrue(fileContent.contains("name=\"duplicated2\""));
		
    }

    
	public void copyDirectory(File srcPath, File dstPath) throws IOException {
		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String files[] = srcPath.list();
			for (int i = 0; i < files.length; i++) {
				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		} else {
			if (!srcPath.exists()) {
				System.out.println("File or directory does not exist.");
				System.exit(0);
			} else {
				InputStream in = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
	}
    
	public static String getFileContent(final InputStream fis) throws IOException {
		final StringBuilder sb = new StringBuilder();
		for (int a = fis.read(); a != -1; a = fis.read()) {
			sb.append((char) a);
		}

		fis.close();

		return sb.toString();
	}

}
