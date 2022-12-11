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
 *  to return metadata information.
 * 
 * @author Carl Eric Codere
 *
 */
public interface ServiceMetadata
{
  /** Returns the name of the plugin. 
   * */
  
  /** Return the name of the service provider. 
   *
   * @param locale The locale indicating the language the value must be returned in,
   *   in case locale specific return information is not available, it should
   *   return the value in english. This value can be null to indicate to use
   *   the default locale. The value should conform to the value returned
   *   by {@link java.util.Locale#getLanguage()}.
   * @return The description of this service provider
   *  in the specified language.
   */
  public String getDescription(String locale);
  
  /** Returns the vendor of the plugin. */
  public String getVendorName();
  
  /** Returns the version of the plugin in the format X.Y.Z.FF where each elements
   *  is optional. */
  public String getVersion();
}
