package com.github.savaki.handlebars

import com.github.jknack.handlebars.ValueResolver
import java.util.concurrent.ConcurrentHashMap
import java.lang.reflect.Method
import java.util

/**
 * @author matt.ho@gmail.com
 */
class ScalaValueResolver extends ValueResolver {
  private[this] val cache = new ConcurrentHashMap[Class[_], util.Map[String, Method]]()

  def resolve(context: Any, name: String): Object = {
    if (context == null) {
      ValueResolver.UNRESOLVED

    } else {
      var methods: util.Map[String, Method] = cache.get(context.getClass)

      if (methods == null) {
        methods = new ConcurrentHashMap[String, Method]()
        context.getClass.getMethods.map {
          method => methods.put(method.getName, method)
        }
      }

      val method: Method = methods.get(name)
      if (method == null) {
        ValueResolver.UNRESOLVED

      } else {
        method.invoke(context)
      }
    }
  }
}
