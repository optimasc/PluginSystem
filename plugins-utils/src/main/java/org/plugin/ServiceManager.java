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
package org.plugin;

import java.util.Iterator;


/** Service provider and plugin manager. Singleton class
 *  that is used to manage all service providers available
 *  for a specific instance.
 *   
 * @author Carl Eric Codere
 *
 */
public class ServiceManager extends ServiceRegistry
{
  public ServiceManager(Iterator/*<Class<?>>*/ categoriesIterator)
  {
    super(categoriesIterator);
  }

  /** List of all user manually added plugins. */
  protected ServiceRegistry registeredPlugins;
  protected static ServiceManager pluginManager;
  
  /** Singleton returning the instance of the service
   *  registry.
   * 
   * @return The service registry.
   */
  public static ServiceRegistry getInstance()
  {
    if (pluginManager == null)
    {
      pluginManager = new ServiceManager(null);
    }
    return pluginManager;
  }
  
  
  
}
