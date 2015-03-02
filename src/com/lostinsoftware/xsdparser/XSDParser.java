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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSValue;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSInput;

/** This class parses all or only some elements of a xsd. It returns
 * XSDElements and XSDAttributes.
 * 
 * @author José Ángel Arrechea
 *
 */
public class XSDParser implements DOMErrorHandler {

  /** Parse service_builder xsd and return data of root/main element and their children.
   *  Only return information of elements previously selected in an array.
   * 
   * @return
   * @throws IOException 
   * @throws ClassCastException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws ClassNotFoundException 
   */
  public static XSDElement parseXSD(File schema, String mainElement)
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD(new FileInputStream(schema), buildHelperList(mainElement)
        , true);
  }
   
  public static XSDElement parseXSD(File schema, List<String> elements)
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return   parseXSD(new FileInputStream(schema), elements);
  }

  public static XSDElement parseXSD(URL url, String mainElement ) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD(url.openStream(), buildHelperList(mainElement), true);
  }

  public static XSDElement parseXSD(URL url, List<String> elements ) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD( url.openStream(), elements);
  }
   
  public static XSDElement parseXSD(String schemaName, String mainElement) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD(new File(schemaName), mainElement);
  }
   
  public static XSDElement parseXSD(String schemaName, List<String> elements ) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD(new File(schemaName), elements);
  }
  
  public static XSDElement parseXSD(InputStream inputStream, String mainElement) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    
     return parseXSD(inputStream,  buildHelperList(mainElement), true);
  }
  
  private static List<String> buildHelperList(String mainElement){
    List<String> elements = new ArrayList<String>();
    elements.add(mainElement);
    return elements;
  }

  public static XSDElement parseXSD(InputStream inputStream, List<String> elements) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    return parseXSD(inputStream , elements, false);
  }
  
  private static XSDElement parseXSD(InputStream inputStream, List<String> elements,
      boolean allElements) 
    throws IOException, ClassNotFoundException, 
    InstantiationException, IllegalAccessException, ClassCastException {
    
    XSDElement mainElement=null;

    DOMImplementationRegistry registry = DOMImplementationRegistry
        .newInstance();

    XSImplementation impl = (XSImplementation) registry
        .getDOMImplementation("XS-Loader");

    XSLoader schemaLoader = impl.createXSLoader(null);

    DOMConfiguration config = schemaLoader.getConfig();

    // create Error Handler
    DOMErrorHandler errorHandler = new XSDParser();

    // set error handler
    config.setParameter("error-handler", errorHandler);

    // set validation feature
    config.setParameter("validate", Boolean.TRUE);

    // parse document
    LSInput input = new DOMInputImpl( );
    input.setByteStream(inputStream);
    XSModel model = schemaLoader.load(input);
    if (model != null) {
      // Main element
      XSElementDeclaration element = model.getElementDeclaration(elements.get(0)
          , null);
      mainElement = new XSDElement();
      mainElement.setName(elements.get(0));
      mainElement.setXsDeclaration(element);
      processElement(mainElement, elements, allElements);
    }
    return mainElement;
  }
  
  private static void processElement(XSDElement xsdElement, 
      List<String> elements,  boolean allElements){
    elements(xsdElement, elements, allElements);
    attributes(xsdElement);
    // Process children
    List<XSDElement> children = xsdElement.getChildren();
    if (children!=null && children.size()>0){
      for (XSDElement child:children){
        processElement(child, elements, allElements);
      }
    }
    
  }
  
  private static void elements(XSDElement xsdElement, List<String> elements,
      boolean allElements){
    XSElementDeclaration element = xsdElement.getXsDeclaration();
    
    if (element.getTypeDefinition() instanceof XSSimpleTypeDefinition) {
      XSSimpleTypeDefinition simple =  (XSSimpleTypeDefinition) element.getTypeDefinition();
      xsdElement.setType(simple.getName());
      XSValue xsValue = element.getValueConstraintValue();
      if (xsValue !=null && !StringUtils.isBlank(xsValue.getNormalizedValue())){
        xsdElement.setDefaultValue(xsValue.getNormalizedValue());
      } else {
        xsdElement.setDefaultValue("");
      }
      return;
    } else if (element.getTypeDefinition() instanceof XSComplexTypeDecl) {
      XSComplexTypeDecl definition =  (XSComplexTypeDecl) element.getTypeDefinition();
      XSParticle particle = definition.getParticle();
      if(particle != null){
        XSTerm term = particle.getTerm();
        if(term instanceof XSModelGroup){
            XSModelGroup xsModelGroup = (XSModelGroup)term;
            XSObjectList xsol = xsModelGroup.getParticles();
            for(Object p : xsol ){
              XSParticle part = (XSParticle) p;
              XSTerm pterm = part.getTerm();
              if(pterm instanceof XSElementDeclaration){ //xs:element inside complex type
                String name = pterm.getName();
                if (allElements || elements.contains(name)) {
                  XSDElement child = new XSDElement();
                  child.setName(name);
                  child.setParent(xsdElement);
                  child.setXsDeclaration((XSElementDeclaration)pterm);
                  child.setMinOcurrs(part.getMinOccurs());
                  child.setMaxOcurrs(part.getMaxOccurs());
                  child.setMaxOcurrsUnbounded(part.getMaxOccursUnbounded());
                  xsdElement.addChildren(child);
                }
              }
            }
        }
      }
    }    
  }
  
  private static void attributes(XSDElement xsdElement){
    XSElementDeclaration element = xsdElement.getXsDeclaration();
    if ( element.getTypeDefinition() instanceof XSComplexTypeDecl) {
      XSComplexTypeDecl definition =  (XSComplexTypeDecl) element.getTypeDefinition();
      if (definition==null) return;
      XSObjectList xsol = definition.getAttributeUses();
      if (xsol!=null && xsol.getLength()>0){
        attributes:
        for (int j=0; j<xsol.getLength();j++){
          XSAttributeUseImpl attr = (XSAttributeUseImpl)xsol.item(j);
          XSValue xsValue = attr.getValueConstraintValue();
          XSAttributeDecl decl = (XSAttributeDecl)attr.getAttrDeclaration();
          if (decl==null) {
            continue attributes;
          }
          XSDAttribute attribute = new XSDAttribute();
          xsdElement.addAttribute(attribute);
          attribute.setName(decl.getName());
          attribute.setRequired(attr.getRequired());
          if (xsValue !=null && !StringUtils.isBlank(xsValue.getNormalizedValue())){
            attribute.setDefaultValue(xsValue.getNormalizedValue());
          } else {
            attribute.setDefaultValue("");
          }
          XSSimpleTypeDefinition type = decl.getTypeDefinition();
          String typeName = type==null?"":type.getName();
          attribute.setType(typeName);
          if (type instanceof XSSimpleTypeDecl) {
            ObjectList list = ((XSSimpleTypeDecl)type).getActualEnumeration();
            if (list!=null && list.getLength()>0){
              for (int k=0; k<list.getLength(); k++){
                attribute.getOptions().add((String)list.get(k));
              }
              if (attribute.isRequired()){
                attribute.setDefaultValue(attribute.getOptions().get(0));
              }
            }
          }
        }
      }
    }
  }

  @Override
  public boolean handleError(DOMError error) {
    short severity = error.getSeverity();
    if (severity == DOMError.SEVERITY_ERROR) {
      System.out.println("[xs-error]: " + error.getMessage());
    }

    if (severity == DOMError.SEVERITY_WARNING) {
      System.out.println("[xs-warning]: " + error.getMessage());
    }
    return true;
  }

}
