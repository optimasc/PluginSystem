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

import org.plugin.ServiceProviderException;


/** Optional interface that may be implemented by service providers 
 *  that support both input and output formats that can be configured.
 * 
 * @author Carl Eric Codere
 *
 */
public interface FormatProvider
{
  /** Returns an array of context specific output formats that is supported. The contents
   *  of this array is context specific, and could represent MIME types, image formats, etc. 
   *  depending on the context. 
   *  
   *  If several output formats are supported, the returned format array may be ordered from 
   *  the most recommended ones (lowest array indexes) to the  least recommended ones 
   *  (highest array indexes) formats to use (for performance, reliability, etc).  
   *  
   *  See interface implementation documentation for possible values. If this is not supported, the
   *  return value can be <code>null</code>.
   * 
   * @return An entry of strings representing formats or <code>null</code> if output formats cannot be configured. 
   */
  public String[] getSupportedOutputFormats()  throws ServiceProviderException;
  /** Returns an array of context specific input formats that is supported. The contents
   *  of this array is context specific, and could represent MIME types, image format, etc. depending on the
   *  context. 
   *  
   *  If several output formats are supported, the returned format array may be ordered from 
   *  the most recommended ones (lowest array indexes) to the  least recommended ones 
   *  (highest array indexes) formats to use (for performance, reliability, etc).
   *    
   *  See interface implementation documentation for possible values. If this is not supported, the return
   *  value can be <code>null</code>.
   * 
   * @return An entry of strings representing formats or <code>null</code> if input formats cannot be configured. 
   */
  public String[] getSupportedInputFormats()  throws ServiceProviderException;
  /** Configure the input format to be used when doing processing of the content. The usage of
   *  this method is context specific. Implementors may ignore it.
   * 
   * @param format The format to be set.
   * @return The format that was set, which might be the supported format that most closely matches 
   *    the one specified. Returns <code>null</code>, if the specified format is not supported and 
   *    no reasonable match could be found.
   */
  public String setInputFormat(String format)  throws ServiceProviderException;
  /** Configure the output format to be used when doing processing of the content. The usage of
   *  this method is context specific. Implementors may ignore it.
   * 
   * @param format The format to be set.
   * @return The format that was set, which might be the supported format that most closely matches 
   *    the one specified. Returns <code>null</code>, if the specified format is not supported and 
   *    no reasonable match could be found.
   */
  public String setOutputFormat(String format)  throws ServiceProviderException;
}
