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

/** Service provider interface that represents processing of an input and returning it as a 
 *  different type of output.
 * 
 * @author Carl Eric Codere
 *
 */
public interface ContentConverter extends FormatProvider, InputOutputTypeProvider
{
  /** Given an input, this method shall convert the input and creates an object that matches 
   *  the type specified. 
   * 
   * @param input The input that needs processing and conversion
   * @param outputType The class representing the object type to return
   * @return An instance of the object to be returned
   * @throws ServiceProviderException if outputType is not a supported class. 
   * @throws ServiceProviderException if input is not an instance of 
   *   a supported instance type.
   * @throws ServiceProviderException for any other errors.
   */
  Object process(Object input, Class outputType) throws ServiceProviderException;
}
