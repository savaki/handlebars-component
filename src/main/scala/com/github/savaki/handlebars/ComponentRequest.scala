package com.github.savaki.handlebars

/**
 * @author matt.ho@gmail.com
 */
class ComponentRequest {
  var name: String = null
  var args: Array[String] = null
}

object ComponentRequest {
  def apply(name: String, args: String*): ComponentRequest = {
    val request = new ComponentRequest
    request.name = name
    request.args = args.toArray
    request
  }
}
