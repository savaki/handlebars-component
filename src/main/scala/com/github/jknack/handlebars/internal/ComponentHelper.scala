package com.github.jknack.handlebars.internal

import com.github.jknack.handlebars.{Context, Template, Options, Helper}

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
  def findModel(options: Options): Option[Context] = {
    val properties: Map[String, Any] = options.context.model().asInstanceOf[Map[String, Any]]
    val contextsByTemplate: Map[Template, Context] = properties.get(property).map(_.asInstanceOf[Map[Template, Context]]).getOrElse {
      Map[Template, Context]()
    }
    contextsByTemplate.get(options.fn)
  }

  def apply(context: String, options: Options): CharSequence = {
    val contextOption: Option[Context] = findModel(options)
    val context: Context = contextOption.getOrElse {
      null
    }
    options.fn.apply(context)
  }
}
