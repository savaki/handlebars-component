package com.github.savaki.handlebars

import com.github.jknack.handlebars.ValueResolver

/**
 * @author matt.ho@gmail.com
 */
class MapValueResolver extends ValueResolver {
  def resolve(context: Any, name: String): Object = {
    if (context == null || context.isInstanceOf[Map[String, _]] == false) {
      ValueResolver.UNRESOLVED

    } else {
      val map = context.asInstanceOf[Map[String, _]]
      map.get(name).getOrElse {
        ValueResolver.UNRESOLVED
      }.asInstanceOf[Object]
    }
  }
}
