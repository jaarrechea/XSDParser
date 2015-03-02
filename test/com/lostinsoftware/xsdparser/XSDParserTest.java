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

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/** Test class for XSDParser.
 * 
 * @author José Ángel Arrechea
 *
 */
public class XSDParserTest {

  private static final String[] elements = new String[] { "database", "author",
      "namespace", "entity", "column" };

  public static void main(String[] argv) {

    try {
      URL url = XSDParserTest.class.getClassLoader().getResource(
          "com/lostinsoftware/xsdparser/xsd/database_test.xsd");
      List<String> data = Arrays.asList(elements);
      XSDElement mainElement = XSDParser.parseXSD(url, data);
      // Print all data
      System.out.println("=============== Only some elements =======================");
      printData(mainElement, 0);

      mainElement = XSDParser.parseXSD(url, "database");
      // Print all data
      System.out.println("\n\n=============== All elements =======================");
      printData(mainElement, 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void printData(XSDElement xsdElement, int level) {

    String margin = StringUtils.repeat(" ", level);

    System.out.println(margin + "Element " + xsdElement.getName() + " ->"
        + " minOcurres=" + xsdElement.getMinOcurrs() + " maxOcurres="
        + xsdElement.getMaxOcurrs() + " unbounded="
        + xsdElement.isMaxOcurrsUnbounded() + " type=" + xsdElement.getType()
        + " default=" + xsdElement.getDefaultValue());

    for (XSDAttribute attribute : xsdElement.getAttributes()) {
      System.out.println(margin + "-- " + attribute.getName() + " ->"
          + " type=" + attribute.getType() + " required="
          + attribute.isRequired() + " default=" + attribute.getDefaultValue());
      for (String option : attribute.getOptions()) {
        System.out.println(margin + "---- " + option);
      }
    }
    if (xsdElement.getChildren().size() > 0) {
      System.out.println(margin + "Children of " + xsdElement.getName());
      for (XSDElement child : xsdElement.getChildren()) {
        printData(child, level + 2);
      }
    }

  }

}
