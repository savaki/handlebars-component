package com.github.savaki.handlebars

import com.github.jknack.handlebars.{Handlebars => H, Template => T, ValueResolver, Helper, TemplateLoader}
import com.github.jknack.handlebars.internal.{ComponentHelper, ComponentTemplate}
import java.net.URI

/**
 * Handlebars is a component aware implementation of handlebars.
 *
 * @author matt.ho@gmail.com
 * @param loader the handlebars parser/loader of templates
 * @param cache the optional backing cache to use
 * @param helperName the name of the handlebars helper; "render" by default ... e.g. #render
 * @param propertyName the name of the context property that stores the results of component execution
 */
class Handlebars(loader: TemplateLoader, cache: TemplateCache = NoCache, val helperName: String = "render", val propertyName: String = "_responses") extends ComponentService {
  /**
   * custom resolvers used by handlebars-component to render context values
   */
  var resolvers: Array[ValueResolver] = Array(new MapValueResolver, new ScalaValueResolver)

  /**
   * a reference to the underlying handlebars implementation
   */
  val underlying: H = {
    val handlebars: H = new H(loader)
    handlebars.registerHelper(helperName, new ComponentHelper(propertyName))
    handlebars
  }

  var DELIM_START = "{{"
  var DELIM_END = "}}"

  def compileString(inline: String, delimStart: String = DELIM_START, delimEnd: String = DELIM_END): ComponentTemplate = {
    loadTemplate(inline) {
      underlying.compile(inline, delimStart, delimEnd)
    }
  }

  def compile(uri: URI, delimStart: String = DELIM_START, delimEnd: String = DELIM_END): ComponentTemplate = {
    loadTemplate(uri) {
      underlying.compile(uri, delimStart, delimEnd)
    }
  }

  def registerHelper[H](name: String, helper: Helper[H]): Handlebars = {
    underlying.registerHelper(name, helper)
    this
  }

  private def loadTemplate(key: AnyRef)(t: => T): ComponentTemplate = {
    var template: ComponentTemplate = cache.get(key)
    if (template == null) {
      template = new ComponentTemplate(this, t)
      cache.put(key, template)
      template

    } else {
      template
    }
  }
}
