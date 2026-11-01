package com.optimasc.plugin.spi;

import com.optimasc.plugin.ServiceProviderException;

/** Optional interface that may be implemented by service providers that
 *  expose discrete, immutable capability flags.
 *
 *  <p>A <em>feature</em> is a boolean fact about the provider's
 *  implementation that does not vary per instance and cannot be configured
 *  by the application — for example "this provider stores its data outside
 *  the resource it describes". Features differ from
 *  {@link ServiceConfiguration} properties, which are mutable, typed,
 *  per-instance values that the application sets to alter behaviour.</p>
 *
 *  <p>The feature key strings are defined by the consuming layer, not by
 *  this interface; see that layer's documentation for the recognised keys
 *  and their meanings. Querying a key the provider does not recognise
 *  returns {@code false} rather than raising an exception.</p>
 *
 * @author Carl Eric Codere
 */
public interface FeatureProvider
{
  /** Returns {@code true} if this provider supports the named feature.
   *
   *  <p>An unrecognised feature key returns {@code false}; this method
   *  never throws for an unknown key, so callers may probe optimistically.</p>
   *
   * @param name [in] The feature key to test. Must not be {@code null}.
   * @return {@code true} if the feature is supported, {@code false} if it
   *         is unsupported or the key is not recognised.
   * @throws ServiceProviderException If the support state cannot be
   *         determined.
   */
  public boolean hasFeature(String name) throws ServiceProviderException;

  /** Returns the feature keys this provider supports, or {@code null} if
   *  none. Every key in the returned array satisfies
   *  {@code hasFeature(key) == true}.
   *
   * @return A non-empty array of supported feature keys, or {@code null}
   *         if this provider supports no features.
   * @throws ServiceProviderException If the feature set cannot be
   *         determined.
   */
  public String[] getFeatures() throws ServiceProviderException;
}