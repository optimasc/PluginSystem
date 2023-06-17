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

/** Generic exception that can be thrown by all service providers.
 *  The exceptions are associated with an error code which the value
 *  of error codes being a subset of http error codes and DLNA
 *  error codes.   
 *  
 * @author Carl Eric Codere
 *
 */
public class ServiceProviderException extends Exception
{
  /** The Locked status code means the source or destination resource of a method is locked. */ 
  public static final int RESOURCE_LOCKED = 423;
  /** Service is not implemented status code. */
  public static final int NOT_IMPLEMENTED = 501;
  /** Internal error status code. */
  public static final int INTERNAL_ERROR = 500;
  /** Unauthorized status code. */
  public static final int UNAUTHORIZED = 401;
  /** Bad or illegal parameters to request status code. */
  public static final int BAD_REQUEST = 400;
  /** Resource or location not found status code. */
  public static final int NOT_FOUND = 404;
  /** Insufficient storage to complete operation status code. */
  public static final int INSUFFICIENT_STORAGE = 507;
  
  int errorCode;

 public ServiceProviderException(int errorCode, String msg)
 {
   super(msg);
   this.errorCode = errorCode;
 }
 
 public ServiceProviderException(Throwable e)
 {
   super(e);
   this.errorCode = INTERNAL_ERROR;
 }
 

 /** Return the status code associated with this exception. The
  *  status code should be one of the constants defined in this
  *  class.
  * 
  * @return Status code
  */
 public int getErrorCode()
 {
   return errorCode;
 }

}
