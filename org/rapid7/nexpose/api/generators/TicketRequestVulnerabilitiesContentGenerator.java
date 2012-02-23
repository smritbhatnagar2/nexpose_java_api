/**
 * Copyright (C) 2010, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of the <organization> nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rapid7.nexpose.api.generators;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.rapid7.nexpose.api.IContentGenerator;
import org.rapid7.nexpose.api.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Generates Vulnerabilities content to add to a Create Ticket request.
 *
 * @author Murali Rongali
 */
public class TicketRequestVulnerabilitiesContentGenerator implements IContentGenerator
{
   /////////////////////////////////////////////////////////////////////////
   // Public methods
   /////////////////////////////////////////////////////////////////////////   
   
   /**
    * Constructs a new SaveTicketRequestVulnerabilitiesContentGenerator object.
    */
   public TicketRequestVulnerabilitiesContentGenerator()
   {
      m_vulnerabilities = new ArrayList<String>();
   }
   
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<Vulnerabilities>");
      for (String vulnerability : m_vulnerabilities)
      {
         sb.append("<Vulnerability id=\"");
         sb.append(StringUtils.xmlEscape(vulnerability));
         sb.append("\"></Vulnerability>");
      }
      sb.append("</Vulnerabilities>");
      return sb.toString();
   }
   
   /**
    * Sets the contents of the generator that come as a parameter. 
    *  
    * @param contents Elements of the xml request.
    */
   @Override
   public void setContents(Element contents)
   {
      try
      {
         final NodeList vulnerabilities = 
            (NodeList) XPathFactory.newInstance().newXPath().evaluate("Vulnerability", contents, XPathConstants.NODESET);
         for (int i = 0; i < vulnerabilities.getLength(); i++)
         {
            Element elementVulnerability = (Element) vulnerabilities.item(i);
            String vulnerability = new String(elementVulnerability.getAttribute("id"));
            m_vulnerabilities.add(vulnerability);
         }
      }
      catch (XPathExpressionException e)
      {
         System.out.println("Could not parse the Contents Generator for " +
            "TicketRequestVulnerabilitiesContentGenerator: " + e.toString());
         throw new RuntimeException("The sites could not be generated: " + e.toString());
      }
   }
   
   /**
    * Returns the list of vulnerabilities which were sent with the Create Ticket request.
    * 
    * @return The list of vulnerabilities.
    */
   public List<String> getVulnerabilities()
   {
      return m_vulnerabilities;
   }
   
   /**
    * Sets the list of vulnerabilities.
    * 
    * @param vulnerabilities The list of vulnerabilities.
    */
   public void setVulnerabilities(List<String> vulnerabilities)
   {
      m_vulnerabilities = vulnerabilities;
   }
   
   /////////////////////////////////////////////////////////////////////////
   // Non-public fields
   /////////////////////////////////////////////////////////////////////////

   /** List of vulnerabilities to be saved */
   private List<String> m_vulnerabilities;
   
}
