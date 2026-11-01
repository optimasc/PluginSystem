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
package com.optimasc.plugin.spi;


/** Optional interface that may be implemented by service providers 
 *  that can be configured. This interface  is a standard way for 
 *  services to be configured with the different elements / properties.
 *  
 *  Each configuration element is a name associated with an object. 
 *  The following data types are allowed for properties:
 *  The possible datatypes that are supported are as follows:
 *  <ul>
 *   <li>{@link java.lang.String} if the value of type string.</li>
 *   <li>String[] if the value must be one of the selection in this string array.</li>
 *   <li>{@link java.lang.Integer} if the value must be of type integer.</li>
 *   <li>{@link java.lang.Boolean} if the value must of type boolean.</li>
 *   <li>Integer[] if the value must be one of the integers defined in this string array.</li>
 *   <li>{@link java.net.URI} if the value must be of type URI (Java SE/CDC/Android only)</li>
 *   <li>{@link java.net.URL} if the value must be of type URL (Java SE/CDC/Android only)</li>
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
  
  /** Returns the datatype of the named property.
  *
  *  <p>The returned {@link java.lang.Class} identifies the Java type that
  *  values of this property must have when passed to
  *  {@link #setProperty(String, Object)}. The supported datatypes are:</p>
  *  <ul>
  *    <li>{@link java.lang.String} — a string value.</li>
  *    <li>{@link java.lang.Integer} — an integer value.</li>
  *    <li>{@link java.lang.Boolean} — a boolean value.</li>
  *    <li>{@link java.net.URI} — a URI value (Java SE / CDC / Android only).</li>
  *    <li>{@link java.net.URL} — a URL value (Java SE / CDC / Android only).</li>
  *  </ul>
  *
  *  <p>When the property is restricted to a fixed set of permitted values,
  *  this method still returns the <em>scalar</em> datatype of each value
  *  (for example {@link java.lang.String} or {@link java.lang.Integer});
  *  the enumeration of permitted values is obtained separately via
  *  {@link #getPropertyAllowedValues(String)}.</p>
  *
  * @param name [in] The property name to return type information for.
  *             Must not be {@code null}.
  * @return The non-null {@link java.lang.Class} representing the property's
  *         datatype.
  * @throws IllegalArgumentException If {@code name} is {@code null} or is
  *         not a recognised property name.
  */
 public Class getPropertyDatatype(String name) throws IllegalArgumentException;

 /** Returns the set of permitted values for the named property, or
  *  {@code null} if the property is not restricted to a fixed set.
  *
  *  <p>When non-null, the returned array enumerates every value that
  *  {@link #setProperty(String, Object)} will accept for this property.
  *  Each element is an instance of the property's datatype as reported by
  *  {@link #getPropertyDatatype(String)} — for example a {@code String[]}
  *  of permitted strings, or an {@code Integer[]} of permitted integers.
  *  A {@code null} return indicates the value is unconstrained within its
  *  datatype (any {@code String}, any {@code Integer}, etc.).</p>
  *
  * @param name [in] The property name to return permitted values for.
  *             Must not be {@code null}.
  * @return A non-empty array of permitted values, each assignment-compatible
  *         with {@link #getPropertyDatatype(String)}, or {@code null} if the
  *         property is not restricted to a fixed set.
  * @throws IllegalArgumentException If {@code name} is {@code null} or is
  *         not a recognised property name.
  */
 public Object[] getPropertyAllowedValues(String name) throws IllegalArgumentException;  
  
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
