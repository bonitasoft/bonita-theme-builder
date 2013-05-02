package org.bonitasoft.theme.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bonitasoft.theme.css.impl.CSSPropertiesImpl;



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

/**
 * @author Romain Bioteau
 * 
 */
public class CSSManager {

	private static CSSManager INSTANCE;

	private CSSManager(){

	}

	public static CSSManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new CSSManager() ; 
		}
		return INSTANCE ;
	}

	/**
	 * @param cssFile
	 * @return a instance of CSSProperties
	 * @throws IOException
	 */
	public CSSProperties createCSSPropertyFromFile(File cssFile) throws IOException{
		FileInputStream in = null ;
		try{
			in = new FileInputStream(cssFile) ;
			CSSProperties properties = new CSSPropertiesImpl() ;
			properties.load(in) ;
			return properties;
		}finally{
			if(in != null){
				in.close() ;
			}
		}

	}

}
