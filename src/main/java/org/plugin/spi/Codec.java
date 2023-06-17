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

/** Service provider interface that represents processing of an input and converting it
 *  to a different type of output.
 * 
 * @author Carl Eric Codere
 *
 */
public interface Codec extends FormatProvider, InputOutputTypeProvider
{
  /** Given an input, this method shall convert the input and return
   *  it in output. 
   * 
   * @param input The input that needs processing and conversion
   * @param output The output that shall receive the processed and/or converted data.
   * @throws ServiceProviderException if input is not an instance of 
   *   a supported instance type.
   * @throws ServiceProviderException if output is not an instance of 
   *   a supported instance type.
   * @throws ServiceProviderException for any other errors.
   */
  void process(Object input, Object output) throws ServiceProviderException;

}
