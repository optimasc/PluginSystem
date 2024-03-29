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
 *  that support output as generic objects.
 * 
 * @author Carl Eric Codere
 *
 */
public interface OutputProvider
{
  /** Returns an array of Class objects indicating what types of objects that may be returned from
   *  the process method.
   *  
   * @return a non-null array of Class objects of length at least 1.
   */
  public Class[] getOutputTypes()  throws ServiceProviderException;

}
