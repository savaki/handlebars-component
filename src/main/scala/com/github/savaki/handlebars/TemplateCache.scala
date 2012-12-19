package com.github.savaki.handlebars

import java.util.concurrent.ConcurrentHashMap
import com.github.jknack.handlebars.internal.ComponentTemplate

/**
 * Based on the handlebars.java caching system, this class caches content
 *
 * @author edgar.espina
 * @since 0.1.0
 */
trait TemplateCache {
  /**
   * Remove all mappings from the cache.
   */
  def clear()

  /**
   * Evict the mapping for this key from this cache if it is present.
   *
   * @param key the key whose mapping is to be removed from the cache
   */
  def evict(key: Object)

  /**
   * Return the value to which this cache maps the specified key. Returns
   * <code>null</code> if the cache contains no mapping for this key.
   *
   * @param key key whose associated value is to be returned.
   * @return the value to which this cache maps the specified key,
   *         or <code>null</code> if the cache contains no mapping for this key
   */
  def get(key: Object): ComponentTemplate

  /**
   * Associate the specified value with the specified key in this cache.
   * <p>
   * If the cache previously contained a mapping for this key, the old value is
   * replaced by the specified value.
   *
   * @param key the key with which the specified value is to be associated
   * @param template the value to be associated with the specified key
   */
  def put(key: Object, template: ComponentTemplate)
}

/**
 * NoCache provides a default implementation of the cache that doesn't cache anything.  Useful for development mode
 * where we always want content to be reloaded.
 */
object NoCache extends TemplateCache {
  /**
   * Remove all mappings from the cache.
   */
  def clear() {}

  /**
   * Evict the mapping for this key from this cache if it is present.
   *
   * @param key the key whose mapping is to be removed from the cache
   */
  def evict(key: Object) {}

  /**
   * Return the value to which this cache maps the specified key. Returns
   * <code>null</code> if the cache contains no mapping for this key.
   *
   * @param key key whose associated value is to be returned.
   * @return the value to which this cache maps the specified key,
   *         or <code>null</code> if the cache contains no mapping for this key
   */
  def get(key: Object) = null

  /**
   * Associate the specified value with the specified key in this cache.
   * <p>
   * If the cache previously contained a mapping for this key, the old value is
   * replaced by the specified value.
   *
   * @param key the key with which the specified value is to be associated
   * @param template the value to be associated with the specified key
   */
  def put(key: Object, template: ComponentTemplate) {}
}

/**
 * Implementation of TemplateCache backed by java.util.concurrent.ConcurrentHashMap
 */
class ConcurrentCache extends TemplateCache {
  private[this] val NULL = new Object()
  private[this] val store = new ConcurrentHashMap[AnyRef, ComponentTemplate]()

  /**
   * Remove all mappings from the cache.
   */
  def clear() {
    store.clear()
  }

  /**
   * Evict the mapping for this key from this cache if it is present.
   *
   * @param key the key whose mapping is to be removed from the cache
   */
  def evict(key: Object) {
    store.remove(key)
  }

  /**
   * Return the value to which this cache maps the specified key. Returns
   * <code>null</code> if the cache contains no mapping for this key.
   *
   * @param key key whose associated value is to be returned.
   * @return the value to which this cache maps the specified key,
   *         or <code>null</code> if the cache contains no mapping for this key
   */
  def get(key: Object): ComponentTemplate = {
    val template = store.get(key)
    if (template == NULL) {
      null
    } else {
      template
    }
  }

  /**
   * Associate the specified value with the specified key in this cache.
   * <p>
   * If the cache previously contained a mapping for this key, the old value is
   * replaced by the specified value.
   *
   * @param key the key with which the specified value is to be associated
   * @param template the value to be associated with the specified key
   */
  def put(key: Object, template: ComponentTemplate) {
    store.put(key, template)
  }
}

