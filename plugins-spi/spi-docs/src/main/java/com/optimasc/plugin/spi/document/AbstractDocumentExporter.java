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
package com.optimasc.plugin.spi.document;

import java.io.File;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.w3c.dom.Document;

import com.optimasc.plugin.ServiceProviderException;

/**
 * Base helper class used for document exporters. The document exporter class
 * supports the following output formats:
 *
 * <ul>
 * <li>{@link #OUTPUT_ARTICLE}</li>
 * <li>{@link #OUTPUT_REPORT}</li>
 * <li>{@link #OUTPUT_BOOK}</li>
 * </ul>
 *
 * It supports the following input types:
 * <ul>
 * <li>Array of {@link org.w3c.dom.Document}</li>
 * </ul>
 * The input format must be a valid XHTML Document.
 *
 *
 * It supports the following output types:
 * <ul>
 * <li>{@link java.io.File} pointing to a directory</li>
 * </ul>
 *
 *
 */
public abstract class AbstractDocumentExporter implements DocumentExporter
{
  /** Active Document style */
  String documentStyle;
  /** Base URL reference of the document, used to resolve relative references */
  URL baseURL;

  static final Class[] inputTypes = { org.w3c.dom.Document[].class };

  static final Class[] outputTypes = { java.io.File.class };

  public AbstractDocumentExporter()
  {
    super();
    documentStyle = OUTPUT_ARTICLE;
  }

  /**
   * Returns an array of Class objects indicating what types of objects that may
   * be returned from the process method. This implementation returns an array
   * of 1 item with a content equal to <code>java.io.OutputStream</code>.
   *
   * @return a non-null array of Class objects of length at least 1.
   */

  public Class[] getOutputTypes() throws ServiceProviderException
  {
    return outputTypes;
  }

  /**
   * Returns an array of Class objects indicating what types of objects may be
   * used as input to the process method. This implementation returns an array
   * of 1 item with a content equal to <code>org.w3c.dom.Document</code>.
   *
   * @return a non-null array of Class objects of length at least 1.
   */

  public Class[] getInputTypes() throws ServiceProviderException
  {
    return inputTypes;
  }


  public void process(Object input, Object output)
      throws ServiceProviderException
  {
    /* Check if supported format */
    if ((input instanceof Document[]) == false)
    {
      throw new ServiceProviderException(ServiceProviderException.BAD_REQUEST,
          "Unsupported input type " + input.getClass().getName());
    }
    if ((output instanceof File) == false)
    {
      throw new ServiceProviderException(ServiceProviderException.BAD_REQUEST,
          "Unsupported output type " + output.getClass().getName());
    }

  }

  /**
   * Return the supported parameters that can be configured for this service
   * provider. It currently supports the {@link #PROPERTY_DOCUMENT_STYLE}
   * configuration parameter.
   *
   */

  public String[] getParameterNames()
  {
    return new String[] { PROPERTY_DOCUMENT_STYLE, PROPERTY_BASE_URL };
  }

  /**
   * Sets the specified configuration parameter for this service provider. It
   * currently supports the {@link #PROPERTY_DOCUMENT_STYLE} and
   * {@link #PROPERTY_BASE_URL} configuration parameters.
   *
   * <p>{@link #PROPERTY_DOCUMENT_STYLE} must be one of the values returned by
   * {@link #getPropertyAllowedValues(String)}; setting it to <code>null</code>
   * restores the default style {@link #OUTPUT_ARTICLE}.
   * {@link #PROPERTY_BASE_URL} must be a {@link java.net.URL} or
   * <code>null</code>.</p>
   *
   * @param name
   *          The configuration parameter to set.
   * @param value
   *          The value of the configuration parameter
   * @throws IllegalArgumentException
   *          If the parameter is not recognized, or the value is of the wrong
   *          type or is not one of the permitted values.
   */

  public void setProperty(String name, Object value)
      throws IllegalArgumentException
  {
    checkName(name);
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      if (value == null)
      {
        documentStyle = OUTPUT_ARTICLE;
        return;
      }
      if ((value instanceof String) == false)
      {
        throw new IllegalArgumentException("Parameter '" + name
            + "' must be of type java.lang.String");
      }
      Object[] allowed = getPropertyAllowedValues(name);
      for (int i = 0; i < allowed.length; i++)
      {
        if (allowed[i].equals(value))
        {
          documentStyle = (String) value;
          return;
        }
      }
      throw new IllegalArgumentException("Unsupported document style '" + value + "'");
    } else
    {
      if ((value != null) && ((value instanceof URL) == false))
      {
        throw new IllegalArgumentException("Parameter '" + name
            + "' must be of type java.net.URL");
      }
      baseURL = (URL) value;
    }
  }

  /**
   * Returns the specified configuration parameter for this service provider. It
   * currently supports the {@link #PROPERTY_DOCUMENT_STYLE} configuration
   * parameter.
   *
   * @param name
   *          The configuration parameter to retrieve
   * @return The configuration parameter value
   *
   */

  public Object getProperty(String name) throws IllegalArgumentException
  {
    checkName(name);
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      return documentStyle;
    } else if (name.equals(PROPERTY_BASE_URL))
    {
      return baseURL;
    } else
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }


  public Class getPropertyDatatype(String name) throws IllegalArgumentException
  {
    checkName(name);
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      return String.class;
    } else if (name.equals(PROPERTY_BASE_URL))
    {
        return URL.class;
    } else
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }


  public String getPropertyHelp(String name, String locale)
      throws IllegalArgumentException
  {
    checkName(name);
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      return "Document style to output";
    } else if (name.equals(PROPERTY_BASE_URL))
    {
      return "Document base location";
    } else
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }

  /**
   * Returns the permitted values of the specified configuration parameter.
   * For {@link #PROPERTY_DOCUMENT_STYLE} this implementation returns the
   * mandatory styles {@link #OUTPUT_ARTICLE}, {@link #OUTPUT_REPORT} and
   * {@link #OUTPUT_BOOK}; subclasses supporting additional styles such as
   * {@link #OUTPUT_LETTER} should override this method. Returns
   * <code>null</code> for {@link #PROPERTY_BASE_URL}, which is unrestricted.
   *
   * @param name
   *          The configuration parameter.
   * @return The permitted values, or <code>null</code> if unrestricted.
   * @throws IllegalArgumentException
   *          If the parameter is not recognized.
   */
  public Object[] getPropertyAllowedValues(String name)
      throws IllegalArgumentException
  {
    checkName(name);
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      return new String[] { OUTPUT_ARTICLE, OUTPUT_REPORT, OUTPUT_BOOK };
    }
    return null;
  }

  /** Throws an IllegalArgumentException if the parameter name is null or
   *  not recognized by this class.
   */
  private static void checkName(String name) throws IllegalArgumentException
  {
    if (name == null)
    {
      throw new IllegalArgumentException("Parameter name must not be null");
    }
    if ((name.equals(PROPERTY_DOCUMENT_STYLE) == false)
        && (name.equals(PROPERTY_BASE_URL) == false))
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }

}
