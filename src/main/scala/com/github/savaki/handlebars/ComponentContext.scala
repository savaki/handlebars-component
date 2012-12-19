package com.github.savaki.handlebars

import java.util

/**
 * @author matt.ho@gmail.com
 */
case class ComponentContext(service: ComponentService, attributes: Map[String, Any] = Map()) {
  val context: util.Map[String, Any] = {
    val map = new util.HashMap[String, Any]()
    attributes.foreach {
      entry => map.put(entry._1, entry._2)
    }
    map
  }
}
