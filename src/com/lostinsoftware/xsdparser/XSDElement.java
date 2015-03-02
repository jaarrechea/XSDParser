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

import org.apache.xerces.xs.XSElementDeclaration;

/** Represents an "element" schema definition.
 * 
 * @author José Ángel Arrechea
 *
 */
public class XSDElement {

	private String name;
	private XSElementDeclaration xsDeclaration;	
	private XSDElement parent;	
	private List<XSDAttribute> attributes = new ArrayList<XSDAttribute>();
	private List<XSDElement> children = new ArrayList<XSDElement>();
	private int minOcurrs;
	private boolean maxOcurrsUnbounded;
	private int maxOcurrs;
	private String type;
	private String defaultValue;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public XSElementDeclaration getXsDeclaration() {
		return xsDeclaration;
	}
	public void setXsDeclaration(XSElementDeclaration xsDeclaration) {
		this.xsDeclaration = xsDeclaration;
	}
	
	public XSDElement getParent() {
		return parent;
	}
	public void setParent(XSDElement parent) {
		this.parent = parent;
	}
	
	public List<XSDAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<XSDAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<XSDElement> getChildren() {
		return children;
	}
	public void setChildren(List<XSDElement> children) {
		this.children = children;
	}
	public int getMinOcurrs() {
		return minOcurrs;
	}
	public void setMinOcurrs(int minOcurrs) {
		this.minOcurrs = minOcurrs;
	}
	public boolean isMaxOcurrsUnbounded() {
		return maxOcurrsUnbounded;
	}
	public void setMaxOcurrsUnbounded(boolean maxOcurrsUnbounded) {
		this.maxOcurrsUnbounded = maxOcurrsUnbounded;
	}
	public int getMaxOcurrs() {
		return maxOcurrs;
	}
	public void setMaxOcurrs(int maxOcurrs) {
		this.maxOcurrs = maxOcurrs;
	}
	
	public void addChildren(XSDElement child) {
	  children.add(child);
	}
	
	public void addAttribute(XSDAttribute attribute) {
    attributes.add(attribute);
  }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


}
