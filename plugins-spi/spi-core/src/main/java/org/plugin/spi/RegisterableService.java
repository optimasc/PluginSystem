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
 *  that will be called upon registration with a ServiceRegistry. If this 
 *  interface is present, notification of registration and deregistration 
 *  will be performed. Usually registration is made when a service
 *  is made available and deregistration when the service shall no 
 *  longer be available. This can be used to initialize and finalize
 *  data associated with the service provider. 
 *
 */
public interface RegisterableService
{
  /** Called when an object implementing this interface is removed 
   *  from the given category of the given service provider registry thus
   *  no longer being active.
   *  
   * @param category The service registry category under which this
   *   deregistration is done. 
   */
  void onDeregistration(Class category) throws ServiceProviderException;
  
  /** Called when an object implementing this interface is added to 
   *  the given category of the given service provider registry thus becoming
   *  an active service provider.
   * 
   * @param category The service registry category under which this
   *   registration is done. 
   */
  void onRegistration(Class category) throws ServiceProviderException; 


}
