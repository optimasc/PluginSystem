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
package org.plugin.spi.document;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.plugin.ServiceProviderException;
import org.w3c.dom.Document;

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
 * <li>{@link org.w3c.dom.Document}</li>
 * </ul>
 * The input format must be a valid XHTML Document.
 * 
 * 
 * It supports the following output types:
 * <ul>
 * <li>{@link java.io.OutputStream}</li>
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

  static final Class[] inputTypes = { org.w3c.dom.Document.class };

  static final Class[] outputTypes = { java.io.OutputStream.class };

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
  @Override
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
  @Override
  public Class[] getInputTypes() throws ServiceProviderException
  {
    return inputTypes;
  }

  @Override
  public void process(Object input, Object output)
      throws ServiceProviderException
  {
    /* Check if supported format */
    if ((input instanceof Document) == false)
    {
      throw new ServiceProviderException(ServiceProviderException.BAD_REQUEST,
          "Unsupported input type " + input.getClass().getName());
    }
    if ((output instanceof OutputStream) == false)
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
  @Override
  public String[] getParameterNames()
  {
    return new String[] { PROPERTY_DOCUMENT_STYLE, PROPERTY_BASE_URL };
  }

  /**
   * Sets the specified configuration parameter for this service provider. It
   * currently supports the {@link #PROPERTY_DOCUMENT_STYLE} configuration
   * parameter.
   * 
   * @param name
   *          The configuration parameter to set, should be
   *          {@link #PROPERTY_DOCUMENT_STYLE}.
   * @param value
   *          The value of the configuration parameter
   * 
   */
  @Override
  public void setProperty(String name, Object value)
      throws IllegalArgumentException
  {
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      documentStyle = value.toString();
    } else if (name.equals(PROPERTY_BASE_URL))
    {
      baseURL = (URL) value;
    }
    else
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }

  /**
   * Returns the specified configuration parameter for this service provider. It
   * currently supports the {@link #PROPERTY_DOCUMENT_STYLE} configuration
   * parameter.
   * 
   * @param name
   *          The configuration parameter to retrieve
   * @returns The configuration parameter value
   * 
   */
  @Override
  public Object getProperty(String name) throws IllegalArgumentException
  {
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

  @Override
  public Object getPropertyDatatype(String name)
      throws IllegalArgumentException
  {
    if (name.equals(PROPERTY_DOCUMENT_STYLE))
    {
      return new String();
    } else if (name.equals(PROPERTY_BASE_URL))
    {
      try
      {
        return new URL(".");
      } catch (MalformedURLException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
    } else
    {
      throw new IllegalArgumentException("Unknown parameter '" + name + "'");
    }
  }

  @Override
  public String getPropertyHelp(String name, String locale)
      throws IllegalArgumentException
  {
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

}
