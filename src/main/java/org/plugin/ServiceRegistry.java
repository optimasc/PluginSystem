/*
*  Licensed to the Apache Software Foundation (ASF) under one or more
*  contributor license agreements.  See the NOTICE file distributed with
*  this work for additional information regarding copyright ownership.
*  The ASF licenses this file to You under the Apache License, Version 2.0
*  (the "License"); you may not use this file except in compliance with
*  the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
/**
 * @author Rustem V. Rafikov, Carl Eric Codere
 */
package org.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

import org.plugin.ServiceProviderUtilities.ServiceList;
import org.plugin.spi.RegisterableService;

/**
 * The ServiceRegistry class provides ability to register, deregister, look up
 * and obtain service provider instances (SPIs). A service means a set of
 * interfaces and classes, and a service provider is an implementation of a
 * service. Service providers can be associated with one or more categories.
 * Each category is defined by a class or interface. Only a single instance of a
 * each class is allowed to be registered as a category.
 * 
 * Service providers should be lightweight and should rely on as little dependencies
 * as possible. 
 * 
 * This implementation is a generic re-implementation of the imageIO 
 * service registry.
 * 
 * @see javax.imageio.spi.ServiceRegistry
 * 
 */
public class ServiceRegistry
{
  /**
   * The categories.
   */
  CategoriesMap categories = new CategoriesMap(this);

  /**
   * Instantiates a new ServiceRegistry with the specified categories.
   * 
   * @param categoriesIterator
   *          an Iterator of Class objects for defining of categories.
   */
  public ServiceRegistry(Iterator<Class<?>> categoriesIterator)
  {
    if (null == categoriesIterator)
    {
      throw new IllegalArgumentException("categories iterator should not be NULL");
    }
    while (categoriesIterator.hasNext())
    {
      Class<?> c = categoriesIterator.next();
      categories.addCategory(c);
    }
  }

  /**
   * Looks up and instantiates the available providers of this service using the
   * specified class loader.
   * 
   * @param providerClass
   *          the Class object of the provider to be looked up.
   * @param loader
   *          the class loader to be used.
   * @return the iterator of providers objects for this service.
   */
  public static <T> Iterator<T> lookupProviders(Class<T> providerClass, ClassLoader loader)
  {
    /** List of all dynamically loaded plugins */
    ServiceLoader<T> serviceLoader =  ServiceLoader.load(providerClass);
    return serviceLoader.iterator();
  }

  /**
   * Looks up and instantiates the available providers of this service using the
   * context class loader.
   * 
   * @param providerClass
   *          the Class object of the provider to be looked up.
   * @return the iterator of providers objects for this service.
   */
  public static <T> Iterator<T> lookupProviders(Class<T> providerClass)
  {
    /** List of all dynamically loaded plugins */
    ServiceLoader<T> serviceLoader =  ServiceLoader.load(providerClass);
    return serviceLoader.iterator();
  }

  /**
   * Registers the specified service provider object in the specified
   * category.
   * 
   * @param provider
   *          the specified provider to be registered.
   * @param category
   *          the category.
   * @return true, if no provider of the same class is registered in this
   *         category, false otherwise.
   */
  public boolean registerServiceProvider(Object provider, Class<?> category)
  {
    return categories.addProvider(provider, category);
  }

  /**
   * Registers a list of service providers.
   * 
   * @param providers
   *          the list of service providers.
   */
  public void registerServiceProviders(Iterator<?> providers)
  {
    for (Iterator<?> iterator = providers; iterator.hasNext();)
    {
      categories.addProvider(iterator.next(), null);
    }
  }

  /**
   * Registers the specified service provider object in all categories. 
   * 
   * @param provider
   *          the service provider.
   */
  public void registerServiceProvider(Object provider)
  {
    categories.addProvider(provider, null);
  }

  /**
   * Deregisters the specifies service provider from the specified category.
   * 
   * @param provider
   *          the service provider to be deregistered.
   * @param category
   *          the specified category.
   * @return true, if the provider was already registered in the specified
   *         category, false otherwise.
   */
  public boolean deregisterServiceProvider(Object provider, Class<?> category)
  {
    return categories.deleteProvider(provider, category);
  }

  /**
   * Deregisters the specified service provider from all categories.
   * 
   * @param provider
   *          the specified service provider.
   */
  public void deregisterServiceProvider(Object provider)
  {
    categories.deleteProvider(provider, null);
  }

  /**
   * Gets an Iterator of registered service providers in the specified category
   * which satisfy the specified Filter. The useOrdering parameter indicates
   * whether the iterator will return all of the server provider objects in a
   * set order.
   * 
   * @param category
   *          the specified category.
   * @param filter
   *          the specified filter.
   * @param useOrdering
   *          the flag indicating that providers are ordered in the returned
   *          Iterator.
   * @return the iterator of registered service providers.
   */
  @SuppressWarnings("unchecked")
  public <T> Iterator<T> getServiceProviders(Class<T> category, Filter filter, boolean useOrdering)
  {
    return new FilteredIterator<T>(filter, (Iterator<T>) categories.getProviders(category));
  }

  /**
   * Gets an Iterator of all registered service providers in the specified
   * category. The useOrdering parameter indicates whether the iterator will
   * return all of the server provider objects in a set order.
   * 
   * @param category
   *          the specified category.
   * @param useOrdering
   *          the flag indicating that providers are ordered in the returned
   *          Iterator.
   * @return the Iterator of service providers.
   */
  @SuppressWarnings("unchecked")
  public <T> Iterator<T> getServiceProviders(Class<T> category, boolean useOrdering)
  {
    return (Iterator<T>) categories.getProviders(category);
  }
  
  
  public <T> List<T> getServiceProvidersAsList(Class<T> category)
  {
    return (List<T>) categories.getProvidersAsList(category);
  }  

  /**
   * Gets the registered service provider object that has the specified class
   * type.
   * 
   * @param providerClass
   *          the specified provider class.
   * @return the service provider object.
   */
  public <T> T getServiceProviderByClass(Class<T> providerClass)
  {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * Sets an ordering between two service provider objects within the specified
   * category.
   * 
   * @param category
   *          the specified category.
   * @param firstProvider
   *          the first provider.
   * @param secondProvider
   *          the second provider.
   * @return true, if a previously unset order was set.
   */
  public <T> boolean setOrdering(Class<T> category, T firstProvider, T secondProvider)
  {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * Unsets an ordering between two service provider objects within the
   * specified category.
   * 
   * @param category
   *          the specified category.
   * @param firstProvider
   *          the first provider.
   * @param secondProvider
   *          the second provider.
   * @return true, if a previously unset order was removed.
   */
  public <T> boolean unsetOrdering(Class<T> category, T firstProvider, T secondProvider)
  {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * Deregisters all providers from the specified category.
   * 
   * @param category
   *          the specified category.
   */
  public void deregisterAll(Class<?> category)
  {
    categories.deleteProviders(category);
  }

  /**
   * Deregister all providers from all categories.
   */
  public void deregisterAll()
  {
    Iterator<Class<?>> iterator = categories.list();
    while (iterator.hasNext())
    {
      deregisterAll(iterator.next());
    }
  }

  /**
   * Finalizes this object.
   * 
   * @throws Throwable
   *           if an error occurs during finalization.
   */
  @Override
  public void finalize() throws Throwable
  {
    deregisterAll();
  }

  /**
   * Checks whether the specified provider has been already registered.
   * 
   * @param provider
   *          the provider to be checked.
   * @return true, if the specified provider has been already registered, false
   *         otherwise.
   */
  public boolean contains(Object provider)
  {
	return categories.contains(provider);
  }
  
  /** Checks where the specified provider has been already registered
   *  in this category. This is an extension to the ServiceRegistry
   *  contract defined by the standard Java API.
   * 
   * @param category The category registry to check in.
   * @param provider The actual provider implementation
   * @return
   */
  public boolean contains(Object provider, Class<?> category)
  {
	return categories.contains(provider, category);
  }

  /**
   * Gets an iterator of Class objects representing the current categories.
   * 
   * @return the Iterator of Class objects.
   */
  public Iterator<Class<?>> getCategories()
  {
    return categories.list();
  }

  /**
   * The ServiceRegistry.Filter interface is used by
   * ServiceRegistry.getServiceProviders to filter providers according to the
   * specified critera.
   * 
   */
  public static interface Filter
  {
    /**
     * Returns true if the specified provider satisfies the criterion of this
     * Filter.
     * 
     * @param provider
     *          the provider.
     * @return true, if the specified provider satisfies the criterion of this
     *         Filter, false otherwise.
     */
    boolean filter(Object provider);
  }
  
  public static class CategoriesMap extends ServiceList
  {

    /**
     * The registry.
     */
    ServiceRegistry registry;
    
    public CategoriesMap(ServiceRegistry registry)
    {      
      super();
      this.registry = registry;
    }

    @Override
    public boolean deleteProvider(Object provider, Class<?> category)
    {
      boolean rt = super.deleteProvider(provider, category);
      if (provider instanceof RegisterableService)
      {
        try
        {
          ((RegisterableService) provider).onDeregistration(category);
        } catch (ServiceProviderException e)
        {
          e.printStackTrace();
        }
      }
      return rt;
    }

    @Override
    public boolean addProvider(Object provider, Class<?> category)
    {
      boolean rt =  super.addProvider(provider, category);
      if (provider instanceof RegisterableService)
      {
        try
        {
          ((RegisterableService) provider).onRegistration(category);
        } catch (ServiceProviderException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return rt;
    }
    
    
    
    
  }


  /**
   * The Class FilteredIterator.
   */
  private static class FilteredIterator<E> implements Iterator<E>
  {
    /**
     * The filter.
     */
    private Filter filter;
    /**
     * The backend.
     */
    private Iterator<E> backend;
    /**
     * The next obj.
     */
    private E nextObj;

    /**
     * Instantiates a new filtered iterator.
     * 
     * @param filter
     *          the filter.
     * @param backend
     *          the backend.
     */
    public FilteredIterator(Filter filter, Iterator<E> backend)
    {
      this.filter = filter;
      this.backend = backend;
      findNext();
    }

    /**
     * Next.
     * 
     * @return the e.
     */
    public E next()
    {
      if (nextObj == null)
      {
        throw new NoSuchElementException();
      }
      E tmp = nextObj;
      findNext();
      return tmp;
    }

    /**
     * Checks for next.
     * 
     * @return true, if successful.
     */
    public boolean hasNext()
    {
      return nextObj != null;
    }

    /**
     * Removes the.
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }

    /**
     * Sets nextObj to a next provider matching the criterion given by the
     * filter.
     */
    private void findNext()
    {
      nextObj = null;
      while (backend.hasNext())
      {
        E o = backend.next();
        if (filter.filter(o))
        {
          nextObj = o;
          return;
        }
      }
    }
  }
}