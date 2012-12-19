package com.github.jknack.handlebars.internal

import com.github.jknack.handlebars.{Context, Template, Handlebars => H}
import scala.collection.JavaConversions._
import com.twitter.util.Future
import java.io.Writer
import com.github.savaki.handlebars.{Handlebars, ComponentContext, ComponentResponse, ComponentRequest}

/**
 * @author matt.ho@gmail.com
 */
class ComponentTemplate(handlebars: Handlebars, template: Template) {
  val blocks: List[Block] = template match {
    case list: TemplateList => {
      list.iterator().toList.map {
        template => template match {
          case block: Block if handlebars.helperName.equalsIgnoreCase(block.name()) => block
          case _ => null
        }
      }.filter(_ != null)
    }
    case _ => Nil
  }

  val blocksWithParams = blocks.map {
    action => action -> new ParamExtractor(action, handlebars.underlying).paramsToString().split(" ")
  }

  def render(context: ComponentContext): Future[String] = {
    val futures: Future[Map[Template, ComponentResponse]] = Future.collect {
      blocksWithParams.map {
        entry => {
          val block: Block = entry._1
          val params: Array[String] = entry._2
          val name = params.head
          val args = params.tail
          val request = ComponentRequest(name, args: _*)
          context.service(request).map(response => block.body() -> response)
        }
      }
    }.map(_.toMap)

    futures.map {
      responses => template(Map(handlebars.propertyName -> responses) ++ context.context)
    }
  }

  class ParamExtractor(resolver: HelperResolver, handlebars: H) extends HelperResolver(handlebars) {

    def text() = ""

    def remove(child: Template) = false

    def merge(context: Context, writer: Writer) {}

    override def paramsToString() = resolver.paramsToString()
  }

}

