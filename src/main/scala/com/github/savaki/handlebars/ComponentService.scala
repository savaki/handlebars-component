package com.github.savaki.handlebars

import com.twitter.finagle.Service
import com.twitter.util.Future
import java.util.concurrent.ConcurrentHashMap
import java.util

/**
 * @author matt.ho@gmail.com
 */
trait ComponentService extends Service[ComponentRequest, ComponentResponse] {
  self: Handlebars =>

  protected val components: util.Map[String, Component] = new ConcurrentHashMap[String, Component]()
  protected var packages: Seq[String] = Seq()

  protected def findComponent(name: String): Option[Component] = {
    packages.map {
      pkg => {
        val klass = "%s.%s".format(pkg, name)

        try {
          Class.forName(klass).newInstance().asInstanceOf[Component]
        } catch {
          case throwable: Throwable => null
        }
      }
    }.find(_ != null)
  }

  def apply(request: ComponentRequest): Future[ComponentResponse] = {
    var component: Component = components.get(request.name)
    if (component == null) {
      component = findComponent(request.name).getOrElse {
        throw new RuntimeException("unable to find component with name, %s" format request.name)
      }
      components.put(request.name, component)
    }

    component(request)
  }

  def addToPackages(packages: String*) {
    synchronized {
      this.packages = this.packages ++ packages
    }
  }

  def register(name: String, component: Component) {
    components.put(name, component)
  }

  def buildContext(attributes: Map[String, Any] = Map()): ComponentContext = {
    new ComponentContext(this, attributes)
  }
}

case class ComponentRequest(context: ComponentContext, name: String, args: String*)

case class ComponentResponse(context: util.Map[String, Any] = new util.HashMap())

/**
 * @author matt.ho@gmail.com
 */
case class ComponentContext(service: ComponentService, attributes: Map[String, Any] = Map()) {
  val context: util.Map[String, Any] = {
    val map = new util.HashMap[String, Any]()
    attributes.foreach {
      entry => map.put(entry._1, entry._2)
    }
    util.Collections.unmodifiableMap[String, Any](map)
  }
}
