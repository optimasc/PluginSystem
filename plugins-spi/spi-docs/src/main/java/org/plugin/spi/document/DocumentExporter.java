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

import org.plugin.spi.Codec;
import org.plugin.spi.ServiceConfiguration;
import org.plugin.spi.ServiceMetadata;
import org.w3c.dom.Document;

/** Represents a service provider interface that can be used to export XHTML DOM Documents
 *  to an output stream in a specific format.
 *  
 *  <p>Any Document Exporter requires to have the following minimum requirements:</p>
 *  
 *  <ul>
 *  <li>It must support at least the following input types:
 *  <ul>
 *   <li><code>org.w3c.dom.Document</code> as a valid XHTML 1.0 Strict Document with the following minimum mandatory
 *    supported modules of XHTML 1.1, all other elements from other modules that are not supported by the service
 *    provider should be ignored
 *    <ul>
 *     <li>Structure Module</li>
 *     <li>Text Module</li>
 *     <li>List Module</li>
 *     <li>Presentation Module</li>
 *     <li>Basic Tables Module</li>
 *     <li>Image Module
 *      <ul>
 *        <li>data URI's shall be supported (embedded images)</li>
 *        <li>Scalable Vector graphics tiny image format</li>
 *        <li>Portable Network Graphics image format</li>  
 *      </ul>
 *     </li>
 *     <li>Metainformation Module, with the following meta <code>name</code> elements supported:
 *     <ul>
 *       <li><code>author</code> which represents the author of the document.</li>
 *       <li><code>generator</code> which contains the name of the tool / generator used to create this document.</li>
 *       <li><code>keywords</code> which contains the subject (keywords) of the document, each keyword being
 *         separated by others with a colon or semi-colon.</li>
 *       <li><code>description</code> which contains the description of the document.</li>
 *       <li><code>date</code> which contains the date of last modification of the document text in ISO 8601 format.</li>
 *     </ul>
 *     </li>
 *    </ul>
 *    </li>
 *  </ul>
 *  </li>
 *  
 *  <li>It must support at least the following output types:
 *      <ul>
 *          <li><code>java.io.OutputStream</code></li>
 *      </ul>
 *  </li>
 *  
 *  <li>It must support at least the following parameter configuration for the {@link #PROPERTY_DOCUMENT_STYLE} 
 *  configurable parameter
 *     <ul>
 *      <li>{@link #OUTPUT_ARTICLE} to represent an article output (The default).</li>
 *      <li>{@link #OUTPUT_REPORT} to represent a report output.</li>
 *      <li>{@link #OUTPUT_BOOK} to represent a book output.</li>
 *     </ul>
 *  </li>
 *  </ul>
  *  
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public interface DocumentExporter extends Codec, ServiceConfiguration, ServiceMetadata
{
  /** Property to set and get the document style configured for this
   *  document exporter.
   */
  public static final String PROPERTY_DOCUMENT_STYLE = "DocumentStyle";
  /** Property to set the base URL, used mostly used for resolving 
   *  relative references to the document.
   */
  public static final String PROPERTY_BASE_URL = "DocumentBaseURL";
  
  /** Document style for {@link TransformerUtils#prepare(Document, String)} method: Report type. */ 
  public static final String OUTPUT_REPORT = "report";
  /** Document style for {@link TransformerUtils#prepare(Document, String)} method: Article type. */ 
  public static final String OUTPUT_ARTICLE = "article";
  /** Document style for {@link TransformerUtils#prepare(Document, String)} method: Book type. */ 
  public static final String OUTPUT_BOOK = "book";
  /** Document style for {@link TransformerUtils#prepare(Document, String)} method: Letter type. */ 
  public static final String OUTPUT_LETTER = "letter";
  /** Returns an array of Strings containing a list of file suffixes associated 
   *  with the format that can be exported. 
   */
  public String[] getFileSuffixes();

  
  
}
