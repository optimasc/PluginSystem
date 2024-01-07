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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Vector;

import org.plugin.spi.ServiceMetadata;

/**
 * Useful utilities associated with service provider plugins.
 * 
 * @author Carl Eric Codere
 * 
 */
public class ServiceProviderUtilities
{
  /**
   * Returns the plugin ID of this plugin. The value returned depends on the
   * instance class of the service. This is a unique identifier for
   * this plugin.
   * 
   * @param pluginObject
   * @return The associated plugin ID.
   */
  public static String getPluginId(Object pluginObject)
  {
    return pluginObject.getClass().getName();
  }

  /** Returns the plugin version. If the plugin implements the
   * {@link ServiceMetadata} class it calls the method to retrieve
   * the implementation version, otherwise it uses the
   * {@link java.lang.Package} to retrieve the implementation version.
   *
   * @param pluginObject
   * @return The implementation version as a string.
   */
  public static String getPluginVersion(Object pluginObject)
  {
    if (pluginObject instanceof ServiceMetadata)
    {
      return ((ServiceMetadata) pluginObject).getVersion();
    }
    return pluginObject.getClass().getPackage().getImplementationVersion();
  }

  /** Returns the plugin title. The title usually represents a human
   *  readable short description of the plugin.
   *
   *  If the plugin implements the {@link ServiceMetadata} class it calls the
   *  method to retrieve the implementation title, otherwise it uses the
   * {@link java.lang.Package} to retrieve the implementation title.
   *
   * @param pluginObject
   * @return The implementation title as a string.
   */
  public static String getPluginTitle(Object pluginObject)
  {
    if (pluginObject instanceof ServiceMetadata)
    {
      return ((ServiceMetadata) pluginObject).getDescription(null);
    }
    Package pkg = pluginObject.getClass().getPackage();
    return pkg.getImplementationTitle();
  }

  /** Returns the name of the plugin creator.
   *
   * If the plugin implements the {@link ServiceMetadata} class it calls the
   * method to retrieve the implementation vendor, otherwise it uses the
   * {@link java.lang.Package} to retrieve the implementation vendor.
   *
   * @param pluginObject
   * @return The implementation creator as a string.
   */
  public static String getPluginVendor(Object pluginObject)
  {
    if (pluginObject instanceof ServiceMetadata)
    {
      return ((ServiceMetadata) pluginObject).getVendorName();
    }
    return pluginObject.getClass().getPackage().getImplementationVendor();
  }

  /**
   * Represents a Service List classified by categories, where each category is
   * represented as a class type. In each of these categories a list of
   * providers object instance is provided.
   * 
   */
  public static class ServiceList
  {
    /**
     * A list of providers for each category.
     */
    Map<Class<?>, Vector> categories = new HashMap<Class<?>, Vector>();

    /**
     * Instantiates a new categories map.
     * 
     */
    public ServiceList()
    {
    }

    /**
     * Gets the providers object instances for the specified category as an
     * iterator.
     * 
     * @param category
     *          the category.
     * @return the providers.
     */
    public Iterator<?> getProviders(Class<?> category)
    {
      Vector providers = categories.get(category);
      if (null == providers)
      {
        throw new IllegalArgumentException("Unknown category: " + category);
      }
      return providers.iterator();
    }

    /**
     * Gets the providers object instances for the specified category as an
     * iterator.
     * 
     * @param category
     *          the category.
     * @return the providers.
     */
    public List<?> getProvidersAsList(Class<?> category)
    {
      Vector providers = categories.get(category);
      if (null == providers)
      {
        throw new IllegalArgumentException("Unknown category: " + category);
      }
      return providers;
    }

    /**
     * List all the categories.
     * 
     * @return the iterator< class<?>>.
     */
    public Iterator<Class<?>> list()
    {
      return categories.keySet().iterator();
    }

    /**
     * Checks all categories and determines if this provider class is already
     * included in any of category registries.
     * 
     * @param provider An instance object
     * @return true if this provider is registered in any category.
     */
    public boolean contains(Object provider)
    {
      boolean found = false;
      Iterator<Class<?>> categoryIterator = categories.keySet().iterator();
      /* Go through all categories */
      while (categoryIterator.hasNext())
      {
        found = contains(provider, categoryIterator.next());
        if (found == true)
          return true;
      }
      return false;
    }

    public boolean contains(Object provider, Class<?> category)
    {
      Vector map = categories.get(category);
      if (map == null)
        return false;
      return map.contains(provider);
    }

    /** Return a list of categories for the specified provider. */
    public Iterator<Class<?>> getCategories(Object provider)
    {
      List<Class<?>> list = new Vector<Class<?>>();
      Iterator<Class<?>> categoryIterator = list();
      while (categoryIterator.hasNext())
      {
        Class<?> category = categoryIterator.next();
        Vector map = categories.get(category);
        if (map == null)
          continue;
        if (map.contains(provider))
        {
          list.add(category);
        }
      }
      return list.iterator();
    }

    /**
     * Adds the specified category.
     * 
     * @param category
     *          the category.
     */
    public void addCategory(Class<?> category)
    {
      categories.put(category, new Vector());
    }

    /**
     * Adds a provider to the category. If <code>category</code> is
     * <code>null</code> then the provider will be added to all categories which
     * the provider is assignable from.
     * 
     * @param provider
     *          provider to add.
     * @param category
     *          category to add provider to.
     * @return true, if there were such provider in some category.
     */
    public boolean addProvider(Object provider, Class<?> category)
    {
      if (provider == null)
      {
        throw new IllegalArgumentException("provider should be != NULL");
      }
      boolean rt;
      if (category == null)
      {
        rt = findAndAdd(provider);
      } else
      {
        rt = addToNamed(provider, category);
      }
      return rt;
    }

    /** Delete all providers of the specified category. */
    public void deleteProviders(Class<?> category)
    {
      if (category == null)
      {
        throw new IllegalArgumentException("category should be != NULL");
      }
      Vector map = categories.get(category);
      if (map == null)
        return;
      /* For each item in map, delete it */
      for (int i = 0; i < map.size(); i++)
      {
        deleteProvider(map.get(i),category);
      }
      map.clear();
    }

    public boolean deleteProvider(Object provider, Class<?> category)
    {
      if (provider == null)
      {
        throw new IllegalArgumentException("provider should be != NULL");
      }
      boolean rt;
      /* Remove the provider from all categories! */
      if (category == null)
      {
        /* We must remove this provider to all categories */
        Iterator<Class<?>> categoryIterator = categories.keySet().iterator();
        while (categoryIterator.hasNext())
        {
          Class<?> listCategory = categoryIterator.next();
          Vector list = categories.get(listCategory);
          Iterator listIterator = list.iterator();
          while (listIterator.hasNext())
          {
            Object object = listIterator.next();
            if (object.getClass().isAssignableFrom(provider.getClass()))
            {
              listIterator.remove();
            }
          }
        }
        rt = true;
      } else
      {
        /* Get the category map. */
        Vector list = categories.get(category);
        if (list == null)
        {

          throw new IllegalArgumentException("Category is not registered.");
        }
        /* Remove the service from that category. */
        if (list.remove(provider) == false)
        {
          rt = false;
        } else
        {
          rt = true;
        }
      }
      return rt;
    }

    /**
     * Adds the to named.
     * 
     * @param provider
     *          the provider.
     * @param category
     *          the category.
     * @return true, if successful.
     */
    private boolean addToNamed(Object provider, Class<?> category)
    {
      Vector list = categories.get(category);
      if (null == list)
      {
        throw new IllegalArgumentException("Unknown category: " + category);
      }
      if (list.contains(provider) == false)
      {
        return list.add(provider);
      }
      return false;
    }

    /**
     * Find and add this provider to each category it implements.
     * 
     * @param provider
     *          the provider.
     * @return true, if successful.
     */
    private boolean findAndAdd(Object provider)
    {
      boolean rt = false;
      for (Entry<Class<?>, Vector> e : categories.entrySet())
      {
        if (e.getKey().isAssignableFrom(provider.getClass()))
        {
          Vector list = e.getValue();
          if (list.contains(provider) == false)
          {
            rt |= list.add(provider);
          }
        }
      }
      return rt;
    }

  }

  /**
   * Returns a list of all service providers, both searched through the class
   * loader implementing the specified categories, as well as those which are
   * manually registered.
   * 
   * @param serviceRegistry
   *          [in] Service registry that will be used to lookup loaded dynamically
   *          loaded service providers.
   * @param additionalProviders
   *          [in] A list of additional service providers specifically defined
   *          by the developer to be returned.
   * @param categories
   *          [in] The categories to look for for dynamically loaded service
   *          providers.
   * @return A list of all service providers.
   */
  public static ServiceList getProviders(ServiceRegistry serviceRegistry,
      Object[] additionalProviders, Class categories[])
  {
    ServiceList serviceList = new ServiceList();
    for (int i = 0; i < categories.length; i++)
    {
      serviceList.addCategory(categories[i]);
    }

    /* Lookup all non-registered plugins first. */
    for (int j = 0; j < categories.length; j++)
    {
      Iterator iterator = serviceRegistry.lookupProviders(categories[j]);
      while (iterator.hasNext())
      {
        Object provider = iterator.next();
        serviceList.addProvider(provider, null);
      }
    }

    /* Lookup for all registered plugins. */
    for (int j = 0; j < additionalProviders.length; j++)
    {
      Object provider = additionalProviders[j];
      serviceList.addProvider(provider, null);
    }
    return serviceList;
  }

  /**
   * Utility function for {@link org.plugin.spi.ServiceConfiguration} to validate values against
   * datatypes and converts to the correct datatype if necessary. Throws an
   * exception if the value is not of the correct format.
   * 
   * @param dataType
   *          [in] The expected datatype.
   * @param value
   *          [in] The actual value
   * @return The value converted to the expected datatype.
   * @throws IllegalArgumentException
   *           In case the value cannot be converted to the expected datatype or
   *           value is not valid.
   */
  public static Object validateValue(Object dataType, Object value) throws IllegalArgumentException
  {
    boolean valid = false;
    /* Choice of integers */
    if (dataType instanceof Integer[])
    {
      Integer[] intValues = (Integer[]) dataType;
      if (value instanceof Integer)
      {
      } else if (value instanceof String)
      {
        value = new Integer((String) value);
      } else
      {
        throw new IllegalArgumentException("value must of type Integer or String");
      }
      for (int i = 0; i < intValues.length; i++)
      {
        if (value.equals(intValues[i]))
        {
          valid = true;
          break;
        }
      }
      if (valid == false)
      {
        throw new IllegalArgumentException("Invalid value of integer");
      }
    }
    /* Integer */
    if (dataType instanceof Integer)
    {
      if (value instanceof Integer)
      {
        valid = true;
      } else if (value instanceof String)
      {
        value = new Integer((String) value);
      } else
      {
        throw new IllegalArgumentException("value must of type Integer or String");
      }
    }
    /* Choice of string values */
    if (dataType instanceof String[])
    {
      String[] strValues = (String[]) dataType;
      if (value instanceof String)
      {
        for (int i = 0; i < strValues.length; i++)
        {
          if (strValues[i].equals(value))
          {
            valid = true;
            break;
          }
        }
        if (valid == false)
        {
          throw new IllegalArgumentException("Invalid value of String.");
        }

      } else
      {
        throw new IllegalArgumentException("value must of type String");
      }
    }
    /* A string value */
    if (dataType instanceof String)
    {
      if (value instanceof String)
      {
        valid = true;
      } else
      {
        throw new IllegalArgumentException("value must of type String");
      }
    }
    /* A boolean value */
    if (dataType instanceof Boolean)
    {
      if (value instanceof Boolean)
      {
        valid = true;
      } else
      {
        throw new IllegalArgumentException("value must of type Boolean");
      }
    }
    return value;
  }

  /**
   * This is a general purpose utility that permits to load a service list from
   * disk for a specific category and returns the available services, as well as
   * those who are activated. It does the following:
   * 
   * <ul>
   * <li>Find all available providers from both the <code>ServiceLoader</code>
   * as well as <code>additionalProviders</code></li>
   * <li>If the service list on disk does not exist, it is created from the
   * above step, if configured to be done.</li>
   * <li>Reads the service list from disk</li>
   * <li>For each item in the service list from disk
   * <ul>
   * <li>If the service is actually available add it to the list of available
   * services in the <code>list</code> parameter.</li>
   * <li>If the service is available and enabled, add it to the
   * <code>registry</code> parameter.</li>
   * </ul>
   * </li>
   * <li>All additional providers that were not on disk are added to the
   * <code>list</code> parameter at the end.</li>
   * </ul>
   * 
   * The format of the service configuration file is a simple text file.
   * <ul>
   *   <li>one service per line</li>
   *   <li>each service is composed of the plugin ID of the provider separated
   *     by a key-value separator "=" followed by its service activation state.</li>
   *   <li>The service activation state is stored as a lower-case
   *    {@link java.lang.Boolean} string</li>
   *   <li>Comment lines are denoted by the number sign (#) or the exclamation mark (!)
   *     as the first non blank character</li>
   *   <li>The name of the file is the actual category name.</li>
   * </ul>
   * 
   * @param directory
   *          [in] The directory where the configuration data will be loaded from.
   * @param list
   *          [in,out] The populated service list of all available services discovered,
   *          ordered from the order on disk.
   * @param registry
   *          [in,out] The populated registry of all services that were activated in the
   *          configuration file.
   * @param additionalProviders
   *          [in] additional providers of this category to add when listing
   *          available services, this can be an empty array, if there are none.
   * @param category
   *          [in] The categories of the services.
   */
  public static void loadServiceList(File directory, ServiceList list, ServiceRegistry registry,
      Object[] additionalProviders, Class category)
  {

    String s;
    String className;
    boolean enabled;
    boolean found;
    Vector additionalProvider = new Vector();
    Vector<String[]> config = new Vector<String[]>();
    ServiceList foundPluginList = ServiceProviderUtilities.getProviders(registry,
        additionalProviders, new Class[] { category });
    File fileList = new File(directory, category.getName()
        + ".properties");
    if (fileList.exists() == false)
    {
      saveServiceList(directory, foundPluginList, registry, category);
    }

    try
    {
      Reader reader = new FileReader(fileList);
      BufferedReader fileReader = new BufferedReader(reader);

      /* Read plugin list configuration file */
      while ((s = fileReader.readLine()) != null)
      {
        String s1 = s.trim();
        /* Supported comment charaters as first non-blacnk character in the
         * properties file 
         */
        if (s1.startsWith("!") || s1.startsWith("#"))
        {
            continue;
        }
        String[] tokens = s.split("=");
        /* Get the class name and enabled state */
        config.add(tokens);
      }

      /** Go through all plugin list items that actually exist */
      Iterator it = foundPluginList.getProviders(category);
      while (it.hasNext())
      {
        Object provider = it.next();
        found = false;
        /* Now add all known entries that were present in the configuration list in the correct order */
        for (int i = 0; i < config.size(); i++)
        {
          /* Get the class name and enabled state */
          String[] tokens = config.elementAt(i);
          className = tokens[0];
          enabled = Boolean.parseBoolean(tokens[1]);
          /* Found ? */
          if (className.equals(provider.getClass().getName()))
          {
            list.addProvider(provider, category);
            if (enabled == true)
            {
              registry.registerServiceProvider(provider, category);
            }
            found = true;
            /* Go to next provider */
            break;
          }
        } /* end for loop */

        /** If not found, add to list of things to add at end. */
        if (found == false)
        {
          additionalProvider.add(provider);
        }
      }
      fileReader.close();
      reader.close();

      /**
       * All additional discovered providers which are not in the configuration
       * list.
       */
      for (int i = 0; i < additionalProvider.size(); i++)
      {
        list.addProvider(additionalProvider.elementAt(i), category);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  /** Saves the service list to a file, saving it in registry order.
   *
   * @param directory
   *          [in] The directory where the configuration data will be saved to.
   * @param list
   *          [in] The list of services.
   * @param registry
   *          [in] The list of activated services from the list of services.
   * @param category
   *          [in] The categories of the services.
   *
   * @see #loadServiceList
   */
  public static void saveServiceList(File directory, ServiceList list, ServiceRegistry registry,
      Class category)
  {
    try
    {
      File fileList = new File(directory, category.getName()
          + ".properties");
      Writer writer = new FileWriter(fileList);
      BufferedWriter fileWriter = new BufferedWriter(writer);

      Iterator it = list.getProviders(category);
      /* Check if it is in the registry. */
      while (it.hasNext())
      {
        Object provider = it.next();
        String s = provider.getClass().getName();
        if (registry.contains(provider, category))
        {
          s = s + "=" + Boolean.TRUE.toString();
        } else
        {
          s = s + "=" + Boolean.FALSE.toString();
        }
        fileWriter.write(s + "\n");
      }
      fileWriter.close();
      writer.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  
  /** Uses the service loader to load and instantiate all service providers
   *  that conform to the specified interface. Contrary to the standard 
   *  {@link java.util.ServiceLoader} class, this automatically searches
   *  in the following directories for service providers:
   *  <ul>
   *   <li>All jar libraries in ../plugins directory</li>
   *   <li>All jar libraries in the ../plugins/lib directory</li>
   *   <li>All jar libraries in the ../plugins/bin directory</li> 
   *  </ul>
   *  
   *  This is based on the application being run.
   *  
   * @param provider The service interface that needs to be searched 
   *   for.
   * @return List of service providers implementing the specified
   *   provider interface.
   */
  public static <S> List<S> loadServiceProviders(Class<S> provider)
  {
    ServiceLoader<S> sl;
    Iterator<S> apit;
    File loc;

    Vector<S> exporter = new Vector<S>();

    loc = new File("../plugins/bin");
    if (loc.exists())
    {
      File[] flist = loc.listFiles(new FileFilter()
      {
        public boolean accept(File file)
        {
          return file.getPath().toLowerCase().endsWith(".jar");
        }
      });
      URL[] urls = new URL[flist.length];
      for (int i = 0; i < flist.length; i++)
        try
        {
          urls[i] = flist[i].toURI().toURL();
        } catch (MalformedURLException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      URLClassLoader ucl = new URLClassLoader(urls);

      /* Load those in the plugins directory */
      sl = ServiceLoader.load(provider, ucl);
      apit = sl.iterator();
      while (apit.hasNext())
      {
        exporter.add(apit.next());
      }
    }

    loc = new File("../plugins/lib");
    if (loc.exists())
    {
      File[] flist = loc.listFiles(new FileFilter()
      {
        public boolean accept(File file)
        {
          return file.getPath().toLowerCase().endsWith(".jar");
        }
      });
      URL[] urls = new URL[flist.length];
      for (int i = 0; i < flist.length; i++)
        try
        {
          urls[i] = flist[i].toURI().toURL();
        } catch (MalformedURLException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      URLClassLoader ucl = new URLClassLoader(urls);

      /* Load those in the plugins directory */
      sl = ServiceLoader.load(provider, ucl);
      apit = sl.iterator();
      while (apit.hasNext())
        exporter.add(apit.next());
    }

    loc = new File("../plugins");
    if (loc.exists())
    {
      File[] flist = loc.listFiles(new FileFilter()
      {
        public boolean accept(File file)
        {
          return file.getPath().toLowerCase().endsWith(".jar");
        }
      });
      URL[] urls = new URL[flist.length];
      for (int i = 0; i < flist.length; i++)
        try
        {
          urls[i] = flist[i].toURI().toURL();
        } catch (MalformedURLException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      URLClassLoader ucl = new URLClassLoader(urls);

      /* Load those in the plugins directory */
      sl = ServiceLoader.load(provider, ucl);
      apit = sl.iterator();
      while (apit.hasNext())
        exporter.add(apit.next());
    }

    /* Dump all exporter plugins */
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    for (int i = 0; i < exporter.size(); i++)
    {
      logger.info("Loaded " + exporter.get(i).getClass().getName() + " plugin.");
    }
    return exporter;
  }


}
