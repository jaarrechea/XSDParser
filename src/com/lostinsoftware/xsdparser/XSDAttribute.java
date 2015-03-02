/*******************************************************************************
* Copyright (c) 2015 José Ángel Arrechea. All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
* details.
*
* Contributors:
*          José Ángel Arrechea - initial implementation and ongoing maintenance
*          
*******************************************************************************/
package com.lostinsoftware.xsdparser;

import java.util.ArrayList;
import java.util.List;

/** Represents an "attribute" schema definition.
 * 
 * @author José Ángel Arrechea
 *
 */
public class XSDAttribute {

	private String name;
	private boolean required;
	private String type;
	private List<String> options = new ArrayList<String>();
	private String defaultValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
  public String getDefaultValue() {
    return defaultValue;
  }
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
	
	

}
