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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.bonitasoft.theme.css.CSSManager;
import org.bonitasoft.theme.css.CSSProperties;
import org.bonitasoft.theme.exception.CSSFileIsEmpty;
import org.bonitasoft.theme.exception.CSSFileNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Christophe Leroy,haoran chen
 * 
 */
public class CSSManagerTest extends TestCase{
	private File tempCSS;

	@Before
	public void setUp() throws Exception {
		tempCSS = new File("bonitaConsole.css");
		tempCSS.createNewFile();
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("BonitaConsole.css");
		InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
		FileOutputStream fileOutputStream = new FileOutputStream(tempCSS);
		OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
		try {
			int character = streamReader.read();
			while (character != -1) {
				streamWriter.write(character);
				character = streamReader.read();
			}
		} finally {
			streamWriter.close();
			inputStream.close();
			streamReader.close();
			fileOutputStream.close();

		}
	}

	@Test
	public void testListRule() throws IOException, CSSFileNotFoundException, CSSFileIsEmpty {
	    CSSProperties cssProperties = CSSManager.getInstance().createCSSPropertyFromFile(tempCSS) ;
		List<String> rules = null;
		final String ruleName = "div#you_are_here";
		rules = cssProperties.getAllRules();
		assertTrue(rules.contains(".menu_separator"));
		assertTrue(rules.contains(ruleName));
		assertTrue(rules.contains(".menu_separator"));
		assertEquals(cssProperties.getComment(".menu_separator"),"/** Label Default (End)*/ /** Label Menu */");
			
		// delete the rule , test there isn't the rule named div#you_are_here in the list
		cssProperties.removeRule(ruleName);
		rules = cssProperties.getAllRules();
		assertTrue(!rules.contains(ruleName)) ;
		
		//Reload content from file
		FileInputStream in = new FileInputStream(tempCSS) ;
		cssProperties.load(in) ;
		in.close() ;
		rules = cssProperties.getAllRules() ;
		assertTrue(rules.contains(ruleName)) ;
		
		cssProperties.removeRule(ruleName);
		
		FileOutputStream fos = new FileOutputStream(tempCSS) ;
		cssProperties.save(fos) ; //Save delete
		fos.close() ;
		
		
		in = new FileInputStream(tempCSS) ;
		cssProperties.load(in) ;
		in.close() ;
		rules = cssProperties.getAllRules() ;
		assertTrue(!rules.contains(ruleName)) ;

		//create a new rule
		cssProperties.addRule(".empty_rule",null) ;
		rules = cssProperties.getAllRules();
		assertTrue(rules.contains(".empty_rule")) ;
		
		in = new FileInputStream(tempCSS) ;
		cssProperties.load(in) ;
		in.close() ;
		
		rules = cssProperties.getAllRules() ;
		assertTrue(!rules.contains(".empty_rule")) ;
		
		cssProperties.addRule(".empty_rule",null) ;
		
		fos = new FileOutputStream(tempCSS) ;
		cssProperties.save(fos) ; //Save delete
		fos.close() ;

		in = new FileInputStream(tempCSS) ;
		cssProperties.load(in) ;
		in.close() ;

		rules = cssProperties.getAllRules();
		assertTrue(rules.contains(".empty_rule")) ;
		

	}

	@Test
	public void testGetPropertyValue() throws IOException, CSSFileNotFoundException, CSSFileIsEmpty {
		final CSSProperties css = CSSManager.getInstance().createCSSPropertyFromFile(tempCSS) ;
		String value = null;
		value = css.get("div#you_are_here", "padding-top");
		assertEquals("3px", value) ;
	}

	@Test
	public void testUpdateRule() throws IOException, CSSFileNotFoundException, CSSFileIsEmpty {
		final CSSProperties css = CSSManager.getInstance().createCSSPropertyFromFile(tempCSS) ;
		css.addRule("div#you_are_here",null);
		css.put("div#you_are_here",  "aaaa",  "xxx") ;
		css.put("div#you_are_here",  "cccc", "tttt" ) ;
	
		FileOutputStream fos = new FileOutputStream(tempCSS) ;
		css.save(fos) ; //Save delete
		fos.close() ;
		
		// read css to find the rule added
		StringBuilder file = new StringBuilder();
		try {
			final InputStream ips = new FileInputStream(tempCSS);
			final InputStreamReader ipsr = new InputStreamReader(ips);
			final BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				file.append(line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		Assert.assertEquals(true, file.toString().contains("xxx"));

		assertTrue(file.toString().contains("aaaa")) ;
		assertTrue(file.toString().contains("cccc")) ;

		css.put("div#you_are_here",  "aaaa",  null) ;
		css.put("div#you_are_here",  "cccc", null) ;

		fos = new FileOutputStream(tempCSS) ;
		css.save(fos) ; //Save delete
		fos.close() ;

		// read css to find the rule added
		file = new StringBuilder();
		try {
			final InputStream ips = new FileInputStream(tempCSS);
			final InputStreamReader ipsr = new InputStreamReader(ips);
			final BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				file.append(line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		Assert.assertEquals(false, file.toString().contains("aaaa"));
		Assert.assertEquals(false, file.toString().contains("cccc"));
	}

	@Test
	public void testDeleteRule() throws IOException, CSSFileNotFoundException, CSSFileIsEmpty {
		final CSSProperties css = CSSManager.getInstance().createCSSPropertyFromFile(tempCSS) ;
		css.removeRule("div#loading-content");
	
		FileOutputStream fos = new FileOutputStream(tempCSS) ;
		css.save(fos) ; //Save delete
		fos.close() ;
		
		final StringBuilder file = new StringBuilder();
		try {
			final InputStream ips = new FileInputStream(tempCSS);
			final InputStreamReader ipsr = new InputStreamReader(ips);
			final BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				file.append(line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		assertFalse(file.toString().contains("div#loading-content"));
	}

	@Test
	public void testSave() throws IOException, CSSFileNotFoundException, CSSFileIsEmpty {
		final CSSProperties css = CSSManager.getInstance().createCSSPropertyFromFile(tempCSS) ;
		css.removeRule("div#loading-content");
		css.addRule("div#loading-content","/* a comment */");
		css.put("div#loading-content", "XXTT", "CCBB");
		
		FileOutputStream fos = new FileOutputStream(tempCSS) ;
		css.save(fos) ; //Save delete
		fos.close();

		final StringBuilder file = new StringBuilder();
		final InputStream ips = new FileInputStream(tempCSS);
		final InputStreamReader ipsr = new InputStreamReader(ips);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			file.append(line + "\n");
		}
		br.close();
		
		Assert.assertTrue(file.toString().contains("/* a comment */\ndiv#loading-content {\n\tXXTT: CCBB;\n}"));
	}

	@After
	public void tearDown() throws Exception {
		tempCSS.delete();
	}
}