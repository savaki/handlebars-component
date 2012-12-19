package com.github.jknack.handlebars.internal

import com.github.jknack.handlebars.{Template, Options, Helper}
import com.github.savaki.handlebars.ComponentResponse

/**
 *
 * @author matt.ho@gmail.com
 */
class ComponentHelper(property: String) extends Helper[String] {
  /**
   *
   * @param options the options used to render the template
   * @return
   */
  def findComponentResponse(options: Options): Option[ComponentResponse] = {
    val properties: Map[String, Any] = options.context.model().asInstanceOf[Map[String, Any]]
    val responsesByTemplate: Map[Template, ComponentResponse] = properties.get(property).map(_.asInstanceOf[Map[Template, ComponentResponse]]).getOrElse {
      Map[Template, ComponentResponse]()
    }
    responsesByTemplate.get(options.fn)
  }

  def apply(context: String, options: Options): CharSequence = {
    val responseOption: Option[ComponentResponse] = findComponentResponse(options)
    val response: ComponentResponse = responseOption.getOrElse {
      println("nope")
      new ComponentResponse
    }
    options.fn.apply(response.context)
  }
}
