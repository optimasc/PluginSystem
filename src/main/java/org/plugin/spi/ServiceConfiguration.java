/* Copyright 2020 Optima SC Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/   
package org.plugin.spi;


/** Optional interface that may be implemented by service providers 
 *  that can be configured. This interface  is a standard way for 
 *  services to be configured with the different elements / properties.
 *  
 *  Each configuration element is a name associated with an object. 
 *  The following data types are allowed for properties:
 *  The possible datatypes that are supported are as follows:
 *  <ul>
 *   <li>{@link String} if the value of type string.</li>
 *   <li>{@link String[]} if the value must be one of the selection in this string array.</li>
 *   <li>{@link Integer} if the value must be of type integer.</li>
 *   <li>{@link Boolean} if the value must of type boolean.</li>
 *   <li>{@link Integer[]} if the value must be one of the integers defined in this string array.</li>
 *   <li>{@link URI} if the value must be of type URI (Java SE/CDC/Android only)</li>
 *   <li>{@link URL} if the value must be of type URL (Java SE/CDC/Android only)</li>
 *  </ul>
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public interface ServiceConfiguration
{
  /** Return the list of the parameters supported by this ServiceConfiguration object and for 
   *  which at least one value can be set by the application. 
   * 
   * @return The parameter names 
   */
  public String[] getParameterNames();

  /** Set the value of a parameter for this service.
   * 
   * @param name The name of the parameter to set.
   * @param value  The new value or null if the user wishes to unset the parameter. 
   *   The object type must match the type defined by the definition of the parameter. 
   * @throws IllegalArgumentException 
   *     Raised when the parameter name is not recognized.
   *     Raised when the parameter name is recognized but the requested value cannot be set.
   *     Raised if the value type for this parameter name is incompatible with the expected value type.
   */
  public void setProperty(String name, Object value) throws IllegalArgumentException;

  /** Return the value of a parameter if known. 
   * 
   * @param name The name of the parameter. 
   * @return The current object associated with the specified parameter or null if no object has been associated or if the parameter is not supported.
   * @throws IllegalArgumentException Raised when the parameter name is not recognized.
   */
  public Object getProperty(String name)  throws IllegalArgumentException;

  /** Return the property data type. Should return an instance of 
   *  the specified datatype, especially the possible options
   *  if the value returned is an array type.
   *  
   *  The possible datatypes that are supported are as follows:
   *  <ul>
   *   <li>{@link String} if the value of type string.</li>
   *   <li>{@link String[]} if the value must be one of the selection in this string array.</li>
   *   <li>{@link Integer} if the value must be of type integer.</li>
   *   <li>{@link Boolean} if the value must of type boolean.</li>
   *   <li>{@link Integer[]} if the value must be one of the integers defined in this string array.</li>
   *   <li>{@link URI} if the value must be of type URI</li>
   *   <li>{@link URL} if the value must be of type URL</li>
   *  </ul>
   * 
   * @param name The property name that we need to return information from.
   * @return
   * @throws IllegalArgumentException
   */
  public Object getPropertyDatatype(String name)   throws IllegalArgumentException;
  
  /** Returns a small help text associated with this property name or null
   *  if no help is supported. This is usually used to return a tooltip 
   *  information when configuring this property.
   * 
   * @param name The name of the property.
   * @param locale The locale indicating the language the value must be returned in,
   *   in case locale specific return information is not available, it should
   *   return the value in english. This value can be null to indicate to use
   *   the default locale. The value should conform to the value returned
   *   by {@link java.util.Locale#getLanguage()}.
   * @return The help associated with this name, in the specified locale.
   * @throws IllegalArgumentException
   */
  public String getPropertyHelp(String name, String locale)   throws IllegalArgumentException;
  
}
